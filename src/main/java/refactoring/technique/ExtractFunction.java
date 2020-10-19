package refactoring.technique;

import java.time.LocalDate;

public class ExtractFunction {

    public void printOwing(String customer, int[] ordersAmount) {
        printBanner(); // 배너 출력 로직을 함수로 추출
        // 미 해결 채무를 계산
        int outstanding = calculateOutstanding(ordersAmount); // 추출한 함수가 반환한 값을 원래 변수에 저장
        LocalDate dueDate = calculateDuDate(); // 추출한 함수가 반환한 값을 원래 변수에 저장
        printDetails(customer, outstanding, dueDate); // 지역 변수를 매개변수로 전달
    }

    private void printBanner() {
        System.out.println("**************");
        System.out.println("**** 고객 채무 ****");
        System.out.println("**************");
    }

    private int calculateOutstanding(int[] ordersAmount) {
        int outstanding = 0; // 맨 위에 있던 선언문을 이 위치로 이동
        for (int orderAmount : ordersAmount) {
            outstanding += orderAmount;
        }
        return outstanding;
    }

    private LocalDate calculateDuDate() {
        return LocalDate.now().plusDays(30);
    }

    private void printDetails(String customer, int outstanding, LocalDate dueDate) {
        System.out.println("customer = " + customer);
        System.out.println("outstanding = " + outstanding);
        System.out.println("dueDate = " + dueDate);
    }
}
