import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Lucene index builder.
 */
public class Indexer {

    private final Analyzer analyzer;

    private final Similarity similarity;

    public Indexer(Analyzer analyzer, Similarity similarity) {
        this.analyzer = analyzer;
        this.similarity = similarity;
    }

    /**
     * Builds indexes for Cranfield documents.
     * @param docs      A Cranfield document list.
     * @param indexDir  A directory to store indexes.
     */
    public void index(List<cran.Document> docs, Path indexDir) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(similarity);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try (Directory dir = FSDirectory.open(indexDir);
             IndexWriter writer = new IndexWriter(dir, config)) {
            for (cran.Document doc : docs) {
                process(writer, doc);
            }
        }
    }

    /**
     * Builds an index for a Cranfield document.
     * @param writer    A Lucene index writer.
     * @param doc       A Cranfield document.
     */
    private static void process(IndexWriter writer, cran.Document doc) {
        try {
            Document luceneDoc = new Document();
            luceneDoc.add(new IntPoint(cran.Field.ID.getName(), doc.getId()));
            luceneDoc.add(new StoredField(cran.Field.ID.getName(), doc.getId()));

            // `StringField` is used to indexing a single string that not supposed to be separated, like an email address.
            // `TextField` is similar to `StringField`, but it is for tokenized strings.
            //
            // `Field.Store` is used to indicate whether a field should be fully stored and returned with search results.
            luceneDoc.add(new TextField(cran.Field.TITLE.getName(), doc.getTitle(), Field.Store.YES));
            luceneDoc.add(new TextField(cran.Field.AUTHOR.getName(), doc.getAuthor(), Field.Store.YES));
            luceneDoc.add(new TextField(cran.Field.BIBLIOGRAPHY.getName(), doc.getBibliography(), Field.Store.NO));
            luceneDoc.add(new TextField(cran.Field.WORDS.getName(), doc.getWords(), Field.Store.NO));

            writer.addDocument(luceneDoc);
            Logger.getGlobal().log(Level.INFO, String.format("Lucene IndexWriter has built an index for the document with ID %d.",  doc.getId()));

        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, String.format("Lucene IndexWriter failed to add the document with ID %d: %s.",  doc.getId(), e));
        }
    }
}