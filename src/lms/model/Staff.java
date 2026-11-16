package lms.model;

/**
 * Subclass representing a staff member.
 * Extends borrowing capacity to a maximum quota of 10 items.
 */
public class Staff extends Member {
    private static final int DEFAULT_LIMIT = 10;

    public Staff(String memberId, String name) {
        super(memberId, name, DEFAULT_LIMIT);
    }
}
