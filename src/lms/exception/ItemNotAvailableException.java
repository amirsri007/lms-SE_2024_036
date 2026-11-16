package lms.exception;

/**
 * Custom checked exception indicating that a requested library item is unavailable,
 * already on loan, not found, or ineligible for return.
 * Callers are required by the compiler to handle or declare this exception.
 */
public class ItemNotAvailableException extends Exception {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}
