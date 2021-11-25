import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A Lucene searcher.
 */
public class Searcher implements AutoCloseable {

    private final IndexSearcher searcher;

    private final Analyzer analyzer;

    private final DirectoryReader dirReader;

    public Searcher(Path indexDir, Analyzer analyzer, Similarity similarity) throws IOException {
        this.analyzer = analyzer;

        try (Directory dir = FSDirectory.open(indexDir)) {
            dirReader = DirectoryReader.open(dir);
            searcher = new IndexSearcher(dirReader);
            searcher.setSimilarity(similarity);
        }
    }

    /**
     * Searches for a Cranfield query.
     * @param query A Cranfield query.
     * @param top   The number of top hits.
     * @return      Top hits.
     */
    public ScoreDoc[] search(cran.Query query, int top) throws ParseException, IOException {
        QueryParser parser = new MultiFieldQueryParser(cran.Field.getAllFields(), analyzer);
        return searcher.search(parser.parse(query.getWords()), top).scoreDocs;
    }

    /**
     * Gets Lucene documents from top hits.
     * @param hits  Top hits.
     * @return      Lucene documents.
     */
    public List<Document> documents(ScoreDoc[] hits) throws IOException {
        List<Document> docs = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            docs.add(document(hit));
        }

        return docs;
    }

    /**
     * Gets the Lucene document from a top hit.
     * @param hit   A top hit.
     * @return      A Lucene document.
     */
    public Document document(ScoreDoc hit) throws IOException {
        return searcher.doc(hit.doc);
    }

    @Override
    public void close() throws Exception {
        dirReader.close();
    }
}