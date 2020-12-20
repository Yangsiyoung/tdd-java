TDD 시작하기
=========

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
그리고 추가적으로 빈값에 대해서도 처리를 하겠다.  

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

    @DisplayName("Null 값인 경우")
    @Test
    public void nullInputThenInvalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @DisplayName("빈값인 경우")
    @Test
    public void emptyInputThenInvalid() {
        assertStrength("", PasswordStrength.INVALID);
    }
}
```

* PasswordStrength.java
```
public enum  PasswordStrength {
    NORMAL, INVALID, STRONG
}
```

* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {

        if(null == password || password.isEmpty()) {
            return PasswordStrength.INVALID;
        }

        if(password.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        if(!meetsContainingNumberCriteria(password)) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String string) {
        for(char ch : string.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
```
### 대문자를 포함하지 않고 나머지 조건을 충족하는 경우
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

    @DisplayName("Null 값인 경우")
    @Test
    public void nullInputThenInvalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @DisplayName("빈값인 경우")
    @Test
    public void emptyInputThenInvalid() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @DisplayName("대문자를 포함하지 않고 나머지 조건을 충족하는 경우")
    @Test
    public void meetsOtherCriteriaExceptForUppercaseThenNormal() {
        String password = "aaaabbbb1";
        assertStrength(password, PasswordStrength.NORMAL);
    }
}
```

* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {

        if(null == password || password.isEmpty()) {
            return PasswordStrength.INVALID;
        }

        if(password.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        if(!meetsContainingNumberCriteria(password)) {
            return PasswordStrength.NORMAL;
        }

        if(!meetsContainingUppercaseCriteria(password)) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String string) {
        for(char ch : string.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean meetsContainingUppercaseCriteria(String string) {
        boolean containsUppercase = false;
        for(char ch : string.toCharArray()) {
            if(ch >= 'A' && ch <= 'Z') {
                containsUppercase = true;
                break;
            }
        }
        return containsUppercase;
    }
}
```
### 이제 남은 테스트들
지금까지 모든 조건을 충족하거나, 한가지 조건만 충족하지 않는 경우를 진행했고,  
이제 한가지 조건만 충족하거나, 모든 조건을 충족하지 않는 경우에 대한 테스트를 진행해야한다.

### 길이가 8글자 이상인 조건만 충족하는 경우
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

    @DisplayName("Null 값인 경우")
    @Test
    public void nullInputThenInvalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @DisplayName("빈값인 경우")
    @Test
    public void emptyInputThenInvalid() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @DisplayName("대문자를 포함하지 않고 나머지 조건을 충족하는 경우")
    @Test
    public void meetsOtherCriteriaExceptForUppercaseThenNormal() {
        String password = "aaaabbbb1";
        assertStrength(password, PasswordStrength.NORMAL);
    }

    @DisplayName("길이가 8글자 이상인 조건만 충족하는 경우")
    @Test
    public void meetsOnlyLengthCriteriaThenWeak() {
        String password = "aaaaaaaa";
        assertStrength(password, PasswordStrength.WEAK);
    }
}
```

* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {

        if(null == password || password.isEmpty()) {
            return PasswordStrength.INVALID;
        }

        boolean lengthEnough = password.length() >= 8;
        boolean containsNumber = meetsContainingNumberCriteria(password);
        boolean containsUppercase = meetsContainingUppercaseCriteria(password);

        if(lengthEnough && !containsNumber && !containsUppercase) {
            return PasswordStrength.WEAK;
        }

        if(!lengthEnough) {
            return PasswordStrength.NORMAL;
        }

        if(!containsNumber) {
            return PasswordStrength.NORMAL;
        }

        if(!containsUppercase) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String string) {
        for(char ch : string.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean meetsContainingUppercaseCriteria(String string) {
        boolean containsUppercase = false;
        for(char ch : string.toCharArray()) {
            if(ch >= 'A' && ch <= 'Z') {
                containsUppercase = true;
                break;
            }
        }
        return containsUppercase;
    }
}
```

### 숫자 포함 조건만 충족하는 경우
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

    @DisplayName("Null 값인 경우")
    @Test
    public void nullInputThenInvalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @DisplayName("빈값인 경우")
    @Test
    public void emptyInputThenInvalid() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @DisplayName("대문자를 포함하지 않고 나머지 조건을 충족하는 경우")
    @Test
    public void meetsOtherCriteriaExceptForUppercaseThenNormal() {
        String password = "aaaabbbb1";
        assertStrength(password, PasswordStrength.NORMAL);
    }

    @DisplayName("길이가 8글자 이상인 조건만 충족하는 경우")
    @Test
    public void meetsOnlyLengthCriteriaThenWeak() {
        String password = "aaaaaaaa";
        assertStrength(password, PasswordStrength.WEAK);
    }

    @DisplayName("숫자 포함 조건만 충족하는 경우")
    @Test
    public void meetsOnlyNumberCriteriaThenWeak() {
        String password = "1234567";
        assertStrength(password, PasswordStrength.WEAK);
    }
}
```

* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {

        if(null == password || password.isEmpty()) {
            return PasswordStrength.INVALID;
        }

        boolean lengthEnough = password.length() >= 8;
        boolean containsNumber = meetsContainingNumberCriteria(password);
        boolean containsUppercase = meetsContainingUppercaseCriteria(password);

        if(lengthEnough && !containsNumber && !containsUppercase) {
            return PasswordStrength.WEAK;
        }

        if(!lengthEnough && containsNumber && !containsUppercase) {
            return PasswordStrength.WEAK;
        }

        if(!lengthEnough) {
            return PasswordStrength.NORMAL;
        }

        if(!containsNumber) {
            return PasswordStrength.NORMAL;
        }

        if(!containsUppercase) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String string) {
        for(char ch : string.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean meetsContainingUppercaseCriteria(String string) {
        boolean containsUppercase = false;
        for(char ch : string.toCharArray()) {
            if(ch >= 'A' && ch <= 'Z') {
                containsUppercase = true;
                break;
            }
        }
        return containsUppercase;
    }
}
```

### 대문자 포함 조건만 충족하는 경우
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

    @DisplayName("Null 값인 경우")
    @Test
    public void nullInputThenInvalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @DisplayName("빈값인 경우")
    @Test
    public void emptyInputThenInvalid() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @DisplayName("대문자를 포함하지 않고 나머지 조건을 충족하는 경우")
    @Test
    public void meetsOtherCriteriaExceptForUppercaseThenNormal() {
        String password = "aaaabbbb1";
        assertStrength(password, PasswordStrength.NORMAL);
    }

    @DisplayName("길이가 8글자 이상인 조건만 충족하는 경우")
    @Test
    public void meetsOnlyLengthCriteriaThenWeak() {
        String password = "aaaaaaaa";
        assertStrength(password, PasswordStrength.WEAK);
    }

    @DisplayName("숫자 포함 조건만 충족하는 경우")
    @Test
    public void meetsOnlyNumberCriteriaThenWeak() {
        String password = "1234567";
        assertStrength(password, PasswordStrength.WEAK);
    }

    @DisplayName("대문자 포함 조건만 충족하는 경우")
    @Test
    public void meetsOnlyUppercaseCriteriaThenWeak() {
        String password = "ABCD";
        assertStrength(password, PasswordStrength.WEAK);
    }
}
```

* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {

        if(null == password || password.isEmpty()) {
            return PasswordStrength.INVALID;
        }

        boolean lengthEnough = password.length() >= 8;
        boolean containsNumber = meetsContainingNumberCriteria(password);
        boolean containsUppercase = meetsContainingUppercaseCriteria(password);

        if(lengthEnough && !containsNumber && !containsUppercase) {
            return PasswordStrength.WEAK;
        }

        if(!lengthEnough && containsNumber && !containsUppercase) {
            return PasswordStrength.WEAK;
        }

        if(!lengthEnough && !containsNumber && containsUppercase) {
            return PasswordStrength.WEAK;
        }

        if(!lengthEnough) {
            return PasswordStrength.NORMAL;
        }

        if(!containsNumber) {
            return PasswordStrength.NORMAL;
        }

        if(!containsUppercase) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String string) {
        for(char ch : string.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean meetsContainingUppercaseCriteria(String string) {
        boolean containsUppercase = false;
        for(char ch : string.toCharArray()) {
            if(ch >= 'A' && ch <= 'Z') {
                containsUppercase = true;
                break;
            }
        }
        return containsUppercase;
    }
}
```

### 리팩토링
아무조건도 충족하지 않는 경우를 생각해야하지만 그전에 리팩토링을 먼저 진행하려한다.  
테스트 - 코드작성 - 리팩토링의 호흡이 길면 그만큼 더 고칠게 많아지기 떄문이다.  

우선 케이스별로 판단하기보다는 3가지 조건중 몇가지 조건이 충족되는지 그 개수로 판단하면 더 좋을 것 같다.  
책에서는 조금 다른 방법으로 리팩토링을 진행했지만, 내가 생각이 들었던 방식대로 해두고 나중에 실력이 더 쌓이면  
더 좋은 방법으로 개선해보자.  

* PasswordStrengthMeter.java
```
public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if(null == password || password.isEmpty()) {
            return PasswordStrength.INVALID;
        }
        return decidePasswordStrength(password);
    }

    private PasswordStrength decidePasswordStrength(String password) {
        List<Boolean> meetsCriteriaList = getMeetsCriteriaList(password);
        return getPasswordStrengthByMeetsCriteriaList(meetsCriteriaList);
    }

    private List<Boolean> getMeetsCriteriaList(String password) {
        List<Boolean> meetsCriteriaList = new ArrayList<>();
        boolean lengthEnough = password.length() >= 8;
        meetsCriteriaList.add(lengthEnough);
        boolean containsNumber = meetsContainingNumberCriteria(password);
        meetsCriteriaList.add(containsNumber);
        boolean containsUppercase = meetsContainingUppercaseCriteria(password);
        meetsCriteriaList.add(containsUppercase);
        return meetsCriteriaList;
    }

    private PasswordStrength getPasswordStrengthByMeetsCriteriaList(List<Boolean> meetsCriteriaList) {
        int meetsCriteriaCount = (int)meetsCriteriaList.stream().filter(criteria->criteria).count();

        if(meetsCriteriaCount <= 1) {
            return PasswordStrength.WEAK;
        }

        if(meetsCriteriaCount == 2) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String string) {
        for(char ch : string.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean meetsContainingUppercaseCriteria(String string) {
        boolean containsUppercase = false;
        for(char ch : string.toCharArray()) {
            if(ch >= 'A' && ch <= 'Z') {
                containsUppercase = true;
                break;
            }
        }
        return containsUppercase;
    }
}
```

### 아무 조건도 충족하지 않은 경우
이 경우는 위에서 리팩토링하며 충족하는 경우의 수가 1이하인 경우 WEAK 로 판단하도록 진행해두어서  
바로 통과하게 되었다.  

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

    @DisplayName("Null 값인 경우")
    @Test
    public void nullInputThenInvalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @DisplayName("빈값인 경우")
    @Test
    public void emptyInputThenInvalid() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @DisplayName("대문자를 포함하지 않고 나머지 조건을 충족하는 경우")
    @Test
    public void meetsOtherCriteriaExceptForUppercaseThenNormal() {
        String password = "aaaabbbb1";
        assertStrength(password, PasswordStrength.NORMAL);
    }

    @DisplayName("길이가 8글자 이상인 조건만 충족하는 경우")
    @Test
    public void meetsOnlyLengthCriteriaThenWeak() {
        String password = "aaaaaaaa";
        assertStrength(password, PasswordStrength.WEAK);
    }

    @DisplayName("숫자 포함 조건만 충족하는 경우")
    @Test
    public void meetsOnlyNumberCriteriaThenWeak() {
        String password = "1234567";
        assertStrength(password, PasswordStrength.WEAK);
    }

    @DisplayName("대문자 포함 조건만 충족하는 경우")
    @Test
    public void meetsOnlyUppercaseCriteriaThenWeak() {
        String password = "ABCD";
        assertStrength(password, PasswordStrength.WEAK);
    }

    @DisplayName("아무 조건도 충족하지 않은 경우")
    @Test
    public void meetsNoCriteriaThenWeak() {
        String password = "abc";
        assertStrength(password, PasswordStrength.WEAK);
    }
}
```

### 정리
1. 기능을 검증하는 테스트를 먼저 작성한다.
2. 테스트를 통과할만큼의 코드를 작성한다.
3. 테스트를 통과한 뒤 개선할 코드가 있으면 리팩토링한다.
4. 리팩토링한 코드가 정상독장하는지 테스트를 진행한다.
5. 이 과정을 반복한다.


