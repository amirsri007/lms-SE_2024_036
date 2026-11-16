package lms.service;

import lms.model.Book;
import lms.model.DVD;
import lms.model.LibraryItem;
import lms.model.Loanable;
import lms.model.Member;
import lms.model.Reservable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service managing library operations.
 * Coordinates lending and reservations via interface contracts and provides member ranking via Comparable.
 */
public class Library {
    private final List<LibraryItem> items = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();

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
     * Delegates borrowing to the Loanable contract interface.
     *
     * @param thing  Any entity implementing Loanable
     * @param member The borrowing member
     */
    public void lend(Loanable thing, Member member) {
        if (thing == null) {
            throw new IllegalArgumentException("Loanable item cannot be null");
        }
        thing.loan(member);
    }

    /**
     * Attempts to reserve an item using interface pattern-matching.
     *
     * @param item   The item to reserve
     * @param member The reserving member
     */
    public void reserveIfPossible(LibraryItem item, Member member) {
        if (item instanceof Reservable reservable) {
            reservable.reserve(member);
        } else {
            System.out.println("[Refusal] Item '" + item.getTitle() + "' is not reservable (only Books support reservations).");
        }
    }

    /**
     * Copies the internal member list and sorts by activity without mutating the original list order.
     */
    public void printMembersByActivity() {
        System.out.println("\n--- Member Rankings by Borrowing Activity (Descending) ---");
        List<Member> sortedMembers = new ArrayList<>(members);
        Collections.sort(sortedMembers);

        int rank = 1;
        for (Member m : sortedMembers) {
            System.out.printf("Rank %d: %s (%s) - Borrowed Items: %d (Limit: %d)%n",
                    rank++, m.getName(), m.getId(), m.getBorrowedCount(), m.getMaxBorrowLimit());
        }
    }

    public void printAllLateFees(int daysLate) {
        System.out.println("\n--- Polymorphic Late Fee Calculation (" + daysLate + " days late) ---");
        for (LibraryItem item : items) {
            double fee = item.calculateLateFee(daysLate);
            System.out.printf("[%s] '%s' -> Late Fee: $%.2f%n", item.getClass().getSimpleName(), item.getTitle(), fee);
        }
    }

    public void streamIfPossible(LibraryItem item) {
        if (item instanceof DVD dvd) {
            System.out.println("Streaming DVD '" + dvd.getTitle() + "' (Duration: " + dvd.getDurationMinutes() + " mins)...");
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
