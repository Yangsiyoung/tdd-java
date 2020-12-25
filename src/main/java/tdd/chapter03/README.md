테스트 코드 작성 순서
================
1. 쉬운 경우에서 어려운 경우로 진행
2. 예외적인 경우에서 정상인 경우로 진행

# 초반에 복잡한 테스트부터 하면 안되는 이유
초반부터 다양한 조합을 검사하는 복잡한 상황을 테스트로 추가하면 해당 테스트를 통과하기 위해  
한 번에 구현해야 할 코드가 많아진다.  

[이전 내용](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter02) 에서 다루었던 암호 검사기를 예로 들어보자.  
만약 아래 순서로 테스트를 진행했다고 가정해보자.  
* 대문자 포함 규칙만 충족하는 경우
* 모든 규칙을 충족하는 경우
* 숫자를 포함하지 않고 나머지 규칙은 충족하는 경우

## 대문자 포함 규칙만 충족하는 경우
```
@DisplayName("대문자 포함 규칙만 충족하는 경우")
    @Test
    public void meetsOnlyUppercaseCriteriaThenWeak() {
        PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
        PasswordStrength result = passwordStrengthMeter.meter("abcDef");
        assertEquals(PasswordStrength.WEAK, result);
    }
```

우선 테스트를 통과하기 위한 구현은 아래와 같을 것 이다.

* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        return PasswordStrength.WEAK;
    }
}
```

## 모든 규칙을 충족하는 경우 추가
이제 모든 규칙을 충족하는 경우를 이어서 해보자.
```
class PasswordStrengthMeterTest {

    @DisplayName("대문자 포함 규칙만 충족하는 경우")
    @Test
    public void meetsOnlyUppercaseCriteriaThenWeak() {
        PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
        PasswordStrength result = passwordStrengthMeter.meter("abcDef");
        assertEquals(PasswordStrength.WEAK, result);
    }

    @DisplayName("모든 규칙을 충족하는 경우")
    @Test
    public void meetsAllCriteriaThenStrong() {
        PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
        PasswordStrength result = passwordStrengthMeter.meter("abcDef12");
        assertEquals(PasswordStrength.STRONG, result);
    }

}
```

우선 가장 빠르게 테스트를 통과하기 위해서는 아래와 같은 코드를 작성할 것 이다.
* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if("abcDef12".equals(password)) {
            return PasswordStrength.STRONG;
        }
        return PasswordStrength.WEAK;
    }
}
```
그럼 여기서 모든 조건을 충족하는 다른 문자열에 대해서는 어떻게 검사를 할까?  
어떻게 구현을 하면 좋을지 바로 떠오르지 않기때문에 쉬운 테스트부터 진행하는 것이 좋다.  

# 구현하기 쉬운 테스트부터 시작하기
가장 구현하기 쉬운 경우부터 시작하면 빠르게 테스트를 통과할 수 있다.  
암호 검사기를 예로들어보면 아래 2가지중 하나가 쉬울 것 같다.  
* 모든 조건을 충족하는 경우
* 모든 조건을 충족하지 않는 경우
이 두가지는 모두 해당 값을 리턴해버리면 된다.  

[이전 내용](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter02) 에서도 모든 조건을 충족하는 경우를 첫번째 테스트로 시작했다.  
하나의 테스트를 통과하면 그다음 구현하기 쉬운 테스트를 선택해나가면 된다.  

# 예외 상황을 먼저 테스트해야 하는 이유
1. 예외 상황을 고려하지 않고 코드를 작성해두면 나중에 예외 상황을 처리하기 위해 코드 구조가 뒤집어지거나  
조건문을 뒤늦게 추가행하는 일이 벌어진다.  

2. TDD 를 하는 동안 예외 상황을 찾고 테스트에 반영하면 나중에 예외 상황을 처리하지 않아 발생하는 버그도 줄여준다.  

만약 암호 검사기에서 값이 없는 상황에 대한 테스트를 추가해두지 않았다면 운영에 배포되어 서비스가 제공되고나서  
NPE 가 발생할 수 있다.  

사소한 예외로 서비스가 중단되는 상황을 만들지 말자.  

# 완급 조절
다음 단계를 따라 TDD 를 익혀보자.
1. 정해진 값을 리턴
2. 값 비교를 이용해서 정해진 값을 리턴
3. 다양한 테스트를 추가하면서 구현을 **일반화**

예를들어 암호 검사기에서 길이가 8글자 미만이면서 나머지 규칙은 충족하는 상황을 위 단계를 밟아 진행해보자.  
```
@DisplayName("길이만 8글자 미만이고 나머지 조건은 충족하는 경우")
@Test
public void meetsOtherCriteriaExceptForLengthThenNormal() {
    PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
    PasswordStrength result = passwordStrengthMeter.meter("abcDef1");
    assertEquals(PasswordStrength.NORMAL, result);
}
```

딱 이테스트만 통과할만큼 코드를 작성해보자.
* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if("abcDef1".equals(password)) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
}
```

다음으로 동일한 조건의 다른 값을 추가해보자.
```
@DisplayName("길이만 8글자 미만이고 나머지 조건은 충족하는 경우")
@Test
public void meetsOtherCriteriaExceptForLengthThenNormal() {
    PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
    PasswordStrength result = passwordStrengthMeter.meter("abcDef1");
    assertEquals(PasswordStrength.NORMAL, result);
    PasswordStrength result2 = passwordStrengthMeter.meter("abcDef2");
    assertEquals(PasswordStrength.NORMAL, result2);
}
```

다시 이 테스트만을 통과하기위해 구현을 해보자.
* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if("abcDef1".equals(password) || "abcDef2".equals(password)) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
}
```

이제 상수를 이용하지말고 일반화를 해서 구현해보자.  
* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if(password.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
}
```

그리고 테스트가 통과하는지 확인하자. 잘된다. 그럼 요까지는 된것이다.  
명심하자 잘안될때는 한발 물러서서 작은 단계로 천천히 점진적으로 나아가자. 

# 리팩토링
TDD 를 진행하는 과정에서 지속적으로 리팩토링을 진행하면 코드 가독성이 높아진다.  
이는 수정요청이 들어왔을때나 확장할 때 속도를 빠르게 해준다.  
즉, 유지보수에 도움이 되는 것 이다.  

리팩토링을 통해 이해하고 변경하기 쉽게 개선함으로써 변화하는 요구사항을 적은 비용으로 반영할 수 있고  
이렇게 소프트웨어의 생존 시간을 늘려준다.  

상수를 변수로 변경하거나 변수 이름을 변경하는 것과 같이 작은 리팩토링은 바로 실행하면 되지만  
메서드 추출과 같이 구조에 영향을 주는 리팩토링을 큰 틀에서 구현 흐름이 눈에 들어오면 진행하는 것이  
좋다고 한다.  

구현 초기에는 아직 전반적인 흐름을 모르기 때문에 메서드 추출과 같은 리팩토링을 진행하면  
코드 구조를 잘못 잡을 가능성이 크고, 그렇게되면 테스트를 통과시키는 과정에서  
코드가 복잡해지거나 구현을 더는 진행하지 못하는 상황에 놓일 수 있다.  

그러니 조금 갑갑하더라도 먼저 구현 흐름과 구조가 명확해지면 그때 리팩토링을 진행하는 것이 좋다.  

# 테스트 작성 순서 연습
매달 비용을 지불해야 사용할 수 있는 유료 서비스가 있다고 하자.  
이 서비스는 아래의 규칙에 따라 서비스 만료일을 결정한다.
  
* 서비스를 이용하려면 매달 1만원을 선불로 납부한다. 납부일 기준으로 한달 뒤가 서비스 만료일이다.  
* 2개월 이상 요금을 납부할 수 있다.  
* 10 만원을 납부하면 서비스를 1년 제공한다.  

## 먼저 테스트 클래스 이름을 정하자.
```
public class ExpiryDateCalculatorTest {
}
```

## 쉬운 것부터 테스트
아래 두 가지를 고려해야 한다.  
1. 구현하기 쉬운 것부터 테스트
2. 예외 상황을 먼저 테스트

우선 납부일 기준으로 한달 뒤 같은 날을 만료일료 계산하는 것이 가장 쉬워보인다.  

```
public class ExpiryDateCalculatorTest {

    @DisplayName("만원 납부하면 한달 뒤가 만료일이 됨")
    @Test
    public void pay10000Won() {
        LocalDate billingDate = LocalDate.of(2020, 12, 20);
        LocalDate expect = LocalDate.of(2021, 1, 20);

        int payAmount = 10000;

        ExpiryDateCalculator expiryDateCalculator = new ExpiryDateCalculator();
        LocalDate result = expiryDateCalculator.calculate(billingDate, payAmount);
        assertEquals(expect, result);
    }
}
```

* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(LocalDate date, int payAmount) {
        return LocalDate.of(2021, 1, 20);
    }
}
```

## 예를 추가하면서 구현을 일반화  
```
public class ExpiryDateCalculatorTest {

    @DisplayName("만원 납부하면 한달 뒤가 만료일이 됨")
    @Test
    public void pay10000Won() {
        LocalDate billingDate = LocalDate.of(2020, 12, 20);
        LocalDate expect = LocalDate.of(2021, 1, 20);

        int payAmount = 10000;

        ExpiryDateCalculator expiryDateCalculator = new ExpiryDateCalculator();
        LocalDate result = expiryDateCalculator.calculate(billingDate, payAmount);
        assertEquals(expect, result);

        LocalDate billingDate2 = LocalDate.of(2021, 1, 20);
        LocalDate expect2 = LocalDate.of(2021, 2, 20);

        ExpiryDateCalculator expiryDateCalculator2 = new ExpiryDateCalculator();
        LocalDate result2 = expiryDateCalculator2.calculate(billingDate2, payAmount);
        assertEquals(expect2, result2);
    }
}
```

* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(LocalDate date, int payAmount) {
        return date.plusMonths(1);
    }
}
```

이후 테스트가 통과하는 것을 확인한다.  

## 예외 상황 처리
예를들어 2021-01-31 에 납부액 1만원이면 만료일은 2021-02-28 이다.  
또한 2021-05-31 에 납부액 1만원이면 만료일은 2021-06-30 이다.  
즉, 월마다 다른 날짜 수 때문에 납부일 기준으로 다음 달의 같은 날이 만료일이 되지 않는 경우도 테스트로 추가해야 한다.  

```
@DisplayName("납부일과 한달 뒤 일자가 같지 않음")
    @Test
    public void notSameDayPay10000Won() {
        LocalDate billingDate = LocalDate.of(2021, 1, 30);
        LocalDate expect = LocalDate.of(2021, 2, 28);
        int payAmount = 10000;
        ExpiryDateCalculator expiryDateCalculator = new ExpiryDateCalculator();
        LocalDate result = expiryDateCalculator.calculate(billingDate, payAmount);
        assertEquals(expect, result);
    }
```

이 테스트는 바로 통과가 되는데 LocalDate.plusMonths() 메서드가 알아서 처리를 해주어서 그렇다.  

## 다시 예외 상황
다음 테스트를 고를 시간이다 쉽거나 예외상황을 고르면 된다.  
일단 쉬운 예를 생각해보자.  
* 2만원 지불하면 만료일은 2달 뒤
* 3만원 지불하면 만료일을 3달 뒤

그리고 예외 상황을 생각해보자.
* 첫 납부일이 2021-01-31 이고 만료되는 2021-02-28 에 1만원 납부하면 다음 만료일은 2021-03-31 이다.
* 첫 납부일이 2021-01-30 이고 만료되는 2021-02-28 에 1만원 납부하면 다음 만료일은 2021-03-30 이다.
* 첫 납부일이 2021-05-31 이고 만료되는 2021-06-30 에 1만원 납부하면 다음 만료일은 2021-07-31 이다.

2만원 지불하고 2달 뒤를 하는게 쉬워보이는데...  
하지만 1만원 지불하는거를 구현하고 있었으니까 1만원 지불에 대한 예외상황을 마무리하자.  

납부일과 납부액만 있었는데 이제 첫 납부일을 생각해야한다.  
그래야 위의 예외 상황에서 첫 납부일 기준으로 만료일을 계산할 수 있을테니까.  

## 다음 테스트 추가전에 리팩토링
납부일, 납부액 -> 첫 납부일, 납부일, 납부액 요렇게 3개로 바뀌었다.  
그러니 아래의 내용을 고민해보자.  
* CalculateExpiryDate 메서드의 첫 납부일 파라미터 추가
* 첫 납부일, 납부일, 납부액을 담은 객체를 CalculateExpiryDate 메서드에 전달  

파라미터 개수가 많아지면 안좋으니까 2번째인 객체로 전달하자.  
그럼 우선 파라미터를 객체로 바꿔서 전달하는 리팩토링하고 테스트 추가합시다.  

* PayData.java
```
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayData {
    private LocalDate billingDate;
    private int payAmount;
}
```

* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        return payData.getBillingDate().plusMonths(1);
    }
}
```

```
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
}
```

리팩토링의 경우 파라미터를 객체로 바꿨으며, Lombok 을 사용하여 빌더도 추가했고, 코드 수를 좀 줄였다.  
그리고 중복되던 단언문을 메서드 추출 기법으로 리팩토링 하였다.  

## 예외 상황 테스트 진행 계속
앞서 말했던 아래의 케이스에 대한 테스트를 작성해보자.  
* 첫 납부일이 2021-01-31 이고 만료되는 2021-02-28 에 1만원 납부하면 다음 만료일은 2021-03-31 이다.

```
@DisplayName("첫 납부일과 만료일자가 다를 때 만원 납부")
@Test
public void notSameDayPayAgain10000Won() {
    LocalDate firstBillingDate = LocalDate.of(2021, 1, 31);
    LocalDate billingDate = LocalDate.of(2021, 2, 28);
    LocalDate expect = LocalDate.of(2021, 3, 31);
    int payAmount = 10000;
    PayData payData = PayData.builder().payAmount(payAmount).firstBillingDate(firstBillingDate).billingDate(billingDate).build();
    assertExpiryDate(payData, expect);
}
```

그런데 빌더에서 firstBillingDate(firstBillingDate) 는 구현해두지 않아서 에러가 날 것이다.  
그래서 이제 구현해주면된다.  

* PayData.java
```
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayData {
    private LocalDate firstBillingDate;
    private LocalDate billingDate;
    private int payAmount;
}
``` 

요로코롬!!!  

이제 상수를 이용하여 테스트를 통과시켜보자.  

* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        if(payData.getFirstBillingDate() != null && payData.getFirstBillingDate().equals(LocalDate.of(2021, 1, 31))) {
            return LocalDate.of(2021, 3, 31);
        }
        return payData.getBillingDate().plusMonths(1);
    }
}
```

이제 다음 케이스에 대해 테스트를 작성해보자.  
* 첫 납부일이 2021-01-30 이고 만료되는 2021-02-28 에 1만원 납부하면 다음 만료일은 2021-03-30 이다.

```
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
```

이제 상수로는 하기 힘들어졌으니 **테스트를 통과할 만큼만 일반화** 해보자.

나는 처음에 아래와 같이 작성했다.  
* ExpiryDateCalculator.java (내가 생각했던 버전)
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        if(payData.getFirstBillingDate() != null) {
            if(!payData.getFirstBillingDate().equals(payData.getBillingDate())) {
                return payData.getFirstBillingDate().plusMonths(2);
            }
        }

        return payData.getBillingDate().plusMonths(1);
    }
}
```
하지만 책에서는 아래와 같이 코드가 있었고, 아래의 코드가 좀 더 로직이 명확한 것 같다.  

* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        LocalDate candidateExpiryDate = payData.getBillingDate().plusMonths(1);
        if(payData.getFirstBillingDate() != null) {
            // 첫 납부일의 일자와 현재 납부일 + 1달의 일자가 다르면
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExpiryDate.getDayOfMonth()) {
                // 현재 납부일 + 1달의 달 에다가 첫 납부일의 일자로 해서 리턴(첫달 납부 일자의 규칙을 따라감)
                return candidateExpiryDate.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }

        return payData.getBillingDate().plusMonths(1);
    }
}
```

## 코드 정리 : 상수를 변수로
달을 추가하는 과정에서 숫자 1을 사용했다, 그래서 1을 변수로 바꿨다.  

* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int addMonth = 1;
        LocalDate candidateExpiryDate = payData.getBillingDate().plusMonths(addMonth);
        if(payData.getFirstBillingDate() != null) {
            // 첫 납부일의 일자와 현재 납부일 + 1달의 일자가 다르면
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExpiryDate.getDayOfMonth()) {
                // 현재 납부일 + 1달의 달 에다가 첫 납부일의 일자로 해서 리턴(첫달 납부 일자의 규칙을 따라감)
                return candidateExpiryDate.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }

        return payData.getBillingDate().plusMonths(addMonth);
    }
}
```

## 다음 테스트 선택 : 쉬운 테스트
아래 두가지 중에서 다음 테스트를 선택하자.  
* 2만원을 지불하면 만료일이 두달뒤가 된다.  
* 3만원을 지불하면 만료일이 세달뒤가 된다.  

지불한 금액이 곧 추가할 개월 수에 비례하기때문에 계산하기 쉬우니까  
2만원부터 진행하자.  

```
@DisplayName("2만원 이상 납부하면 비례해서 만료일 계산")
@Test
public void calculateInProportionToWon() {
    LocalDate billingDate = LocalDate.of(2021, 1, 31);
    LocalDate expect = LocalDate.of(2021, 3, 31);
    int payAmount = 20000;
    PayData payData = PayData.builder().billingDate(billingDate).payAmount(payAmount).build();
    assertExpiryDate(payData, expect);
}
```

테스트를 통과하기 위해 구현하는데 구현할게 명확하므로 아래와같이 바로 구현했다.  
* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int addMonth = payData.getPayAmount() / 10000;
        LocalDate candidateExpiryDate = payData.getBillingDate().plusMonths(addMonth);
        if(payData.getFirstBillingDate() != null) {
            // 첫 납부일의 일자와 현재 납부일 + 1달의 일자가 다르면
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExpiryDate.getDayOfMonth()) {
                // 현재 납부일 + 1달의 달 에다가 첫 납부일의 일자로 해서 리턴(첫달 납부 일자의 규칙을 따라감)
                return candidateExpiryDate.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }

        return payData.getBillingDate().plusMonths(addMonth);
    }
}
```

다른 테스트도 통과하는지 3만원으로 진행해보자.  
```
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
```

잘통과한다. 이제 예외 상황에 대한 테스트를 추가해보자.
  
## 예외 상황 테스트 추가

첫 납부일과 납부일자가 다를때 2만원 이상 납부한 경우를 테스트해보자.  
* 첫 납부일이 2021-01-31 이고 만료되는 2021-02-28 에 2만원을 납부하면 다음 만료일은 2021-04-30 이다.  

```
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
}
```
그리고 아래와 같은 에러를 만나게 된다.  
java.time.DateTimeException: Invalid date 'APRIL 31'  

그래서 이와같은 현상을 해결하기 위해 나는 아래와 같이 코드를 작성했었다.  
* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int addMonth = payData.getPayAmount() / 10000;

        LocalDate candidateExpiryDate = payData.getBillingDate().plusMonths(addMonth);
        if(payData.getFirstBillingDate() != null) {
            // 첫 납부일의 일자와 현재 납부일 + 1달의 일자가 다르면
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExpiryDate.getDayOfMonth()) {
                /*
                 * 현재 납부일 + 1달의 달 에다가 첫 납부일의 일자로 해서 리턴(첫달 납부 일자의 규칙을 따라감)
                 * 하지만 첫달에는 31일이 있는데 납부일 + 1달에는 31일이 없는 경우도 생각해야하기에
                 * 아래의 조건문을 우선 실행
                 */
                if(YearMonth.from(candidateExpiryDate).isValidDay(payData.getFirstBillingDate().getDayOfMonth())) {
                    return candidateExpiryDate.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
                }
                return YearMonth.from(candidateExpiryDate).atEndOfMonth();
                
            }
        }
        return payData.getBillingDate().plusMonths(addMonth);
    }
}
```

그리고 책의 경우 아래와 같이 작성했다.  
* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int addMonth = payData.getPayAmount() / 10000;

        LocalDate candidateExpiryDate = payData.getBillingDate().plusMonths(addMonth);
        if(payData.getFirstBillingDate() != null) {
            // 첫 납부일의 일자와 현재 납부일 + 1달의 일자가 다르면
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExpiryDate.getDayOfMonth()) {
                /*
                 * 현재 납부일 + 1달의 달 에다가 첫 납부일의 일자로 해서 리턴(첫달 납부 일자의 규칙을 따라감)
                 * 하지만 첫달에는 31일이 있는데 납부일 + 1달에는 31일이 없는 경우도 생각해야하기에
                 * 아래의 조건문을 우선 실행
                 */
                if(YearMonth.from(candidateExpiryDate).lengthOfMonth() < payData.getFirstBillingDate().getDayOfMonth()) {
                    return candidateExpiryDate.withDayOfMonth(YearMonth.from(candidateExpiryDate).lengthOfMonth());
                }
                return candidateExpiryDate.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(addMonth);
    }
}
```

이제 같은 로직의 테스트를 좀 더 추가해보자.
```
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
```

모든 테스트를 통과하는 것을 확인 할 수 있다.  

## 코드 정리
* ExpiryDateCalculator.java 
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int addMonth = payData.getPayAmount() / 10000;

        LocalDate candidateExpiryDate = payData.getBillingDate().plusMonths(addMonth);
        if(payData.getFirstBillingDate() != null) {
            // 첫 납부일의 일자와 현재 납부일 + 1달의 일자가 다르면
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExpiryDate.getDayOfMonth()) {
                /*
                 * 현재 납부일 + 1달의 달 에다가 첫 납부일의 일자로 해서 리턴(첫달 납부 일자의 규칙을 따라감)
                 * 하지만 첫달에는 31일이 있는데 납부일 + 1달에는 31일이 없는 경우도 생각해야하기에
                 * 아래의 조건문을 우선 실행
                 */
                if(YearMonth.from(candidateExpiryDate).lengthOfMonth() < payData.getFirstBillingDate().getDayOfMonth()) {
                    return candidateExpiryDate.withDayOfMonth(YearMonth.from(candidateExpiryDate).lengthOfMonth());
                }
                return candidateExpiryDate.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(addMonth);
    }
}
```

우선 많은 중복이 눈에 들어오는데 후보 만료일이 속한 월의 마지막 일자를 구하는 코드의 중복을 없애보자.  

```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int addMonth = payData.getPayAmount() / 10000;

        LocalDate candidateExpiryDate = payData.getBillingDate().plusMonths(addMonth);
        if(payData.getFirstBillingDate() != null) {
            // 첫 납부일의 일자와 현재 납부일 + 1달의 일자가 다르면
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExpiryDate.getDayOfMonth()) {
                /*
                 * 현재 납부일 + 1달의 달 에다가 첫 납부일의 일자로 해서 리턴(첫달 납부 일자의 규칙을 따라감)
                 * 하지만 첫달에는 31일이 있는데 납부일 + 1달에는 31일이 없는 경우도 생각해야하기에
                 * 아래의 조건문을 우선 실행
                 */
                final int dayLengthOfCandidateExpiryDate = YearMonth.from(candidateExpiryDate).lengthOfMonth();
                if(dayLengthOfCandidateExpiryDate < payData.getFirstBillingDate().getDayOfMonth()) {
                    return candidateExpiryDate.withDayOfMonth(dayLengthOfCandidateExpiryDate);
                }
                return candidateExpiryDate.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(addMonth);
    }
}
```

그리고 첫 납부일의 일자를 구하는 코드의 중복도 없애보자.  
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int addMonth = payData.getPayAmount() / 10000;

        LocalDate candidateExpiryDate = payData.getBillingDate().plusMonths(addMonth);
        if(payData.getFirstBillingDate() != null) {
            // 첫 납부일의 일자와 현재 납부일 + 1달의 일자가 다르면
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExpiryDate.getDayOfMonth()) {
                /*
                 * 현재 납부일 + 1달의 달 에다가 첫 납부일의 일자로 해서 리턴(첫달 납부 일자의 규칙을 따라감)
                 * 하지만 첫달에는 31일이 있는데 납부일 + 1달에는 31일이 없는 경우도 생각해야하기에
                 * 아래의 조건문을 우선 실행
                 */
                final int dayLengthOfCandidateExpiryDate = YearMonth.from(candidateExpiryDate).lengthOfMonth();
                final int dayLengthOfFirstBillingDate = payData.getFirstBillingDate().getDayOfMonth();
                if(dayLengthOfCandidateExpiryDate < dayLengthOfFirstBillingDate) {
                    return candidateExpiryDate.withDayOfMonth(dayLengthOfCandidateExpiryDate);
                }
                return candidateExpiryDate.withDayOfMonth(dayLengthOfFirstBillingDate);
            }
        }
        return payData.getBillingDate().plusMonths(addMonth);
    }
}
```

테스트를 전체적으로 다시 돌려보고 이상이 없다면 가독성도 높여보자.  
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int addMonth = payData.getPayAmount() / 10000;
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
}
```

테스트가 모두 통과하는 것을 확인하고 다음 테스트로 넘어가자.  

## 다음 테스트 : 10개월 요금을 납부하면 1년 제공
이제 10만원을 납부하면 서비스를 1년 제공하는 규칙을 구현하자.  
해당 규칙 관련 테스트를 작성해보자.  

```
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
```

이제 테스트를 통과하도록 구현해보자.  
* ExpiryDateCalculator.java
```
public class ExpiryDateCalculator {
    public LocalDate calculate(PayData payData) {
        int payAmount = payData.getPayAmount();
        int addMonth = payAmount == 100000 ? 12 : payAmount / 10000;
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
}
```

이제 윤달의 마지막 날에 10만원을 납부하는 케이스 혹은 13만원을 납부하는 케이스에 대해서도 생각을 해봐야한다.  
13만원의 경우 1년 3개월 뒤가 만료일이 되어야 한다. 

먼저 윤달의 마지막 날에 10만원을 납부하는 케이스를 생각해보자.  

```
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
```

테스트 결과 LocalDate 의 plusMonths 라이브러리가 알아서 잘 계산해줘서 바로 통과할 수 있었다.  

이제 13만원을 납부하는 케이스에 대해서 생각을 해보자.  

```
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
    }
```

이 테스트를 통과시키기위해 구현해보자.  
* ExpiryDateCalculator.java
```
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
```

일반화도 같이 진행했으니 23만원을 납부했을 경우 2년 + 3개월이 되는지 테스트

```
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
```
잘 통과한다.  
이제 유료 서비스 납부에 대한 예제를 끝내도록 하겠다.  

## 테스트할 목록 정하기
TDD 를 시작할 때 테스트할 목록을 미리 정리하면 좋다고 한다.  
테스트할 목록을 정리하고 어떤 테스트가 쉬울지 상상해보고 어떤 테스트가 예외적인지 생각해본다.  

테스트 과정에서 새로운 테스트 사례를 발견하면 그 사례를 목록에 추가해서 놓치지 않도록하자.  
처음부터 모든 케이스를 정리하려면 시간이 오래걸리기도하고 쉽지않으니까  
테스트 과정에서 새롭게 발겨되면 그때그때 추가하자.  

테스트를 한번에 다 작성하지말고, 하나씩 진행해보자 그러면서 점진적으로 리팩토링 할 수 있는 구조를 만들자.  
하나의 테스트 코드를 만들고 통과시키고 리팩토링하고, 다시 테스트 코드를 만들고하는 과정은  
짧은 리듬을 반복하기 때문에 다루는 범위가 작고 개발 주기도 짧아서 개발 집중력도 높아진다고 한다.  

리팩토링 범위가 넓은 큰 리팩토링을 발견할 때가 있을 것 이다.  
시간이 오래걸리기 때문에 TDD 흐름이 깨지기 쉬울것이라고 한다.  
이럴땐 테스트를 통과시키는데 집중을 하고 대신 범위가 큰 리팩토링을 다음 할 일 목록에 추가해서  
놓치지않고 진행하는것도 좋은 방법이라고 한다.  

## 시작이 안 될 때는 단언부터 고민
테스트 코드를 작성하다보면 시작이 안될때가 있다.  
이럴떈 검증하는 코드부터 작성하기 시작하면 도움이 된다고 한다.  
(assert 부터 작성해보면 도움이 될 것이라는 뜻 같다.)

## 구현이 막히면  
TDD 를 진행하다 구현이 막힐땐 과감하게 코드를 지우고 미련없이 다시 시작하라고한다.  
그리고 다시 진행할 때 아래의 내용을 상기해본다.  
* 쉬운 테스트, 예외적인 테스트
* 완급 조절