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
