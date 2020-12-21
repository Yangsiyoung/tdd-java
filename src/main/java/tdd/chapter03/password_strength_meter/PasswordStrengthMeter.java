package tdd.chapter03.password_strength_meter;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if("abcDef12".equals(password)) {
            return PasswordStrength.STRONG;
        }
        return PasswordStrength.WEAK;
    }
}
