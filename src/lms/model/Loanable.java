package lms.model;

/**
 * Interface contract defining loan capabilities for circulating items.
 */
public interface Loanable {
    void loan(Member member);
    void returnItem();
}
