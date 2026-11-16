package lms.exception;

/**
 * Custom unchecked exception indicating that a member lookup failed.
 * Extends RuntimeException to represent precondition or lookup failure.
 */
public class MemberNotFoundException extends RuntimeException {
    private final String memberId;

    public MemberNotFoundException(String memberId) {
        super("No member with id '" + memberId + "'");
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }
}
