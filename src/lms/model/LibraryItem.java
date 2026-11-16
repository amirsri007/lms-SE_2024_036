package lms.model;

/**
 * Abstract base class for library inventory items, enforcing Loanable contract.
 * Centralizes borrowing state logic while mandating concrete implementations of calculateLateFee.
 */
public abstract class LibraryItem implements Loanable {
    private String id;
    private String title;
    private boolean isBorrowed;

    public LibraryItem(String id, String title) {
        setId(id);
        setTitle(title);
        this.isBorrowed = false;
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

    /**
     * Abstract method enforcing item-specific late fee calculation rules.
     *
     * @param daysLate Number of days past due date
     * @return Computed overdue fee
     */
    public abstract double calculateLateFee(int daysLate);

    @Override
    public void loan(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Borrowing member cannot be null");
        }
        if (isBorrowed) {
            System.out.println("[Refusal] '" + title + "' is already on loan.");
            return;
        }
        if (member.getBorrowedCount() >= member.getMaxBorrowLimit()) {
            System.out.println("[Refusal] Member '" + member.getName() + "' has reached the limit of " + member.getMaxBorrowLimit() + " items.");
            return;
        }

        this.isBorrowed = true;
        member.incrementBorrowedCount();
        System.out.println("[Loan Confirmed] '" + title + "' loaned to " + member.getName() + ".");
    }

    @Override
    public void returnItem() {
        if (!isBorrowed) {
            System.out.println("[Refusal] '" + title + "' is not on loan.");
            return;
        }

        this.isBorrowed = false;
        System.out.println("[Return Confirmed] '" + title + "' has been successfully returned.");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id='" + id + "', title='" + title + "', borrowed=" + isBorrowed + "}";
    }
}
