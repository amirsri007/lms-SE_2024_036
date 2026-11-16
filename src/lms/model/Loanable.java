package lms.model;

import lms.exception.ItemNotAvailableException;

/**
 * Interface contract defining loan capabilities.
 * Methods throw checked ItemNotAvailableException to signal domain error conditions.
 */
public interface Loanable {
    void loan(Member member) throws ItemNotAvailableException;
    void returnItem() throws ItemNotAvailableException;
}
