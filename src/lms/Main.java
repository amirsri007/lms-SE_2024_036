package lms;

import lms.model.Book;
import lms.model.DVD;
import lms.model.LibraryItem;
import lms.model.Staff;
import lms.model.Student;
import lms.service.Library;

/**
 * Phase 2 Entry Point: Demonstrates inheritance hierarchies, static vs dynamic binding,
 * and safe downcasting using pattern-matching instanceof.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Phase 2: Inheritance & Polymorphism ===");

        Library library = new Library();

        // 1. Static Binding Demonstration (Overloaded Methods)
        System.out.println("\n--- Demonstrating Static Binding (Overloaded addItem) ---");
        Book book = new Book("B001", "Clean Code", "Robert C. Martin");
        DVD dvd = new DVD("D001", "Inception", 148);
        LibraryItem genericItem = new Book("B002", "Effective Java", "Joshua Bloch");

        // Static binding resolves at compile time based on the declared reference type:
        library.addItem(book);        // Calls addItem(Book)
        library.addItem(dvd);         // Calls addItem(DVD)
        library.addItem(genericItem); // Calls addItem(LibraryItem) despite runtime type being Book

        // 2. Member Hierarchy
        System.out.println("\n--- Registering Members (Student & Staff) ---");
        Student student = new Student("S101", "Kasun Perera");
        Staff staff = new Staff("E201", "Dr. Kamal Gunasekara");

        library.addMember(student);
        library.addMember(staff);

        library.listItems();
        library.listMembers();

        // 3. Dynamic Binding Demonstration (Polymorphic Method Dispatch)
        // Late fee calculation: Book = $10.0/day, DVD = $25.0/day
        library.printAllLateFees(4);

        // 4. Safe Pattern Matching Downcasting
        System.out.println("\n--- Demonstrating Safe Pattern Matching (streamIfPossible) ---");
        library.streamIfPossible(dvd);
        library.streamIfPossible(book);
    }
}
