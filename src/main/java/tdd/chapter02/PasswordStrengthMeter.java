package tdd.chapter02;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {

        if(null == password) {
            return PasswordStrength.INVALID;
        }

        if(password.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        if(!meetsContainingNumberCriteria(password)) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    public boolean meetsContainingNumberCriteria(String string) {
        for(char ch : string.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
