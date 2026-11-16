package lms.model;

/**
 * Domain entity representing a library member.
 * Demonstrates state encapsulation and controlled mutator access.
 */
public class Member {
    private String memberId;
    private String name;
    private int borrowedCount;

    /**
     * Constructs a Member instance.
     * Validates input invariants through setter methods.
     *
     * @param memberId Unique member identifier
     * @param name     Full name of member
     */
    public Member(String memberId, String name) {
        setMemberId(memberId);
        setName(name);
        setBorrowedCount(0);
    }

    public String getMemberId() {
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

    /**
     * Increments the count of currently borrowed items by 1.
     * Delegates to setBorrowedCount to preserve validation invariants.
     */
    public void incrementBorrowedCount() {
        setBorrowedCount(this.borrowedCount + 1);
    }

    @Override
    public String toString() {
        return "Member{id='" + memberId + "', name='" + name + "', borrowedCount=" + borrowedCount + "}";
    }
}
