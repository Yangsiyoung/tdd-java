package tdd.chapter03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpiryDateCalculatorTest {

    private void assertExpiryDate(PayData payData, LocalDate expect) {
        ExpiryDateCalculator expiryDateCalculator = new ExpiryDateCalculator();
        LocalDate result = expiryDateCalculator.calculate(payData);
        assertEquals(expect, result);
    }

    @DisplayName("만원 납부하면 한달 뒤가 만료일이 됨")
    @Test
    public void pay10000Won() {
        LocalDate billingDate = LocalDate.of(2020, 12, 20);
        LocalDate expect = LocalDate.of(2021, 1, 20);

        int payAmount = 10000;
        PayData payData = PayData.builder().payAmount(payAmount).billingDate(billingDate).build();
        assertExpiryDate(payData, expect);

        LocalDate billingDate2 = LocalDate.of(2021, 1, 20);
        LocalDate expect2 = LocalDate.of(2021, 2, 20);
        PayData payData2 = PayData.builder().payAmount(payAmount).billingDate(billingDate2).build();
        assertExpiryDate(payData2, expect2);
    }

    @DisplayName("납부일과 한달 뒤 일자가 같지 않음")
    @Test
    public void notSameDayPay10000Won() {
        LocalDate billingDate = LocalDate.of(2021, 1, 30);
        LocalDate expect = LocalDate.of(2021, 2, 28);
        int payAmount = 10000;
        PayData payData = PayData.builder().payAmount(payAmount).billingDate(billingDate).build();
        assertExpiryDate(payData, expect);
    }

    @DisplayName("첫 납부일과 만료일자가 다를 때 만원 납부")
    @Test
    public void notSameDayPayAgain10000Won() {
        LocalDate firstBillingDate = LocalDate.of(2021, 1, 31);
        LocalDate billingDate = LocalDate.of(2021, 2, 28);
        LocalDate expect = LocalDate.of(2021, 3, 31);
        int payAmount = 10000;
        PayData payData = PayData.builder().payAmount(payAmount).firstBillingDate(firstBillingDate).billingDate(billingDate).build();
        assertExpiryDate(payData, expect);

        firstBillingDate = LocalDate.of(2021, 1, 30);
        expect = LocalDate.of(2021, 3, 30);
        payData = PayData.builder().payAmount(payAmount).firstBillingDate(firstBillingDate).billingDate(billingDate).build();
        assertExpiryDate(payData, expect);
    }

    @DisplayName("2만원 이상 납부하면 비례해서 만료일 계산")
    @Test
    public void calculateInProportionToWon() {
        LocalDate billingDate = LocalDate.of(2021, 1, 31);
        LocalDate expect = LocalDate.of(2021, 3, 31);
        int payAmount = 20000;
        PayData payData = PayData.builder().billingDate(billingDate).payAmount(payAmount).build();
        assertExpiryDate(payData, expect);

        expect = LocalDate.of(2021, 4, 30);
        payAmount = 30000;
        payData = PayData.builder().billingDate(billingDate).payAmount(payAmount).build();
        assertExpiryDate(payData, expect);
    }

    @DisplayName("첫 납부일과 만료일자가 다를 때 2만원 이상 납부")
    @Test
    public void notSameDayPayAgainOver10000Won() {
        LocalDate firstBillingDate = LocalDate.of(2021, 1, 31);
        LocalDate billingDate = LocalDate.of(2021, 2, 28);
        int payAmount = 20000;
        LocalDate expect = LocalDate.of(2021, 4, 30);

        PayData payData = PayData.builder()
                            .firstBillingDate(firstBillingDate)
                            .billingDate(billingDate)
                            .payAmount(payAmount)
                            .build();

        assertExpiryDate(payData, expect);

        payAmount = 30000;
        expect = LocalDate.of(2021, 5, 31);
        payData = PayData.builder()
                .firstBillingDate(firstBillingDate)
                .billingDate(billingDate)
                .payAmount(payAmount)
                .build();
        assertExpiryDate(payData, expect);

    }

    @DisplayName("10만원 납부하면 1년 제공")
    @Test
    public void pay100000Won() {
        LocalDate billingDate = LocalDate.of(2021, 1, 31);
        int payAmount = 100_000;
        LocalDate expect = LocalDate.of(2022, 1, 31);
        PayData payData = PayData.builder()
                                .billingDate(billingDate)
                                .payAmount(payAmount)
                                .build();

        assertExpiryDate(payData, expect);
    }

    @DisplayName("윤달에 10만원 납부")
    @Test
    public void pay100000WonFeb29() {
        LocalDate billingDate = LocalDate.of(2020, 2, 29);
        int payAmount = 100_000;
        LocalDate expect = LocalDate.of(2021, 2, 28);
        PayData payData = PayData.builder()
                                .billingDate(billingDate)
                                .payAmount(payAmount)
                                .build();

        assertExpiryDate(payData, expect);
    }

    @DisplayName("10민원 넘게 납부하는 경우")
    @Test
    public void payOver100000Won() {
        LocalDate billingDate = LocalDate.of(2021, 1, 31);
        int payAmount = 130_000;
        LocalDate expect = LocalDate.of(2022, 4, 30);
        PayData payData = PayData.builder()
                                .billingDate(billingDate)
                                .payAmount(payAmount)
                                .build();

        assertExpiryDate(payData, expect);

        payAmount = 230_000;
        expect = LocalDate.of(2023, 4, 30);
        payData = PayData.builder()
                .billingDate(billingDate)
                .payAmount(payAmount)
                .build();

        assertExpiryDate(payData, expect);
    }
}
