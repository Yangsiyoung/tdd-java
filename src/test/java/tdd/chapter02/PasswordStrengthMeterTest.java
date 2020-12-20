package tdd.chapter02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    public void meetsOnlyLengthCriteriaThenNormal() {
        String password = "aaaaaaaa";
        assertStrength(password, PasswordStrength.WEAK);
    }
}
