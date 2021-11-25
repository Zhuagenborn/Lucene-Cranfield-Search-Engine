package cran;

/**
 * A Cranfield query.
 */
public class Query {

    private final int id;

    private final String words;

    public Query(int id, String words) {
        this.id = id;
        this.words = words;
    }

    public int getId() {
        return id;
    }

    public String getWords() {
        return words;
    }
}