package lms.model;

/**
 * Interface contract defining reservation capabilities for library items.
 */
public interface Reservable {
    void reserve(Member member);
    void cancelReservation();
    boolean isReserved();
}
