package tdd.chapter03;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int payAmount = payData.getPayAmount();
        int addMonth = calculateAddMonth(payAmount);
        if(payData.getFirstBillingDate() != null) {
            return expiryDateUsingFirstBillingDate(payData, addMonth);
        }
        return payData.getBillingDate().plusMonths(addMonth);
    }


    private LocalDate expiryDateUsingFirstBillingDate(PayData payData, int addMonth) {
        // 첫 납부일의 일자와 현재 납부일 + 1달의 일자가 다르면
        LocalDate candidateExpiryDate = payData.getBillingDate().plusMonths(addMonth);
        if(isSameDayOfMonth(payData.getFirstBillingDate(), candidateExpiryDate)) {
            /*
             * 현재 납부일 + 1달의 달 에다가 첫 납부일의 일자로 해서 리턴(첫달 납부 일자의 규칙을 따라감)
             * 하지만 첫달에는 31일이 있는데 납부일 + 1달에는 31일이 없는 경우도 생각해야하기에
             * 아래의 조건문을 우선 실행
             */
            final int dayLengthOfCandidateExpiryDate = lastDayOfMonth(candidateExpiryDate);
            final int dayLengthOfFirstBillingDate = payData.getFirstBillingDate().getDayOfMonth();
            if(dayLengthOfCandidateExpiryDate < dayLengthOfFirstBillingDate) {
                return candidateExpiryDate.withDayOfMonth(dayLengthOfCandidateExpiryDate);
            }
            return candidateExpiryDate.withDayOfMonth(dayLengthOfFirstBillingDate);
        }
        return candidateExpiryDate;
    }

    private boolean isSameDayOfMonth(LocalDate firstLocalDate, LocalDate secondLocalDate) {
        return firstLocalDate.getDayOfMonth() != secondLocalDate.getDayOfMonth();
    }

    private int lastDayOfMonth(LocalDate localDate) {
        return YearMonth.from(localDate).lengthOfMonth();
    }

    private int calculateAddMonth(int payAmount) {
        int addMonth = payAmount / 10000;
        if(payAmount >= 100000) {
            // 10만원 마다 1년이니까 그리고 23만원이면 2년 + 3개월이니까
            addMonth = 12 * (payAmount / 100000) + ((payAmount % 100000) / 10000);
        }
        return addMonth;
    }
}
