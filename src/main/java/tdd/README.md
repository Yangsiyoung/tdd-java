TDD
=========
최범균님의 '테스트 주도 개발 시작하기' 를 읽으며 정리해보자  

# TDD 란
테스트부터 시작한다.  
구현을 먼저하고 나중에 테스트하는 방식이 아니라, 테스트를 하고 구현을 시작한다.  
기능을 검증하는 테스트 코드를 먼저 작성하고 테스트 코드를 통과시키기 위해 개발을 진행  

# 예시 (계산기)
우선 덧셈 기능을 가지고있는 계산기를 만든다고 가정해보고 TDD 를 시작해보자.  

* CalculatorTest.java
```
public class CalculatorTest {

    @Test // Junit 에서 @Test 어노테이션이 붙어 있어야 테스트 메서드로 인식
    void plus() {
        int result = Calculator.plus(1, 2);
        assertEquals(3, result); // assertEquals() : 기대한 값과 실제 값이 동일한지 비교
    }
    
}
```

먼저 테스트코드를 짜면 Calculator 라는 객체가 없다며 컴파일 에러가 보인다.  
컴파일 에러를 잡기 위해 Calculator 라는 클래스를 만들면 plus 라는 메서드가 없다고 컴파일 에러가 보인다.  
그래서 Calculator 클래스의 plus 메서드까지 만들고 나면 비로소 컴파일 에러는 피할 수 있고  
그대로 실행을하면 기대한 값과 실제 값이 동일하지 않아 테스트가 실패한다.  

따라서 num1 + num2 를 리턴하는 메서드를 완성하면 아래와 같은 결과물이 나온다.  

* Calculator.java
```
public class Calculator {
    public static int plus(int num1, int num2) {
        return num1 + num2;
    }
}
```

# 예시 (암호 검사기)
문자열을 검사해서 규칙을 준수하는지에 따라 암호의 강도를 약함, 보통, 강함으로 구분한다.  
다음의 규칙을 이용해서 암호를 검사할 것 이다.  

## 검사 규칙
1. 길이가 8글자 이상
2. 0부터 9 사이의 숫자를 포함
3. 대문자 포함

* 세 규칙을 모두 충족하면 강함
* 2개의 규칙을 충족하면 보통
* 1개 이하의 규칙을 충족하면 약함

## Test 먼저 작성
우선 가장 쉬운 케이스를 골라서 테스트를 통과시켜보자.  
세 규칙을 모두 충족하는 강함 수준의 비밀번호를 나타내는 테스트 코드를 짜보자.  

### 세 규칙을 모두 충족하는 경우
```
@DisplayName("패스워드가 조건을 모두 충족하면 암호 강도가 강함")
@Test
public void meetsAllCriteriaThenStrongPassword() {
    String password = "abcdABCD1234";
    PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
    PasswordStrength result = passwordStrengthMeter.meter(password);
    assertEquals(PasswordStrength.STRONG, result);
}
```

* PasswordStrength.java
```
public enum  PasswordStrength {
    STRONG
}
```

* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        return PasswordStrength.STRONG;
    }
}
```

### 길이만 8글자 미만이고 나머지 조건은 충족하는 경우  
```
@DisplayName("패스워드가 조건을 모두 충족하면 암호 강도가 강함")
@Test
public void meetsAllCriteriaThenStrongPassword() {
    String password = "abcdABCD1234";
    PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
    PasswordStrength result = passwordStrengthMeter.meter(password);
    assertEquals(PasswordStrength.STRONG, result);
}

@DisplayName("길이만 8글자 미만이고 나머지 조건은 충족하는 경우")
@Test
public void meetsOtherCriteriaExceptForLengthThenNormal() {
    String password = "aA1";
    PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
    PasswordStrength result = passwordStrengthMeter.meter(password);
    assertEquals(PasswordStrength.NORMAL, result);
}
```

* PasswordStrength.java
```
public enum  PasswordStrength {
    NORMAL, STRONG
}
```

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

### 숫자를 포함하지 않고 나머지 조건은 충족하는 경우
```
@DisplayName("패스워드가 조건을 모두 충족하면 암호 강도가 강함")
@Test
public void meetsAllCriteriaThenStrongPassword() {
    String password = "abcdABCD1234";
    PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
    PasswordStrength result = passwordStrengthMeter.meter(password);
    assertEquals(PasswordStrength.STRONG, result);
}

@DisplayName("길이만 8글자 미만이고 나머지 조건은 충족하는 경우")
@Test
public void meetsOtherCriteriaExceptForLengthThenNormal() {
    String password = "aA1";
    PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
    PasswordStrength result = passwordStrengthMeter.meter(password);
    assertEquals(PasswordStrength.NORMAL, result);
}

@DisplayName("숫자는 포함하지 않고 나머지 조건은 충족하는 경우")
@Test
public void meetsOtherCriteriaExceptForNumberThenNormal() {
    String password = "aaaaAAAA";
    PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
    PasswordStrength result = passwordStrengthMeter.meter(password);
    assertEquals(PasswordStrength.NORMAL, result);
}
```  

* PasswordStrength.java
```
public enum  PasswordStrength {
    NORMAL, STRONG
}
```

* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if(password.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        boolean containsNumber = false;
        for(char ch : password.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                containsNumber = true;
                break;
            }
        }

        if(!containsNumber) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
}
```

### 잠시 리팩토링(리팩토링하면서 테스트 코드는 께속 돌려보기)
* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if(password.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        if(!meetsContainingNumberCriteria(password)) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    public boolean meetsContainingNumberCriteria(String string) {
        for(char ch : string.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
```

### 테스트 코드도 리팩토링해보자
```
@DisplayName("암호화 검사기 테스트")
public class PasswordStrengthMeterTest {

    private PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();

    private void assertStrength(String password, PasswordStrength expect) {
        PasswordStrength result = passwordStrengthMeter.meter(password);
        assertEquals(expect, result);
    }
    
    @DisplayName("패스워드가 조건을 모두 충족하면 암호 강도가 강함")
    @Test
    public void meetsAllCriteriaThenStrongPassword() {
        String password = "abcdABCD1234";
        assertStrength(password, PasswordStrength.STRONG);
    }

    @DisplayName("길이만 8글자 미만이고 나머지 조건은 충족하는 경우")
    @Test
    public void meetsOtherCriteriaExceptForLengthThenNormal() {
        String password = "aA1";
        assertStrength(password, PasswordStrength.NORMAL);
    }

    @DisplayName("숫자는 포함하지 않고 나머지 조건은 충족하는 경우")
    @Test
    public void meetsOtherCriteriaExceptForNumberThenNormal() {
        String password = "aaaaAAAA";
        assertStrength(password, PasswordStrength.NORMAL);
    }
}
```

### 값이 없는 경우
이런 예외 상황을 잘 고려해서 테스트를 작성해두어야한다.  
항상 가장 발생하지 말아야할 부분부터 고려해서 미리 대응할 수 있도록 하자.  

Null 값에 대해 아무런 작업을 하지 않으면 NPE 가 발생하기 때문에 아래 두가지의 방법을 생각할 수 있다.
* 적절한 Exception 을 발생시킨다.
* 유효하지 않은 암호를 뜻하는 값을 리턴한다.  

여기서 2번째 방법을 채택하고, PasswordStrength.INVALID 라는 값을 추가해서 사용하도록 하겠다.  


