package lms;

import lms.exception.ItemNotAvailableException;
import lms.exception.MemberNotFoundException;
import lms.model.Book;
import lms.model.DVD;
import lms.model.Magazine;
import lms.model.Staff;
import lms.model.Student;
import lms.service.Library;

/**
 * Phase 4 Entry Point: Comprehensive exception handling verification.
 * Tests 8 distinct operational scenarios covering custom checked exceptions,
 * custom unchecked exceptions, invariant violations, and transactional quota restoration.
 */
public class Main {

    /**
     * Helper method wrapping borrow transactions with required try-catch-finally block.
     * Guarantees that the finally log executes regardless of transaction outcome.
     *
     * @param library  Target library service
     * @param itemId   Identifier of item to borrow
     * @param memberId Identifier of requesting member
     */
    public static void attempt(Library library, String itemId, String memberId) {
        try {
            library.borrowItem(itemId, memberId);
            System.out.println("Success: item '" + itemId + "' borrowed by member '" + memberId + "'");
        } catch (ItemNotAvailableException e) {
            System.out.println("ItemNotAvailableException caught: " + e.getMessage());
        } catch (MemberNotFoundException e) {
            System.out.println("MemberNotFoundException caught: " + e.getMessage());
        } finally {
            System.out.println("attempt finished: item " + itemId + ", member " + memberId);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Phase 4: Custom Exception Handling & Transactions ===\n");

        Library library = new Library();

        // Setup Catalog
        Book b1 = new Book("B001", "Clean Code", "Robert C. Martin");
        Book b2 = new Book("B002", "Effective Java", "Joshua Bloch");
        DVD d1 = new DVD("D001", "Inception", 148);
        Magazine m1 = new Magazine("M101", "IEEE Software", 42);

        library.addItem(b1);
        library.addItem(b2);
        library.addItem(d1);
        library.addItem(m1);

        // Setup Members
        Student student = new Student("S101", "Alice Perera");       // Quota = 3
        Staff staff = new Staff("E201", "Dr. Kamal Gunasekara");     // Quota = 10

        library.addMember(student);
        library.addMember(staff);

        // --- Scenario 1: Successful Borrow ---
        System.out.println("[Scenario 1] Successful Borrow");
        attempt(library, "B001", "S101");
        System.out.println();

        // --- Scenario 2: Item Already On Loan ---
        System.out.println("[Scenario 2] Item Already On Loan");
        attempt(library, "B001", "E201");
        System.out.println();

        // --- Scenario 3: Unknown Member (M999) ---
        System.out.println("[Scenario 3] Unknown Member (Unchecked MemberNotFoundException)");
        attempt(library, "D001", "M999");
        System.out.println();

        // --- Scenario 4: Unknown Item (X404) ---
        System.out.println("[Scenario 4] Unknown Item (Checked ItemNotAvailableException)");
        attempt(library, "X404", "S101");
        System.out.println();

        // --- Scenario 5: Returning an Unborrowed Item ---
        System.out.println("[Scenario 5] Returning an Unborrowed Item");
        try {
            library.returnItem("D001");
        } catch (ItemNotAvailableException e) {
            System.out.println("ItemNotAvailableException caught: " + e.getMessage());
        }
        System.out.println();

        // --- Scenario 6: Catching IllegalArgumentException on Blank Book Title ---
        System.out.println("[Scenario 6] Invariant Validation (IllegalArgumentException on Blank Title)");
        try {
            new Book("B999", "   ", "Anonymous");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException caught: " + e.getMessage());
        }
        System.out.println();

        // --- Scenario 7: Student Borrowing Limit of 3 ---
        System.out.println("[Scenario 7] Enforcing Student Borrowing Limit (Limit = 3)");
        attempt(library, "D001", "S101"); // Loan 2 for Alice
        attempt(library, "M101", "S101"); // Loan 3 for Alice (quota reached)
        attempt(library, "B002", "S101"); // Loan 4 attempt: Quota exceeded exception
        System.out.println();

        // --- Scenario 8: Returning an Item to Free a Slot for a Subsequent Borrow ---
        System.out.println("[Scenario 8] Slot Release Bug Fix: Quota Restoration on Return");
        try {
            System.out.println("Active borrows for Alice before return: " + student.getBorrowedCount());
            library.returnItem("B001");
            System.out.println("Active borrows for Alice after returning B001: " + student.getBorrowedCount());
        } catch (ItemNotAvailableException e) {
            System.out.println("Error returning item: " + e.getMessage());
        }

        // Alice now has available quota (2/3), so B002 should succeed:
        attempt(library, "B002", "S101");
        System.out.println("Final active borrows for Alice: " + student.getBorrowedCount());
    }
}
