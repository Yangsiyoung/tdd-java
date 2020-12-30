테스트 코드의 구성
===============

# 상황, 실행, 결과 확인
테스트 코드의 기본 골격은 아래와 같다.  
특정 상황이 주어지고(given) -> 기능을 실행하고(when) -> 결과 확인(then)  
꼭 이 구조를 지켜야하는 건 아니고, 테스트 구조를 보고 테스트 내용을 이해할 수 있으면 된다.  
그리고 항상 상황이 있는 건 아니다, 앞선 [암호 강도 측정](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter02) 의 경우  
상황이 없이 암호에 대한 기능을 실행하고 결과만 확인 했을 뿐이다.  

* 상황이 있는 예
```
@DisplayName("정답일 경우")
@Test
public void exactMatch() {
    // 야구 게임 정답이 456인 상황
    BaseballGame baseballGame = new BaseballGame("456");
    int expectStrikeCount = 3;
    int expectBallCount = 0;
    // 기능 실행
    Score score = baseballGame.guess("456");
    // 결과 확인
    assertEquals(expectStrikeCount, score.strikes());
    assertEquals(expectBallCount, score.balls());
}

@DisplayName("오답일 경우")
@Test
public void noMatch() {
    // 야구 게임 정답이 123인 상황
    BaseballGame baseballGame = new BaseballGame("123");
    int expectStrikeCount = 0;
    int expectBallCount = 0;
    // 기능 실행
    Score score = baseballGame.guess("456");
    // 결과 확인
    assertEquals(expectStrikeCount, score.strikes());
    assertEquals(expectBallCount, score.balls());
}
```

  



