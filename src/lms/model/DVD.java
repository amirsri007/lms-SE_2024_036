package lms.model;

/**
 * Concrete domain entity representing a Digital Versatile Disc (DVD).
 * Inherits from abstract LibraryItem; does not implement Reservable.
 */
public class DVD extends LibraryItem {
    private int durationMinutes;

    public DVD(String id, String title, int durationMinutes) {
        super(id, title);
        setDurationMinutes(durationMinutes);
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero minutes");
        }
        this.durationMinutes = durationMinutes;
    }

    @Override
    public double calculateLateFee(int daysLate) {
        if (daysLate < 0) {
            throw new IllegalArgumentException("Days late cannot be negative");
        }
        return daysLate * 25.0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id='" + getId() + "', title='" + getTitle() + "', durationMinutes=" + durationMinutes + ", borrowed=" + isBorrowed() + "}";
    }
}
