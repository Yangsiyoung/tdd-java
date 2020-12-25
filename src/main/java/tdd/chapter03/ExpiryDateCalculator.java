package tdd.chapter03;

import java.time.LocalDate;

public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        return payData.getBillingDate().plusMonths(1);
    }
}
