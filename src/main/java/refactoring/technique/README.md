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
Extract Variable 을 통해 표현식을 변수에 할당 해 표현식에 대한 이름으로 사용하고 대부분 긍정적인 효과이지만,  
표현식과 이름이 별차이 없을 때가 있다. 이럴 때 인라인하는 것이 좋다.

## 절차  
1. 변수로 된 표현식을, 원본 표현식으로 바꾼다.  
  
2. 테스트 한다. 

3. 변수를 사용하는 모든 부분을 교체할 때 까지 반복한다.  

4. 변수 선언문과 대입문을 지운다.  

5. 테스트 한다.  

## 예시  

* 원본 변수 및 표현식  
```
public boolean basePriceOverThan1000(price) {
    int basePrice = price;
    return (basePrice > 1000)
}
```

* 변수 인라인 후  
```
public boolean basePriceOverThan1000(price) {
    return (basePrice > 1000)
}
```

# 5. Change Function Declaration  
메서드는 프로그램을 작은 부분으로 나누는 수단.  
중요한 것은 메서드 이름, 이름이 잘 생각 나지않는다면 주석으로 메서드의 목적을 설명해보다보면  
괜찮은 이름이 떠오르기도 한다.  

매개변수는 메서드가 외부 세계와 어우러지는 방식을 정의  

즉 이 테크닉은 메서드 명 혹은 메서드 매개변수를 바꾸는 작업이다.  

## 절차
메서드 선언과 호출문을 단번에 고칠 수 있다면 간단한 절차를,  
호출하는 곳이 많거나, 호출 과정이 복잡하거나, 호출 대상이 다형 메서드거나, 선언을 복잡하게 변경할 때는  
마이그레이션 절차를 따르자.  

### 간단한 절차
1. 매개변수를 제거하고자한다면, 메서드 내에서 매개변수를 참조한는 곳이 없는지 확인
  
2. 메서드 선언을 원하는 형태로 변경  

3. 기존 메서드를 참조하는 부분을 모두 찾아서 바뀐 형태로 수정  

4. 테스트 한다.  

### 마이그레이션 절차  
1. 메서드 본문을 적절하게 리팩토링 한다.  

2. 메서드 본문을 새로운 메서드로 추출한다.  

3. 추출한 메서드에 매개변수를 추가해야한다면, '간단한 절차' 를 활용한다.  

4. 테스트 한다.  

5. 기존 함수를 인라인 한다.  

6. 이름을 적절하게 정의한다.  

7. 테스트 한다.  

## 예시  

### 함수 이름 바꾸기 (간단한 절차)  
* 원본 메서드  
```
public double circum(int radius) {
    return 2 * Math.PI * radius;
}
```

* 변경 후  
```
public double circumference(int radius) {
    return 2 * Math.PI * radius;
}
```  

### 함수 이름 바꾸기 (마이그레이션 절차)  
* 원본 메서드  
```
public double circum(int radius) {
    return 2 * Math.PI * radius;
}
```

* 변경 후  
```
public double circum(int radius) {
    return circumference(radius);
}

public double circumference(int radius) {
    return 2 * Math.PI * radius;
}
```

이렇게 변경하고 기존 circum 메서드를 사용하던 곳을 circumference 메서드로 바꾼뒤  
테스트 하고 이상이 없으면 circum 과 circumference 의 기능이 동일하기에  
circum 메서드가 폐기 예정(deprecated) 임을 알린다.  
그리고 나중에 client 들이  circumference 메서드로 갈아타면  
circum 메서드를 제거한다.  

### 매개변수 추가하기  

* 원본 메서드  
```
public void addReservation(String customer) {
    reservations.add(customer);
}
```

여기서 예약 시 우선순위 큐를 지원하라는 새로운 요구 추가
```
public void addReservation(String customer) {
    addReservationWithPriority(customer, false);
}

public void addReservationWithPriority(String customer, boolean priority) {
    reservations.add(customer);
}
```

### 매개변수를 속성으로 바꾸기  
고객이 뉴잉글랜드에 살고있는지 확인하는 메서드가 있다고 해보자.  

* 기존 메서드
```
public boolean isNewEngland(ChangeFunctionDeclarationCustomer customer) {
    return Stream.builder().add("MA").add("CT").add("ME").add("VT").add("NH").add("RI").build().anyMatch((state) -> state.equals(customer.getState()));
}
```

고객에 대한 의존성을 제거하고, 거주하는 주에 대한 정보만 받으면 더 넓은 문맥에서 사용가능  

메서드 매개변수 타입 변경 + 프로퍼트로 변경
```

private List<ChangeFunctionDeclarationCustomer> newEnglanders =  customers.stream().filter((customer) -> isNewEngland(customer.getState())).collect(Collectors.toList());

public boolean isNewEngland(String stateCode) {
    return Stream.builder().add("MA").add("CT").add("ME").add("VT").add("NH").add("RI").build().anyMatch((state) -> state.equals(stateCode));
}
```

지금까지하면서 든 생각은 알아보기 쉽게하는 것이 목적인거같다 그리고 알아보기 쉽게하기 위한  
기법들을 알려주고 있는 듯 하다.  

