package tdd.chapter03;

import java.time.LocalDate;

public class ExpiryDateCalculator {
    public LocalDate calculate(LocalDate date, int payAmount) {
        return date.plusMonths(1);
    }
}
