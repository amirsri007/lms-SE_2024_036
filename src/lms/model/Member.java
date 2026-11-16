package lms.model;

/**
 * Superclass representing library members.
 * Implements Comparable to sort members based on borrowing activity in descending order.
 */
public class Member implements Comparable<Member> {
    private String memberId;
    private String name;
    private int borrowedCount;
    private int maxBorrowLimit;

    public Member(String memberId, String name, int maxBorrowLimit) {
        setMemberId(memberId);
        setName(name);
        setMaxBorrowLimit(maxBorrowLimit);
        setBorrowedCount(0);
    }

    public Member(String memberId, String name) {
        this(memberId, name, 5);
    }

    public String getMemberId() {
        return memberId;
    }

    public String getId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID cannot be blank");
        }
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(int borrowedCount) {
        if (borrowedCount < 0) {
            throw new IllegalArgumentException("Borrowed count cannot be negative");
        }
        this.borrowedCount = borrowedCount;
    }

    public int getMaxBorrowLimit() {
        return maxBorrowLimit;
    }

    public void setMaxBorrowLimit(int maxBorrowLimit) {
        if (maxBorrowLimit <= 0) {
            throw new IllegalArgumentException("Max borrow limit must be greater than zero");
        }
        this.maxBorrowLimit = maxBorrowLimit;
    }

    public void incrementBorrowedCount() {
        setBorrowedCount(this.borrowedCount + 1);
    }

    /**
     * Compares two members by borrowedCount descending (most active members first).
     *
     * @param other The other member to compare against
     * @return Negative if this > other, positive if this < other, 0 if equal
     */
    @Override
    public int compareTo(Member other) {
        if (other == null) {
            return -1;
        }
        return Integer.compare(other.borrowedCount, this.borrowedCount);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id='" + memberId + "', name='" + name + "', borrowedCount=" + borrowedCount + ", maxLimit=" + maxBorrowLimit + "}";
    }
}
