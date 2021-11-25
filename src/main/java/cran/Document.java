package cran;

/**
 * A Cranfield document.
 */
public class Document {

    private final int id;

    private final String title;

    private final String author;

    private final String bibliography;

    private final String words;

    public Document(int id, String title, String author, String bibliography, String words) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.bibliography = bibliography;
        this.words = words;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBibliography() {
        return bibliography;
    }

    public String getWords() {
        return words;
    }
}