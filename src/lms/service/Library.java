package lms.service;

import lms.model.Book;
import lms.model.DVD;
import lms.model.LibraryItem;
import lms.model.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service managing polymorphic items and members.
 * Demonstrates static binding through method overloading and dynamic binding through polymorphic method invocation.
 */
public class Library {
    private final List<LibraryItem> items = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();

    // --- Static Binding Demonstration (Overloaded Methods) ---

    public void addItem(LibraryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Library item cannot be null");
        }
        System.out.println("[Static Binding] addItem(LibraryItem) called for: " + item.getTitle());
        items.add(item);
    }

    public void addItem(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        System.out.println("[Static Binding] addItem(Book) called for: " + book.getTitle());
        items.add(book);
    }

    public void addItem(DVD dvd) {
        if (dvd == null) {
            throw new IllegalArgumentException("DVD cannot be null");
        }
        System.out.println("[Static Binding] addItem(DVD) called for: " + dvd.getTitle());
        items.add(dvd);
    }

    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        members.add(member);
    }

    /**
     * Demonstrates dynamic binding.
     * Iterates through all items invoking calculateLateFee without any conditional branching or type checks.
     * Runtime method dispatch resolves the actual overriding implementation (e.g., DVD vs Book).
     *
     * @param daysLate Number of overdue days
     */
    public void printAllLateFees(int daysLate) {
        System.out.println("\n--- Polymorphic Late Fee Calculation (" + daysLate + " days late) ---");
        for (LibraryItem item : items) {
            double fee = item.calculateLateFee(daysLate);
            System.out.printf("[%s] '%s' -> Late Fee: $%.2f%n", item.getClass().getSimpleName(), item.getTitle(), fee);
        }
    }

    /**
     * Demonstrates safe downcasting with pattern matching instanceof.
     *
     * @param item The library item to inspect for streaming capability
     */
    public void streamIfPossible(LibraryItem item) {
        if (item instanceof DVD dvd) {
            System.out.println("Streaming DVD '" + dvd.getTitle() + "' (Duration: " + dvd.getDurationMinutes() + " mins)... Enjoy the movie!");
        } else {
            System.out.println("Item '" + item.getTitle() + "' is not streamable: only DVDs can be streamed.");
        }
    }

    public void listItems() {
        System.out.println("--- Library Items ---");
        for (LibraryItem item : items) {
            System.out.println(item);
        }
    }

    public void listMembers() {
        System.out.println("--- Library Members ---");
        for (Member member : members) {
            System.out.println(member);
        }
    }

    public List<LibraryItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }
}
