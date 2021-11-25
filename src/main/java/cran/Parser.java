package cran;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A parser for the Cranfield collection.
 */
public class Parser {

    private static final int DOC_FIELD_NUM = 5;

    private static final int DOC_ID_IDX = 0;
    private static final int DOC_TITLE_IDX = 1;
    private static final int DOC_AUTHOR_IDX = 2;
    private static final int DOC_BIBLIOGRAPHY_IDX = 3;
    private static final int DOC_WORDS_IDX = 4;

    private static final int QUERY_ID_IDX = 0;
    private static final int QUERY_WORDS_IDX = 1;

    /**
     * Parses documents.
     * @param text  The content of file 'cran.all.1400'.
     * @return      A document list.
     */
    public static List<Document> parseDocuments(String text) {
        String[] contents = text.split("\\.I\\s*", -1);
        List<Document> docs = new ArrayList<>();
        for (String content : contents) {
            content = content.trim();
            if (content.isEmpty()) {
                continue;
            }

            String[] validFields = content.split("\\s*\\.[TABW]\\s*", -1);
            String[] fields = new String[DOC_FIELD_NUM];
            Arrays.fill(fields, "");
            System.arraycopy(validFields, 0, fields, 0, Math.min(validFields.length, DOC_FIELD_NUM));

            docs.add(new Document(Integer.parseInt(fields[DOC_ID_IDX]), fields[DOC_TITLE_IDX], fields[DOC_AUTHOR_IDX], fields[DOC_BIBLIOGRAPHY_IDX], fields[DOC_WORDS_IDX]));
        }

        return docs;
    }

    /**
     * Parses queries.
     * @param text  The content of file 'cran.qry'.
     * @return      A query list.
     */
    public static List<Query> parseQueries(String text) {
        String[] contents = text.replace("?", "").split("\\.I\\s*");
        List<Query> queries = new ArrayList<>();
        for (String content : contents) {
            content = content.trim();
            if (content.isEmpty()) {
                continue;
            }

            String[] fields = content.split("\\s*\\.W\\s*", -1);
            queries.add(new Query(Integer.parseInt(fields[QUERY_ID_IDX]), fields[QUERY_WORDS_IDX]));
        }

        return queries;
    }
}