package lms.service;

import lms.exception.ItemNotAvailableException;
import lms.exception.MemberNotFoundException;
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
 * Service orchestrating library operations and transactional workflows.
 * Implements robust domain exception handling for lookups, loans, and returns.
 */
public class Library {
    private final List<LibraryItem> items = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();

    public void addItem(LibraryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Library item cannot be null");
        }
        items.add(item);
    }

    public void addItem(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        items.add(book);
    }

    public void addItem(DVD dvd) {
        if (dvd == null) {
            throw new IllegalArgumentException("DVD cannot be null");
        }
        items.add(dvd);
    }

    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        members.add(member);
    }

    /**
     * Looks up a member by memberId.
     * Throws unchecked MemberNotFoundException when ID is not present.
     *
     * @param memberId Target member identifier
     * @return Found Member instance
     */
    public Member findMember(String memberId) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID cannot be blank");
        }
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(memberId)) {
                return m;
            }
        }
        throw new MemberNotFoundException(memberId);
    }

    /**
     * Looks up a library item by itemId.
     * Throws checked ItemNotAvailableException when item ID is not present.
     *
     * @param itemId Target item identifier
     * @return Found LibraryItem instance
     * @throws ItemNotAvailableException if no item matches the given itemId
     */
    public LibraryItem findItem(String itemId) throws ItemNotAvailableException {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item ID cannot be blank");
        }
        for (LibraryItem item : items) {
            if (item.getId().equalsIgnoreCase(itemId)) {
                return item;
            }
        }
        throw new ItemNotAvailableException("No item with id '" + itemId + "'");
    }

    /**
     * Executes a borrowing transaction between an item and a member.
     *
     * @param itemId   Identifier of item to borrow
     * @param memberId Identifier of borrower
     * @throws ItemNotAvailableException if item does not exist, is on loan, or borrow limit is reached
     */
    public void borrowItem(String itemId, String memberId) throws ItemNotAvailableException {
        Member member = findMember(memberId);
        LibraryItem item = findItem(itemId);
        lend(item, member);
    }

    /**
     * Processes the return of an item and frees the borrower's borrowing quota.
     *
     * @param itemId Identifier of item being returned
     * @throws ItemNotAvailableException if item does not exist or is not currently on loan
     */
    public void returnItem(String itemId) throws ItemNotAvailableException {
        LibraryItem item = findItem(itemId);
        item.returnItem();
    }

    /**
     * Delegates borrowing to the Loanable contract interface.
     *
     * @param thing  Item implementing Loanable
     * @param member Paton borrowing the item
     * @throws ItemNotAvailableException if loan cannot be completed
     */
    public void lend(Loanable thing, Member member) throws ItemNotAvailableException {
        if (thing == null) {
            throw new IllegalArgumentException("Loanable item cannot be null");
        }
        thing.loan(member);
    }

    public void reserveIfPossible(LibraryItem item, Member member) {
        if (item instanceof Reservable reservable) {
            reservable.reserve(member);
        } else {
            System.out.println("[Refusal] Item '" + item.getTitle() + "' is not reservable (only Books support reservations).");
        }
    }

    public void printMembersByActivity() {
        System.out.println("\n--- Member Rankings by Borrowing Activity (Descending) ---");
        List<Member> sortedMembers = new ArrayList<>(members);
        Collections.sort(sortedMembers);

        int rank = 1;
        for (Member m : sortedMembers) {
            System.out.printf("Rank %d: %s (%s) - Borrowed Items: %d (Limit: %d)%n",
                    rank++, m.getName(), m.getMemberId(), m.getBorrowedCount(), m.getMaxBorrowLimit());
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
