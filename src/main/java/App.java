import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.ClassicSimilarity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class App {

    private static final String CRAN_DOCS = "cran.all.1400";

    private static final String CRAN_QUERY = "cran.qry";

    private static final Path DATA_DIR = Paths.get("data");

    private static final Path INDEX_DIR = DATA_DIR.resolve("index");

    // Only get the top 2 results.
    private static final int TOP_NUM = 2;

    public static void main(String[] args) throws IOException {
        try {
            // Read documents.
            String docText = readResource(CRAN_DOCS);
            List<cran.Document> docs = cran.Parser.parseDocuments(docText);

            // Build indexes.
            Indexer indexer = new Indexer(new StandardAnalyzer(), new ClassicSimilarity());
            indexer.index(docs, INDEX_DIR);

            // Read queries.
            String queryText = readResource(CRAN_QUERY);
            List<cran.Query> queries = cran.Parser.parseQueries(queryText);

            // Search for each query.
            Searcher searcher = new Searcher(INDEX_DIR, new StandardAnalyzer(), new ClassicSimilarity());

            for (cran.Query query : queries) {
                System.out.printf("%nThe results for the query with ID %d: %s%n", query.getId(), query.getWords());
                for (ScoreDoc hit: searcher.search(query, TOP_NUM)) {
                    Document doc = searcher.document(hit);
                    int id = doc.getField(cran.Field.ID.getName()).numericValue().intValue();
                    String title = doc.getField(cran.Field.TITLE.getName()).stringValue();
                    System.out.printf("\tSCORE: %f, DOCUMENT ID: %d, TITLE: %s%n", hit.score, id, title);
                }
            }

        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Reads a text resource file as a string.
     * @param file  A file path.
     * @return      The content.
     * @throws FileNotFoundException    The file does not exist.
     */
    private static String readResource(String file) throws IOException {
        try (InputStream stream = App.class.getResourceAsStream(file)) {
            if (stream == null) {
                throw new FileNotFoundException(String.format("The file '%s' does not exist.", file));
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                List<String> lines = reader.lines().collect(Collectors.toList());
                return String.join(" ",  lines);
            }
        }
    }
}