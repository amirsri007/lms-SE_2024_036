package lms.model;

import lms.exception.ItemNotAvailableException;

/**
 * Abstract superclass representing library catalog items.
 * Implements Loanable, enforcing checked exceptions on loan/return and tracking active borrower.
 */
public abstract class LibraryItem implements Loanable {
    private String id;
    private String title;
    private boolean isBorrowed;
    private Member borrower;

    public LibraryItem(String id, String title) {
        setId(id);
        setTitle(title);
        this.isBorrowed = false;
        this.borrower = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be blank");
        }
        this.id = id;
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

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.isBorrowed = borrowed;
    }

    public Member getBorrower() {
        return borrower;
    }

    public abstract double calculateLateFee(int daysLate);

    @Override
    public void loan(Member member) throws ItemNotAvailableException {
        if (member == null) {
            throw new IllegalArgumentException("Borrowing member cannot be null");
        }
        if (isBorrowed) {
            throw new ItemNotAvailableException(title + " is already on loan.");
        }
        if (member.getBorrowedCount() >= member.getMaxBorrowLimit()) {
            throw new ItemNotAvailableException(member.getName() + " has reached the limit of " + member.getMaxBorrowLimit() + " items.");
        }

        this.isBorrowed = true;
        this.borrower = member;
        member.incrementBorrowedCount();
        System.out.println("[Loan Confirmed] '" + title + "' loaned to " + member.getName() + ".");
    }

    @Override
    public void returnItem() throws ItemNotAvailableException {
        if (!isBorrowed) {
            throw new ItemNotAvailableException(title + " is not on loan.");
        }

        if (this.borrower != null) {
            this.borrower.decrementBorrowedCount();
            this.borrower = null;
        }
        this.isBorrowed = false;
        System.out.println("[Return Confirmed] '" + title + "' has been successfully returned.");
    }

    @Override
    public String toString() {
        String borrowerInfo = isBorrowed && borrower != null ? "borrower='" + borrower.getName() + "'" : "borrowed=false";
        return getClass().getSimpleName() + "{id='" + id + "', title='" + title + "', " + borrowerInfo + "}";
    }
}
