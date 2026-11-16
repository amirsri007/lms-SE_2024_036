package lms.model;

/**
 * Concrete domain entity representing a Book.
 * Extends abstract LibraryItem and implements Reservable contract.
 */
public class Book extends LibraryItem implements Reservable {
    private String author;
    private Member reservedBy;

    public Book(String id, String title, String author) {
        super(id, title);
        setAuthor(author);
        this.reservedBy = null;
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

    @Override
    public double calculateLateFee(int daysLate) {
        if (daysLate < 0) {
            throw new IllegalArgumentException("Days late cannot be negative");
        }
        return daysLate * 10.0;
    }

    @Override
    public void reserve(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        if (isReserved()) {
            System.out.println("[Refusal] Book '" + getTitle() + "' is already reserved by " + reservedBy.getName() + ".");
            return;
        }
        this.reservedBy = member;
        System.out.println("[Reservation Confirmed] Book '" + getTitle() + "' reserved for " + member.getName() + ".");
    }

    @Override
    public void cancelReservation() {
        if (!isReserved()) {
            System.out.println("[Refusal] Book '" + getTitle() + "' is not currently reserved.");
            return;
        }
        this.reservedBy = null;
        System.out.println("[Reservation Cancelled] Reservation on '" + getTitle() + "' cancelled.");
    }

    @Override
    public boolean isReserved() {
        return reservedBy != null;
    }

    public Member getReservedBy() {
        return reservedBy;
    }

    @Override
    public String toString() {
        String res = isReserved() ? reservedBy.getName() : "none";
        return getClass().getSimpleName() + "{id='" + getId() + "', title='" + getTitle() + "', author='" + author + "', borrowed=" + isBorrowed() + ", reservedBy=" + res + "}";
    }
}
