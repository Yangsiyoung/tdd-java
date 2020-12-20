package tdd.chapter02;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {

        if(null == password || password.isEmpty()) {
            return PasswordStrength.INVALID;
        }

        boolean lengthEnough = password.length() >= 8;
        boolean containsNumber = meetsContainingNumberCriteria(password);
        boolean containsUppercase = meetsContainingUppercaseCriteria(password);

        if(lengthEnough && !containsNumber && !containsUppercase) {
            return PasswordStrength.WEAK;
        }

        if(!lengthEnough) {
            return PasswordStrength.NORMAL;
        }

        if(!containsNumber) {
            return PasswordStrength.NORMAL;
        }

        if(!containsUppercase) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String string) {
        for(char ch : string.toCharArray()) {
            if(ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean meetsContainingUppercaseCriteria(String string) {
        boolean containsUppercase = false;
        for(char ch : string.toCharArray()) {
            if(ch >= 'A' && ch <= 'Z') {
                containsUppercase = true;
                break;
            }
        }
        return containsUppercase;
    }
}
