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


