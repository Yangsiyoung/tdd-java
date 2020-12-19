package tdd.chapter02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test // Junit 에서 @Test 어노테이션이 붙어 있어야 테스트 메서드로 인식
    void plus() {
        int result = Calculator.plus(1, 2);
        assertEquals(3, result); // assertEquals() : 기대한 값과 실제 값이 동일한지 비교
    }

}
