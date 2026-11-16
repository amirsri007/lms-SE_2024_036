package lms;

import lms.model.Book;
import lms.model.Member;
import lms.service.Library;

/**
 * Phase 1 Entry Point: Tests encapsulation, validation, and object composition.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Phase 1: Classes, Objects & Encapsulation ===");

        Library library = new Library();

        // Populate 3 books
        Book b1 = new Book("Clean Code", "Robert C. Martin", "978-0132350884");
        Book b2 = new Book("Design Patterns", "Erich Gamma et al.", "978-0201633610");
        Book b3 = new Book("Effective Java", "Joshua Bloch", "978-0134685991");

        library.addBook(b1);
        library.addBook(b2);
        library.addBook(b3);

        // Populate 2 members
        Member m1 = new Member("M001", "Alice Johnson");
        Member m2 = new Member("M002", "Bob Smith");

        library.addMember(m1);
        library.addMember(m2);

        // List both collections
        library.listBooks();
        library.listMembers();

        // Demonstrate validation invariant failure
        System.out.println("\n--- Testing Invariant Validation ---");
        try {
            new Book("", "Nobody", "000-0000000000");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected invalid book: " + e.getMessage());
        }
    }
}
