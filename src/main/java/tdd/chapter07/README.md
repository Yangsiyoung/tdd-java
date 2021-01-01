대역(Test Double)
=====

# 대역의 필요성
테스트를 작성하다보면 어쩔 수 없이 기능이 외부 요인을 필요로 할 때가 있다.  
예를들어 파일 시스템을 사용하거나 DB 가 필요하거나 외부 API 와 통신할 때 등이 있다.  

**중요한건** 외부에 테스트가 의존하게 되면 테스트가 힘들어지기 때문에  
대역을 써서 마치 외부 시스템인 것 처럼 사용하면 테스트가 수월해진다.  

예를들어 신용 카드사 API 를 활용하여 해당 신용카드 번호가 유요한지 확인하고  
그 결과에 따라 자동이체 정보를 저장하는 서비스를 생각해보자.  
우선 실제 구현은 좀 이상하더라도 아래와 같이 되어있다고 가정하자.  

* AutoDebitRegister.java
```
@RequiredArgsConstructor
public class AutoDebitRegister {
    private final CardNumberValidator cardNumberValidator;
    private final AutoDebitInfoRepository autoDebitInfoRepository;

    public String register(AutoDebitRequestDTO autoDebitRequestDTO) {
        CardValidity validity = cardNumberValidator.validate(autoDebitRequestDTO.getCardNumber());
        if(validity != CardValidity.VALID) {
            return validity.toString();
        }
        RegisterInfo registerInfo = autoDebitInfoRepository.findOne(autoDebitRequestDTO.getUserID());
        if(registerInfo != null) {
            registerInfo.changeCardNumber(autoDebitRequestDTO.getCardNumber());
        } else {
            registerInfo = new RegisterInfo(null, autoDebitRequestDTO.getUserID(), autoDebitRequestDTO.getCardNumber());
            autoDebitInfoRepository.save(registerInfo);
        }
        return "SUCCESS";
    }
}
```

* CardNumberValidator.java
```
public class CardNumberValidator {
    public CardValidity validate(String cardNumber) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                                    .uri(URI.create("https://xxx.xx.xxx/card"))
                                    .header("Content-Type", "text/plain")
                                    .POST(HttpRequest.BodyPublishers.ofString(cardNumber))
                                    .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            switch (response.body()) {
                case "OK":
                    return CardValidity.VALID;
                case "BAD":
                    return CardValidity.INVALID;
                case "EXPIRED":
                    return CardValidity.EXPIRED;
                case "THEFT":
                    return CardValidity.THEFT;
                default:
                    return CardValidity.UNKNOWN;
            }
        } catch (IOException | InterruptedException e) {
            return CardValidity.ERROR;
        }
    }
}
```

이 기능들에 대해서 테스트 코드를 짜게되면 외부 업체에서 테스트 목적의 유요한 카드번호를 받아야 하고  
카드가 만료되면 이후부터는 또 만료되어서 통과하던 테스트가 실패하게되고...  
테스트 용도의 도난카드 정보를 받아서 테스트 등록하고 나중에 외부 업체가 그 도난카드 정보를 삭제하면  
또... 통과하던 테스트가 실패하고...  
그래서 이렇게 의존하는 요인떄문에 테스트가 힘들때 대역을 사용한다.  

# 대역을 이용한 테스트
대역을 이용해서 AutoDebitRegister Test 코드를 다시 짜보자.  
먼저 CardNumberValidator 대역부터 만들자.

* StubCardNumberValidator.java
```
public class StubCardNumberValidator extends CardNumberValidator {
    private String invalidCardNumber;

    public void setInvalidCardNumber(String invalidCardNumber) {
        this.invalidCardNumber = invalidCardNumber;
    }

    public CardValidity validate(String cardNumber) {
        if(invalidCardNumber != null && invalidCardNumber.equals(cardNumber)) {
            return CardValidity.INVALID;
        }
        return CardValidity.VALID;
    }

}
```

그리고 대역을 이용해서 테스트 코드를 작성해보자.  
```
public class AutoDebitRegisterTest {
    private AutoDebitRegister autoDebitRegister;
    private StubCardNumberValidator stubCardNumberValidator;
    private StubAutoDebitInfoRepository stubAutoDebitInfoRepository;

    @BeforeEach
    public void init() {
        stubCardNumberValidator = new StubCardNumberValidator();
        stubAutoDebitInfoRepository = new StubAutoDebitInfoRepository();
        autoDebitRegister = new AutoDebitRegister(stubCardNumberValidator, stubAutoDebitInfoRepository);
    }

    @DisplayName("신용카드 번호가 올바르지 않은 경우")
    @Test
    public void invalidCard() {
        String cardNumber = "111122223333";
        stubCardNumberValidator.setInvalidCardNumber(cardNumber);
        AutoDebitRequestDTO autoDebitRequestDTO = new AutoDebitRequestDTO("user1", cardNumber);
        String expect = "INVALID";
        String result = autoDebitRegister.register(autoDebitRequestDTO);
        assertEquals(expect, result);
    }
}
```

추가로 도난 카드에 대한 테스트를 진행해보자. 대역이 있으니 카드사에 도난 처리된 카드 번호를 달라고 요청할 필요가 없다. 
먼저 테스트를 작성해보자.  
```
@DisplayName("도난 카드번호인 경우")
@Test
public void theftCard() {
    String cardNumber = "111122223333";
    stubCardNumberValidator.setTheftCardNumber(cardNumber);
    AutoDebitRequestDTO autoDebitRequestDTO = new AutoDebitRequestDTO("user1", cardNumber);
    String expect = "THEFT";
    String result = autoDebitRegister.register(autoDebitRequestDTO);
    assertEquals(expect, result);
}
``` 

그리고 StubCardNumberValidator 에 도난카드 관련 설정을 추가하자.  
* StubCardNumberValidator.java
```
public class StubCardNumberValidator extends CardNumberValidator {
    private String invalidCardNumber;
    private String theftCardNumber;

    public void setInvalidCardNumber(String invalidCardNumber) {
        this.invalidCardNumber = invalidCardNumber;
    }

    public void setTheftCardNumber(String theftCardNumber) {
        this.theftCardNumber = theftCardNumber;
    }

    public CardValidity validate(String cardNumber) {

        if(invalidCardNumber != null && invalidCardNumber.equals(cardNumber)) {
            return CardValidity.INVALID;
        }

        if(theftCardNumber != null && theftCardNumber.equals(cardNumber)) {
            return CardValidity.THEFT;
        }
        
        return CardValidity.VALID;
    }

}
```

테스트가 잘통과하는지 돌려보고 전체적으로도 돌려보자.  
(나도 모르는 사이드 이펙트를 확인하기위해.. 몇초안걸리니까 다돌려보자...)  

이제 DB 처리 관련해서도 대역을 사용해보자.  
DB 연동을 위한 Repository Interface 가 아래와 같다고하자.  
  
* AutoDebitInfoRepository.java
```
public interface AutoDebitInfoRepository {
    void save(RegisterInfo registerInfo);
    AutoDebitInfo findOne(String userID);
}
```

대역을 사용하면 실제 DB 없이 테스트를 할 수 있다.  
현재 테스트 코드에서 바라보고있는 StubAAutoDebitInfoRepository 코드는 아래와 같다.  
* StubAutoDebitInfoRepository.java
```
public class StubAutoDebitInfoRepository implements AutoDebitInfoRepository {
    @Override
    public void save(AutoDebitInfo registerInfo) {

    }

    @Override
    public AutoDebitInfo findOne(String userID) {
        return null;
    }
}
```

이런 상태에서는 AutoDebitRegister 의 register 메소드 내 DB 관련 기능의 테스트를 아예 할 수 없으니  
대역을 좀 더 상세하게 구현해보자.  
* MemoryAutoDebitInfoRepository.java
```
public class MemoryAutoDebitInfoRepository implements AutoDebitInfoRepository {

    private Map<String, AutoDebitInfo> autoDebitInfoMap = new HashMap<>();

    @Override
    public void save(AutoDebitInfo registerInfo) {
        autoDebitInfoMap.put(registerInfo.getUserID(), registerInfo);
    }

    @Override
    public AutoDebitInfo findOne(String userID) {
        return autoDebitInfoMap.get(userID);
    }
}
```

이렇게하면 자동이체 정보를 메모리에 저장할 수 있다.  
그리고 테스트 코드의 의존성을 수정하고, 테스트 코드를 작성해보자.  
```
@DisplayName("이미 자동이체 정보가 존재하고, 기존 카드 번호와 다른 카드일 경우 카드 번호 갱신")
@Test
public void alreadyRegisteredThenUpdate() {
    String userID = "user1";
    memoryAutoDebitInfoRepository.save(new AutoDebitInfo(userID, "1111222233334444"));
    AutoDebitRequestDTO autoDebitRequestDTO = new AutoDebitRequestDTO(userID, "555566667777");
    autoDebitRegister.register(autoDebitRequestDTO);
    AutoDebitInfo result = memoryAutoDebitInfoRepository.findOne(autoDebitRequestDTO.getUserID());
    assertEquals(autoDebitRequestDTO.getCardNumber(), result.getCardNumber());
}

@DisplayName("기존 자동이체 정보가 없다면, 새로 등록")
@Test
public void notRegisteredThenSave() {
    AutoDebitRequestDTO autoDebitRequestDTO = new AutoDebitRequestDTO("user1", "555566667777");
    autoDebitRegister.register(autoDebitRequestDTO);
    AutoDebitInfo result = memoryAutoDebitInfoRepository.findOne(autoDebitRequestDTO.getUserID());
    assertEquals(autoDebitRequestDTO.getCardNumber(), result.getCardNumber());
}
```

이렇게 대역을 사용해서 외부 카드 정보 API 연동 없이, 자동이체 정보 저장한 DB 없이 테스트를 진행할 수 있었다.  

# 대역의 종류
|대역 종류|설명|
|:-------:|:-----|
|Stub|구현을 단순한 것으로 대체, 그냥 테스트에 맞게 단순히 원하는 동작만 수행, StubCardNumberValidator 가 여기에 해당|
|Fake|상용에는 부적합하지만, 실제 동작하는 구현 제공, DB 대신 메모리로 구현했던 MemoryAutoDebitRepository 가 여기에 해당|
|Spy|호출된 내역 기록, 기록한 내용으로 테스트 결과 검증 때 사용|
|Mock|기대한대로 상호작용 하는지 행위를 검증, 기대한대로 동작하지않으면 익셉션 throw 가능, Stub 이자 Spy 도 된다.|

간단한 회원가입 예를 통해 알아보도록 하겠다.  
먼저 회원가입 기능은 다음과 같다.  

1. 암호가 약한지 검사하고
2. 동일한 아이디로 가입된 정보가 있는지 확인하고
3. 회원가입 처리를 하고
4. 회원가입 이메일 발송을 한다

그리고 이에 대한 객체들을 살펴보자.  
* UserRegister : 회원가입에 대한 핵심 로직 수행
* WeakPasswordChecker : 암호가 약한지 검사
* UserRepository : 회원 정보 저장 / 조회
* EmailNotifier : 이메일 발송 기능

중요한 건 **테스트 대상은 UserRegister** 이다.

## 약한 암호 확인 기능에 스텁(Stub) 사용
테스트 대상이 UserRegister 이므로 WeakPasswordChecker 는 대역을 사용할 것 이고,  
약한 암호인지 여부만 알려주면 되기때문에 스텁으로 충분한다.  

```
public class UserRegisterTest {
    private UserRegister userRegister;
    private StubWeakPasswordChecker stubWeakPasswordChecker = new StubWeakPasswordChecker();

    @BeforeEach
    public void init() {
        userRegister = new UserRegister(stubWeakPasswordChecker);
    }

    @DisplayName("약한 암호면 가입 실패")
    @Test
    public void weakPassword() {
        stubWeakPasswordChecker.setWeak(true);
        assertThrows(WeakPasswordException.class, () -> userRegister.register("userId", "pw", "email"));
    }
}
```

* UserRegister.java
```
@RequiredArgsConstructor
public class UserRegister {
    private final WeakPasswordChecker weakPasswordChecker;

    public void register(String userId, String password, String email) {
        if(weakPasswordChecker.checkPasswordWeak(password)) {
            throw new WeakPasswordException();
        }
    }
}
```

* WeakPasswordChecker.java
```
public interface WeakPasswordChecker {
    boolean checkPasswordWeak(String password);
}
```

* StubWeakPasswordChecker.java
```
public class StubWeakPasswordChecker implements WeakPasswordChecker {
    private boolean weak;
    public void setWeak(boolean weak) {
        this.weak = weak;
    }

    @Override
    public boolean checkPasswordWeak(String password) {
        return this.weak;
    }
}
```

## Repository 를 가짜 구현(Fake) 으로 사용
다음 테스트로는 동일 ID 를 가진 회원이 존재할 경우 익셉션을 발생시키는 테스트를 하자.  
먼저 테스트를 짜야하는 아마 아래와 같을 것 이다.  
```
// 상황 : 동일 ID 를 가진 회원이 DB 에 존재
상황을 만들기위한 코드

// 실행 및 검증
assertThorws(DuplicateIdException.class, () -> userRegister.register("id", "pw", "email"));
```

이제 상황을 만들기 위한 코드와 실행 및 검증 코드를 합친 전체 코드를 보자.
```
public class UserRegisterTest {
    private UserRegister userRegister;
    private StubWeakPasswordChecker stubWeakPasswordChecker = new StubWeakPasswordChecker();
    private FakeUserRepository fakeUserRepository = new FakeUserRepository();

    @BeforeEach
    public void init() {
        userRegister = new UserRegister(stubWeakPasswordChecker, fakeUserRepository);
    }

    @DisplayName("약한 암호면 가입 실패")
    @Test
    public void weakPassword() {
        stubWeakPasswordChecker.setWeak(true);
        assertThrows(WeakPasswordException.class, () -> userRegister.register(new User("userId", "pw", "email")));
    }

    @DisplayName("이미 존재하는 계정이면 가입 실패")
    @Test
    public void duplicateID() {
        User user = new User("userId", "pw", "email");
        fakeUserRepository.save(user);
        assertThrows(DuplicateIdException.class, () -> userRegister.register(user));
    }
}
```

* UserRegister.java
```
@RequiredArgsConstructor
public class UserRegister {
    private final WeakPasswordChecker weakPasswordChecker;
    private final UserRepository userRepository;

    public void register(User user) {
        if(weakPasswordChecker.checkPasswordWeak(user.getPassword())) {
            throw new WeakPasswordException();
        }

        User findUser = userRepository.findByUserID(user.getUserID());
        if(findUser != null) {
            throw new DuplicateIdException();
        }
    }
}
```

* User.java
```
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private String userID;
    private String password;
    private String email;
}
```

* UserRepository.java
```
public interface UserRepository {
    User findByUserID(String userID);
    void save(User user);
}
```

* FakeUserRepository.java
```
public class FakeUserRepository implements UserRepository {
    private Map<String, User> userMap = new HashMap<>();

    @Override
    public User findByUserID(String userID) {
        return userMap.get(userID);
    }

    @Override
    public void save(User user) {
        userMap.put(user.getUserID(), user);
    }
}
```

그리고 회원가입에 성공한 테스트도 진행해보자.
```
@DisplayName("같은 ID가 없으면 가입 성공함")
@Test
public void noDuplicateId() {
    User user = new User("userId", "pw", "email");
    userRegister.register(user);

    User findUser = fakeUserRepository.findByUserID(user.getUserID());
    assertEquals(user.getUserID(), findUser.getUserID());
    assertEquals(user.getEmail(), findUser.getEmail());
}
```

될거라 생각하고 테스트를 돌렸는데 실패한다. findUser 가 Null 이란다...  
생각해보니 UserRegister.register() 에 저장하는 코드가 없다 그래서 추가해줬다.  
* UserRegister.java
```
@RequiredArgsConstructor
public class UserRegister {
    private final WeakPasswordChecker weakPasswordChecker;
    private final UserRepository userRepository;

    public void register(User user) {
        if(weakPasswordChecker.checkPasswordWeak(user.getPassword())) {
            throw new WeakPasswordException();
        }

        User findUser = userRepository.findByUserID(user.getUserID());
        if(findUser != null) {
            throw new DuplicateIdException();
        }

        userRepository.save(user);
    }
}
```

여기서 생각을 해보자
Stub 과 Fake 의 차이  
**Stub** 은 그냥 말그대로 리턴을 하는 등 **조건만** 맞추기위한 역할을 했고,  
**Fake** 의 경우 **어느정도 구현이 필요한 대역일 경우**에 구현을 최소한으로 진행한 기능을 수행했다.

## 이메일 발송 여부를 확인하기 위해 스파이 사용
회원가입에 성공하면 이메일로 회원가입 안내 메일을 발송하는데 이를 검정하기 위한 테스트는 아래와 같을 것 이다.  
```
// 회원가입 진행
userRegister.register(new User("id", "pw", "email@domain.com"))

// 결과
email@domain.com 으로 이메일 발송 요청했는지 확인
``` 

내가 테스트하는 대상은 UserRegister 의 register 이다.  
따라서 EmailNotifier 의 이메일 보내기 기능이 제대로 수행되었는지보다  
EmailNotifier 에게 메시지를 보내 협력할때 내가 원하는 이메일 주소를 전달했는지가 중요하다.  

이럴때(내가 원하는 값으로 전달이 되었는지...) Spy 대역을 이용한다.  

스파이 구현은 아래와 같다.  
* SpyEmailNotifier.java
```
public class SpyEmailNotifier implements EmailNotifier {
    // EmailNotifier 고유기능에 내가 원하는 값을 확인할 수 있도록 추가한 모습이다.
    private boolean called;
    private String email;

    public boolean isCalled() {
        return called;
    }

    public String getEmail() {
        return email;
    }
}
```

아직 EmailNotifier 인터페이스는 메서드를 정의하지 않고 테스트 코드부터 작성하러 가보자.  
```
@DisplayName("회원가입 성공하면 이메일을 전송함")
@Test
public void afterSignUpSendEmail() {
    User user = new User("userId", "pw", "email@domain.com");
    userRegister.register(user);
    assertTrue(spyEmailNotifier.isCalled());
    assertEquals(user.getEmail(), spyEmailNotifier.getEmail());
}
```

그리고 이제 회원가입 시 이메일 전송코드를 포함한 UserRegister 를 작성해보자.
* UserRegister.java
```
@RequiredArgsConstructor
public class UserRegister {
    private final WeakPasswordChecker weakPasswordChecker;
    private final UserRepository userRepository;
    private final EmailNotifier emailNotifier;

    public void register(User user) {
        if(weakPasswordChecker.checkPasswordWeak(user.getPassword())) {
            throw new WeakPasswordException();
        }

        User findUser = userRepository.findByUserID(user.getUserID());
        if(findUser != null) {
            throw new DuplicateIdException();
        }

        userRepository.save(user);
        emailNotifier.sendSignUpEmail(user.getEmail());
    }
}
```

자 이제 UserRegister 가 EmailNotifier 에게 어떤 메세지를 보낼지 작성했으니  
EmailNotifier 의 코드를 보자.

* EmailNotifier.java
```
public interface EmailNotifier {
    void sendSignUpEmail(String email);
}
```

그리고 Spy 대역을 보자.
* SpyEmailNotifier.java
```
public class SpyEmailNotifier implements EmailNotifier {
    // EmailNotifier 고유기능에 내가 원하는 값을 확인할 수 있도록 추가한 모습이다.
    private boolean called;
    private String email;

    public boolean isCalled() {
        return called;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public void sendSignUpEmail(String email) {
        this.called = true;
        this.email = email;
        // 더이상은 필요없다. 왜냐면 UserRegister 를 테스트하기위한 Spy 대역이니까
        // 이 객체가 호출되었는지, 전달된 이메일이 내가 원한 이메일이 맞는지만 확인할 수 있으면 된다.
    }
}
```

그리고 앞서 작성한 테스트가 통과하는지 돌려보자.  
모든 테스트가 통과한다.  

## 모의 객체(Mock Object) 로 스텁과 스파이 대체
Mock 객체의 경우 Stub 과 Spy 가 될 수 있다고 했다.  
앞서 작성한 코드를 Mock 객체로 바꿔보자.  

약한 암호의 경우 가입에 실패하는 케이스를 다시 작성해보자.
```
public class UserRegisterMockTest {
    private UserRegister userRegister;
    private WeakPasswordChecker mockWeakPasswordChecker = Mockito.mock(WeakPasswordChecker.class); // Mockito 이용하여 Mock 객체 생성
    private FakeUserRepository fakeUserRepository = new FakeUserRepository(); // Mockito 이용하여 Mock 객체 생성
    private EmailNotifier mockEmailNotifier = Mockito.mock(EmailNotifier.class);

    @BeforeEach
    public void init() {
        userRegister = new UserRegister(mockWeakPasswordChecker, fakeUserRepository, mockEmailNotifier);
    }

    @DisplayName("약한 암호면 가입 실패")
    @Test
    public void weakPassword() {
        // DBBMockito 를 통해 Mock 객체를 설정하겠다.
        BDDMockito
                // mockWeakPasswordChecker.checkPasswordWeak 메서드에 대한 설정을 하겠다.
                // 인자로 어떤 스트링이 넘어올때(BDDMockito.anyString())
                .given(mockWeakPasswordChecker.checkPasswordWeak(BDDMockito.anyString()))
                // true 를 반환하겠다.
                .willReturn(true);

        assertThrows(WeakPasswordException.class, () -> userRegister.register(new User("id", "pw", "email")));
    }
}
```

Stub 을 해보았으니 이번에는 Spy 를 해보자.  
회원가입 이메일 전송 관련 테스트를 Mock 객체로 다시 작성해보자.  
```
@DisplayName("회원가입 성공하면 이메일을 전송함")
@Test
public void afterSignUpSendEmail() {
    User user = new User("userId", "pw", "email@domain.com");
    userRegister.register(user);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    BDDMockito
            // 호출 여부를 검증할 모의 객체를 전달받고
            .then(mockEmailNotifier)
            // should 뒤에 붙는 메서드가 호출되어야하는지 확인하고 전달된 인자를 가져옴
            // 만약에 should 뒤의 sendSignUpEmail 함수가 호출되지 않았다면
            // 테스트가 실패하고 Wanted but not invoked: 메시지를 보게된다.
            .should().sendSignUpEmail(captor.capture());

    assertEquals(user.getEmail(), captor.getValue());

}
```  
  
이렇게 Mock 객체를 이용하여 따로 클래스를 만들지 않고 테스트 코드 내에서 정의하고 테스트가 가능했다.  

# 상황과 결과 확인을 위한 협업대상(의존) 도출과 대역 사용
외부 상황에 대한 의존도가 있고, 그 외부 상황은 제어하기 힘들기때문에 아래와 같은 방식으로 진행해보자.  
* 제어하기 힘든 외부 상황을 별도 타입으로 분리한다.
* 테스트 코드는 별도로 분리한 타입의 대역을 생성한다.
* 생성한 대역을 테스트 대상의 생성자 등을 이용해 전달하고
* 대역을 이용해서 상황을 구성하고 테스트를 진행한다.  

외부 상황에 대한 의존성이 높거나 (위에서 다뤘던 카드 정보 외부 API 에 의존하던 상황이나, 회원가입 시 이메일 발송 같은 상황)    
혹은 당장 구현하는데 시간이 오래걸릴 것 같은 로직을 포함하고있는 상황(위에서 다뤘던 비밀번호가 약한지 검사했던 WeakPasswordChecker) 은
대역을 사용하기 좋은 후보이다.  

# 대역과 개발속도
TDD 에서 대역을 사용하지 않고 실제 구현을 사용하면 아래와 같은 일이 벌어진다.  
* 카드 정보 제공 업체에서 도난 카드 번호를 받을 때 까지 기다린다.
* 카드 정보 제공 API 가 비정상 응답을 주는 상황을 테스트하기위해 업체에 요청을 하고 기다려야한다.
* 회원가입 테스트를 한 뒤 이메일이 도착할 때까지 메일함을 확인한다.
* 약한 암호 검사 기능을 개발할 때까지 회원가입 테스트를 대기한다.

이처럼 다양한 외부 요인에 의해 테스트가 늦어질 수 있고,  
약한 암호 검사 기능을 같은 팀 개발자가 구현하고있다면, 그 개발자가 구현을 완료할 때까지 기다려야  
약한 암호에 대한 회원 가입 테스트를 진행할 수 있다... 그렇게 때문에 대역이 필요하다.  

대역은 테스트 대상이 의존하고 있는 대상을 구현하지 않아도, 테스트 대상의 테스트를 진행할 수 있게 해주며,  
이는 시간을 줄여줘서 개발 속도를 올리는 데 도움이 된다고 한다.  

# 모의 객체(Mock) 를 과하게 사용하지 않기
Mock 객체 Stub 과 Spy 역할을 할 수 있기 때문에 대역으로 Mock 객체가 많이 사용된다고 한다.  
회원가입 성공 테스트를 모의 객체를 이용해서 작성해보자  

```
@DisplayName("같은 ID가 없으면 가입 성공함")
@Test
public void noDuplicateId() {
    User user = new User("userId", "pw", "email");
    userRegister.register(user);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    BDDMockito
            // mockUserRepository 객체의 호출 여부를 검증할거고
            .then(mockUserRepository)
            // 호출되어야한다
            .should()
            // mockUserRepository 객체의 save() 메서드가
            // 그리고 save() 메서드에 넘어온 인자를 저장하겠다.
            .save(captor.capture());

    // 위에서 저장한 인자를 가져온다
    User saveUser = captor.getValue();
    assertEquals(user.getUserID(), saveUser.getUserID());
    assertEquals(user.getEmail(), saveUser.getEmail());
}
``` 

여기서 목적은 회원 데이터가 올바르게 저장되었는지이다.  
그런데 Mock 객체를 사용하니까 save() 메서드가 호출되었는지 확인하고,  
ArgumentCaptor 를 이용해서 호출할때 전달한 인자를 저장해야한다.  
하지만 앞서 이용한 Fake 를 사용하면 결과 확인 코드가 단순해진다.  
```
@DisplayName("같은 ID가 없으면 가입 성공함")
@Test
public void noDuplicateId() {
    User user = new User("userId", "pw", "email");
    userRegister.register(user);

    User findUser = fakeUserRepository.findByUserID(user.getUserID());
    assertEquals(user.getUserID(), findUser.getUserID());
    assertEquals(user.getEmail(), findUser.getEmail());
}
```

Mock 객체를 사용했을때는 "Repository 의 save 메서드를 호출해야하며, 이때 전달된 객체의 값이 어떤 값이어야 한다." 고  
검증하고있다면, Fake 를 사용하면 "Repository 에 저장된 값이 어떠 값이어야 한다." 라는 식으로  
실제로 검증해야하는 부분에 대해서 명확하게 나타낼 수 있다.  

Mock 객체를 사용하면 처음에는 대역 클래스를 만들지 않아도 되어서 편한데,  
결과 값을 확인하는 수단으로 사용하면 테스트 코드가 길어지고 복잡해진다.  

또한 Mock 객체는 기본적으로 메서드 호출 여부를 검증하는 수단이기 때문에 테스트 대상과 Mock 객체와의  
상호 작용이 조금만 바뀌어도 테스트가 깨지기 쉽다.(then().should() 사용할 경우)  

그래서 Mock 객체의 메서드 호출 여부(위의 호출여부 검사하면서 전달된 인자를 저장했던 로직 참고)를 결과 검증 수단으로 사용하는 것은 주의해야한다.  
DAO 나 Repository 같이 저장소에 대한 대역은 Mock 객체보단 Fake 를 사용하는 것이  
테스트 코드 관리에 유리하다고한다.  

처음에는 Fake 구현을 해야해서 귀찮겠지만, 한번 구현하고나면 Mock 객체를 사용할 때 보다  
테스트 코드도 간결해지고 관리해지기 쉬워진다고 한다.  
