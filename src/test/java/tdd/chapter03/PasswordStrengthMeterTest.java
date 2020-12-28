package tdd.chapter03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tdd.chapter03.password_strength_meter.PasswordStrength;
import tdd.chapter03.password_strength_meter.slow_adjustment.PasswordStrengthMeter;

import static org.junit.jupiter.api.Assertions.*;

class PasswordStrengthMeterTest {

//    @DisplayName("대문자 포함 규칙만 충족하는 경우")
//    @Test
//    public void meetsOnlyUppercaseCriteriaThenWeak() {
//        PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
//        PasswordStrength result = passwordStrengthMeter.meter("abcDef");
//        assertEquals(PasswordStrength.WEAK, result);
//    }
//
//    @DisplayName("모든 규칙을 충족하는 경우")
//    @Test
//    public void meetsAllCriteriaThenStrong() {
//        PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
//        PasswordStrength result = passwordStrengthMeter.meter("abcDef12");
//        assertEquals(PasswordStrength.STRONG, result);
//    }

    @DisplayName("길이만 8글자 미만이고 나머지 조건은 충족하는 경우")
    @Test
    public void meetsOtherCriteriaExceptForLengthThenNormal() {
        PasswordStrengthMeter passwordStrengthMeter = new PasswordStrengthMeter();
        PasswordStrength result = passwordStrengthMeter.meter("abcDef1");
        assertEquals(PasswordStrength.NORMAL, result);
        PasswordStrength result2 = passwordStrengthMeter.meter("abcDef2");
        assertEquals(PasswordStrength.NORMAL, result2);
    }

}