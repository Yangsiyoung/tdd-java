package tdd.chapter03.password_strength_meter.slow_adjustment;

import tdd.chapter03.password_strength_meter.PasswordStrength;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if(password.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
}
