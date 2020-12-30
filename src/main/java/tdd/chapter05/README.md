Junit 5
========
Junit 5 정리

* Junit Platform : 테스팅 프레임워크 구동을 위한 런처 + 테스트 엔진 API 제공
* Junit Jupiter : Junit 5를 위한 Test API + 실행 엔진 제공
* Junit Vintage : Unit 3, 4로 작성된 테스트를 Junit 5 플랫폼에서 실행하기 위한 모듈 제공

(Gradle 4.6 버전부터 Junit 5 를 사용할 수 있다고한다.)

# @Test Annotation And Test Method
테스트 클래스의 이름을 지을 때 Test 를 접미사로 붙인다.  
테스트를 실행할 메서드에 @Test 어노테이션을 붙여야 한다.  
@Test 를 붙이는 메서드는 private 이면 안된다.  

# 주요 단언 메서드
* _assertEquals(expected, actual)_ : 기대하는 값과 실제 값이 같은지
```
@Test
public void assertEqualsTest() {
    int expect = 3;
    assertEquals(expect, Calculator.plus(1, 2));
}
```  

* _assertNotEquals(unexpected, actual)_ : 기대하는 값과 실제 값이 다른지
```
@Test
public void assertNotEqualsTest() {
    int expect = 3;
    assertNotEquals(expect, Calculator.plus(2, 2));
}
```  

* _assertSame(Object expected, actual)_ : 두 객체가 동일한 객체인지  
```
@Test
public void assertSameTest() {
    Student student1 = new Student("리다양", "우리집");
    Student student2 = student1;
    assertSame(student1, student2);
}
```

```
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {
    private String name;
    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) &&
                Objects.equals(address, student.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }
}
```
* _assertNotSame(Object unexpected, actual)_ : 두 객체가 동일하지 않은 객체인지  
```
@Test
public void assertNotSameTest() {
    Student student1 = new Student("리다양", "우리집");
    Student student2 = new Student("리다양", "우리집");
    assertNotSame(student1, student2);
}
```
* _assertTrue(boolean condition)_ : 값이 True 인지  
```
@Test
public void assertTrueTest() {
    Student student1 = new Student("리다양", "우리집");
    Student student2 = new Student("리다양", "우리집");
    assertTrue(student1.equals(student2));
}
```
* _assertFalse(boolean  condition)_ : 값이 False 인지
```
@Test
public void assertNotTrueTest() {
    Student student1 = new Student("리다양", "우리집");
    Student student2 = new Student("리다양", "우리집");
    assertFalse(student1 == student2);
}
```
  
* _assertNull(Object actual)_  : 값이 null 인지  
```
@Test
public void assertNullTest() {
    Student student1 = null;
    assertNull(student1);
}
```

* _assertNotNull(Object actual)_ : 값이 null 이 아닌지  
```
@Test
public void assertNotNullTest() {
    Student student1 = new Student("리다양", "우리집");
    assertNotNull(student1);
}
```

* _fail()_ : Test 를 실패처리 
```
@Test
public void failTest() {
    Student student1 = new Student("리다양", "우리집");
    if(!student1.getName().equals("리다양")) {
        fail();
    }
    assertEquals(new Student("리다양", "우리집"), student1);
}
```  

assertEquals() 의 경우 주요 타입별로 존재하며, 객체를 비교하는 assertEquals() 의 경우 해당 객체는  
equals() 메서드를 구현해두어야 비교가 가능하다.  

fail() 의 경우 테스트에 실패했음을 알릴 때 사용한다.  

* _assertThrows(Class<T> expectedType, Executable executable)_ : executable 을 실행한 결과로 지정한 타입의 익셉션이 발생하는 지
* Executable.java
```
@FunctionalInterface
@API(
    status = Status.STABLE,
    since = "5.0"
)
public interface Executable {
    void execute() throws Throwable;
}
```
따라서 executable 위치에는 람다식이 들어가면 된다.  

```
@Test
public void assertThrowsTest() {
    Student student1 = new Student("리다양", "우리집", -1);
    assertThrows(InvalidAgeException.class, () -> student1.getAge());
}
```  

```
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {
    private String name;
    private String address;
    private Integer age;

    public int getAge() throws InvalidAgeException {
        if(age < 0) {
            throw new InvalidAgeException();
        }
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) &&
                Objects.equals(address, student.address) &&
                Objects.equals(age, student.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, age);
    }
}
```
* _assertDoesNotThrows(Executable executable)_ : executable 을 실행한 결과로 익셉션이 발생하지 않는지
```
@Test
public void assertDoesNotThrowTest() {
    Student student1 = new Student("리다양", "우리집", 28);
    assertDoesNotThrow(() -> student1.getAge());
}
```

# 테스트 라이프사이클

## @BeforeEach, @AfterEach

각 테스트 메서드 실행 순서  
1. 테스트를 포함한 객체 생성  
2. @BeforeEach 붙은 메서드(있다면) 실행  
3. @Test 붙은 메서드 실행  
4. @AfterEach 붙은 메서드(있다면) 실행  

참고로 @BeforeAll, @AfterAll 어노테이션들이 있는데 이들의 경우 메서드가 static 이어야하고,  
테스트 객체 실행 시 @BeforeAll 의 경우 모든 테스트 메서드들에 앞서 최초로 1회 실행되고  
@AfterAll 의 경우 모든 테스트 메서드들이 실행되고 난 후에 마지막으로 1회 실행된다.  

```
public class JunitLifecycleTest {

    @BeforeAll
    public static void beforeAllTest() {
        System.out.println("### beforeAllTest is executed once###");
    }
    @BeforeEach
    public void beforeEachTest() {
        System.out.println("## beforeEachTest is executed each test##");
    }

    @Test
    public void aTest() {
        System.out.println("# aTest #");
    }

    @Test
    public void bTest() {
        System.out.println("# bTest #");
    }

    @AfterEach
    public void afterEachTest() {
        System.out.println("## afterEachTest is executed each test##");
    }

    @AfterAll
    public static void afterAllTest() {
        System.out.println("### afterAllTest is executed once###");
    }
}
```
* 결과
```
### beforeAllTest is executed once###

## beforeEachTest is executed each test##
# aTest #
## afterEachTest is executed each test##


## beforeEachTest is executed each test##
# bTest #
## afterEachTest is executed each test##

### afterAllTest is executed once###
```

# 테스트 메서드 간 실행 순서에 의존하지 않고, 필드 공유하지 않기
각 테스트 메서드는 서로 독립적으로 동작해야하기 때문에 어떤 테스트의 결과에 따라  
다른 테스트의 결과가 달라지면 안된다.  

그래서 어떤 테스트 메서드가 필드에 값을 할당하고, 수정하는 것에 따라 다른 테스트가 의존하면 안되며  
테스트 메서드 간의 실행 순서 또한 예측하고 의도하면 안된다.  

왜냐면 Junit 버전마다 순서가 바뀔수도있고, 그러면 잘 통과하던 테스트가 실패하기 때문이고  
그렇게 순서에 의존하던 테스트들을 모두 수정해야하기 때문이다.  

# @DisplayName, @Disabled
@DisplayName 은 해당 테스트에 이름을 붙일 수 있다.  
앞서 다뤘던 [암호 검사기](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter02) 테스트 코드를 보면 아래와 같고   
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

실행결과는 아래와 같다. 
영문 메서드로 표현될 테스트들이 @DisplayName 덕에 한글로 어떤 테스트인지 잘 표현 되었다.  

<img width="407" alt="스크린샷 2020-12-29 오후 3 37 50" src="https://user-images.githubusercontent.com/8858991/103263799-3a0d6d00-49ec-11eb-9b19-60dac0c29fcd.png"> 

그리고 특정 테스트를 테스트 대상에서 제외해야할 때 @Disabled 어노테이션을 사용하면 된다.  
```
@Disabled
@Test
public void disabledTest() {
    // test case is not complete, so this test case is disabled temporarily
}
```

# 모든 테스트 실행하기
인텔리제이와 같은 IDE 에서 실행하거나  
Maven 의 경우 : mvn test (wrapper : mvnw test)  
Gradle 의 경우 : gradle test (wraptter : gradlew test)  
명령어를 수행하면 된다.  

* gradle test 실행 결과(통과)
<img width="313" alt="스크린샷 2020-12-29 오후 5 07 50" src="https://user-images.githubusercontent.com/8858991/103269462-46e48d80-49f9-11eb-89ab-76b2a3c5c0b8.png">

* gradle test 실행 결과(실패)
<img width="451" alt="스크린샷 2020-12-29 오후 5 07 21" src="https://user-images.githubusercontent.com/8858991/103269460-43e99d00-49f9-11eb-8add-cf9738b391b0.png">

