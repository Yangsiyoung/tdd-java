Refactoring Technique
======================

# 1. Extract Function
코드에서 메서드를 추출하는 것  
메서드의 목적과 구현을 분리  
메서드를 짧게 작성하도록 하는 결과를 가져옴  
단, 코드를 메서드로 빼낼 때 해당 메서드가 의미있는 이름을 가지도록 하는 것이 포인트  

## 절차

1. 메서드를 새로 만들고, 메서드의 목적을 잘 나타내는 이름을 붙인다.  
   (무엇을 하는지가 잘 드러나야 한다.)  
   
2. 추출할 코드를 원본 메서드에서 복사하여 새로운 메서드에 붙여넣는다.  

3. 추출한 코드 중 유효범위를 벗어나는 변수가 있다면, 매개변수를 활용한다.  

4. 컴파일 해본다.  

5. 원본 메서드에서 새로 만든 메서드를 호출한다.  

6. 테스트 한다.  

7. 다른 코드에 똑같거나 비슷한 코드가 있는지 보고, 있다면 방금 새로 만든 메서드를 활용할지 결정해본다.  

## 예시  

* 원본 메서드
```
public void printOwing(String customer, int[] ordersAmount) {
    int outstanding = 0;

    System.out.println("**************");
    System.out.println("**** 고객 채무 ****");
    System.out.println("**************");

    // 미 해결 채무를 계산
    for (int orderAmount : ordersAmount) {
        outstanding += orderAmount;
    }

    LocalDate dueDate = LocalDate.now().plusDays(30);

    System.out.println("customer = " + customer);
    System.out.println("outstanding = " + outstanding);
    System.out.println("dueDate = " + dueDate);
}
```

* 유효 범위를 벗어나는 변수가 없는 경우  
```
public void printOwing(String customer, int[] ordersAmount) {
    int outstanding = 0;

    printBanner(); // 배너 출력 로직을 함수로 추출

    // 미 해결 채무를 계산
    for (int orderAmount : ordersAmount) {
        outstanding += orderAmount;
    }

    LocalDate dueDate = LocalDate.now().plusDays(30);

    System.out.println("customer = " + customer);
    System.out.println("outstanding = " + outstanding);
    System.out.println("dueDate = " + dueDate);
}

private void printBanner() {
    System.out.println("**************");
    System.out.println("**** 고객 채무 ****");
    System.out.println("**************");
}
```

* 지역변수를 사용할 때  
```
public void printOwing(String customer, int[] ordersAmount) {
    int outstanding = 0;

    printBanner(); // 배너 출력 로직을 함수로 추출

    // 미 해결 채무를 계산
    for (int orderAmount : ordersAmount) {
        outstanding += orderAmount;
    }

    LocalDate dueDate = LocalDate.now().plusDays(30);

    printDetails(customer, outstanding, dueDate); // 지역 변수를 매개변수로 전달
}

private void printBanner() {
    System.out.println("**************");
    System.out.println("**** 고객 채무 ****");
    System.out.println("**************");
}

private void printDetails(String customer, int outstanding, LocalDate dueDate) {
    System.out.println("customer = " + customer);
    System.out.println("outstanding = " + outstanding);
    System.out.println("dueDate = " + dueDate);
}
```

* 지역 변수의 값을 변경할 때  
```
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
```
추가적으로 나는 printOwing 메서드에서 호출하는 메서드를 순서대로 아래에 배치했다.  

# 2. Inline Function  
메서드 본문이 명확해서, 메서드를 제거하고 해당 메서드의 본문을 그대로 사용하는 것  

## 절차  

1. 다형 메서드인지 확인한다.  
   (서브 클래스에서 오버라이드하는 메서드면 안되니까.)  

2. 인라인할 메서드를 호출하는 곳을 모두 찾는다.  

3. 각 호출부분을 메서드 본문으로 교체한다.  

4. 하나씩 교체하며 테스트한다.  

5. 기존 메서드를 제거한다.  

## 예시  

* 원본 메서드
```
public int rating(int numberOfLateDeliveries) {
    return moreThanFiveLateDeliveries(numberOfLateDeliveries) ? 2 : 1;
}

private boolean moreThanFiveLateDeliveries(int numberOfLateDeliveries) {
    return numberOfLateDeliveries > 5;
}
```

* 함수 인라인 후
```
public int rating(int numberOfLateDeliveries) {
    return numberOfLateDeliveries > 5 ? 2 : 1;
}
```

개인적으로 원본 메서드도 불필요하게 내부에서 메서드를 호출하는 것 처럼 보이지만,  
내부 메서드를 호출해서 하는 방식도 괜찮아 보이긴한다...  
내가 보기엔 간단한 코드라도 조금 더 명확한 이름인것같긴하기때문...  

# 3. Extract Variable  
표현식이 복잡해서 이해하기 어려울 때, 지역 변수를 활용해서 해결  
해당 메서드 내에서만 사용한다면 지역 변수로 하지만, 메서드를 벗어나 넓은 범위에서 사용되면  
메서드로 추출하는 것을 고려해 봐야함

## 절차  

1. 불변 변수를 선언하고, 이름을 붙일 표현식을 대입한다.  

2. 원본 표현식을 새로 만든 변수로 교체한다.  

3. 테스트한다.  

4. 표현식을 여러 곳에서 사용한다면, 새로 만든 변수로 교체한다.  
   (교체할 때마다 테스트를 해야한다.)  

## 예시

* 원본 메서드
```
public double price(int quantity, int itemPrice) {
    // 가격 = 기본 가격 - 수량 할인 + 배송비
    return (quantity * itemPrice)
            - (Math.max(0, quantity - 500) * itemPrice * 0.05)
            + (Math.min(quantity * itemPrice * 0.1, 100));
}
```

* 변수 추출 후
```
public double price(int quantity, int itemPrice) {
    // 가격 = 기본 가격 - 수량 할인 + 배송비
    int basePrice = quantity * itemPrice;
    double quantityDiscount = Math.max(0, quantity - 500) * itemPrice * 0.05;
    double shipping = Math.min(quantity * itemPrice * 0.1, 100);
    return basePrice
            - quantityDiscount
            + shipping;
}
```

# 4. Inline Variable