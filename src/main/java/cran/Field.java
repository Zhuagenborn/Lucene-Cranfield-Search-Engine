package cran;

/**
 * The fields of a Cranfield document.
 */
public enum Field {

    ID(".I", "id"),

    TITLE(".T", "title"),

    AUTHOR(".A", "author"),

    BIBLIOGRAPHY(".B", "bibliography"),

    WORDS(".W", "words");

    public static String[] getAllFields() {
        return new String[]{ID.getName(), TITLE.getName(), AUTHOR.getName(), BIBLIOGRAPHY.getName(), WORDS.getName()};
    }

    private final String tag;

    private final String name;

    Field(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }
}