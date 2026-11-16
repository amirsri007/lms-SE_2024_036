package lms;

import lms.model.Book;
import lms.model.DVD;
import lms.model.Magazine;
import lms.model.Staff;
import lms.model.Student;
import lms.service.Library;

/**
 * Phase 3 Entry Point: Demonstrates abstract classes, interface contracts (Loanable, Reservable),
 * and Comparable member activity sorting.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Phase 3: Abstract Classes & Interfaces ===");

        Library library = new Library();

        // 1. Populate Catalog (Book, DVD, Magazine)
        Book book1 = new Book("B001", "Clean Code", "Robert C. Martin");
        Book book2 = new Book("B002", "Effective Java", "Joshua Bloch");
        DVD dvd1 = new DVD("D001", "Inception", 148);
        Magazine mag1 = new Magazine("M101", "IEEE Software", 42);

        library.addItem(book1);
        library.addItem(book2);
        library.addItem(dvd1);
        library.addItem(mag1);

        // 2. Populate Members (Student & Staff)
        Student student = new Student("S101", "Kasun Perera");        // Max limit 3
        Staff staff = new Staff("E201", "Dr. Kamal Gunasekara");     // Max limit 10

        library.addMember(student);
        library.addMember(staff);

        // 3. Polymorphic Late Fees (Book $10, DVD $25, Magazine $5)
        library.printAllLateFees(3);

        // 4. Interface Demonstration: Loanable (lend)
        System.out.println("\n--- Testing Loanable Contract (lend) ---");
        library.lend(book1, student); // Loan 1
        library.lend(dvd1, student);  // Loan 2
        library.lend(mag1, student);  // Loan 3 (student reaches limit)

        Book book3 = new Book("B003", "Refactoring", "Martin Fowler");
        library.addItem(book3);
        library.lend(book3, student); // Refused: limit reached

        // Attempt to borrow already loaned item
        library.lend(book1, staff);   // Refused: already borrowed

        // 5. Interface Demonstration: Reservable (reserveIfPossible)
        System.out.println("\n--- Testing Reservable Contract (reserveIfPossible) ---");
        library.reserveIfPossible(book3, student); // Book is Reservable
        library.reserveIfPossible(dvd1, staff);    // DVD is not Reservable
        library.reserveIfPossible(mag1, staff);    // Magazine is not Reservable

        // 6. Comparable Sorting: Member Activity
        // Staff borrows book3
        library.lend(book3, staff);
        library.printMembersByActivity();
    }
}
