package lms.model;

/**
 * Subclass representing a student member.
 * Restricts borrowing capacity to a maximum quota of 3 items.
 */
public class Student extends Member {
    private static final int DEFAULT_LIMIT = 3;

    public Student(String memberId, String name) {
        super(memberId, name, DEFAULT_LIMIT);
    }
}
