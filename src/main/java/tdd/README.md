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

# TDD 시작하기
[보러가기](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter02)

# 테스트 코드 작성 순서
[보러가기](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter03)

# 기능명세 설계
[보러가기](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter04)

# Junit 5
[보러가기](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter05)

# 테스트 코드의 구성
[보러가기](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter06)

# 대역
[보러가기](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter07)

# 테스트 가능한 설계
[보러가기](https://github.com/Yangsiyoung/tdd-java/tree/master/src/main/java/tdd/chapter08)

