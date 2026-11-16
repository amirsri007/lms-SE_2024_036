package lms.model;

/**
 * Domain entity representing a book in the Library Management System.
 * Adheres to strict encapsulation principles with validated mutators.
 */
public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isBorrowed;

    /**
     * Constructs a new Book instance.
     * Invokes validated setters to prevent bypassing domain constraints during instantiation.
     *
     * @param title  Book title
     * @param author Book author
     * @param isbn   International Standard Book Number
     */
    public Book(String title, String author, String isbn) {
        setTitle(title);
        setAuthor(author);
        setIsbn(isbn);
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be blank");
        }
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be blank");
        }
        this.isbn = isbn;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.isBorrowed = borrowed;
    }

    @Override
    public String toString() {
        return "Book{title='" + title + "', author='" + author + "', isbn='" + isbn + "', borrowed=" + isBorrowed + "}";
    }
}
