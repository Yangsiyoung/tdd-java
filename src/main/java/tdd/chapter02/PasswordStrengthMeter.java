package tdd.chapter02;

import java.util.ArrayList;
import java.util.List;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String password) {
        if(null == password || password.isEmpty()) {
            return PasswordStrength.INVALID;
        }
        return decidePasswordStrength(password);
    }

    private PasswordStrength decidePasswordStrength(String password) {
        List<Boolean> meetsCriteriaList = getMeetsCriteriaList(password);
        return getPasswordStrengthByMeetsCriteriaList(meetsCriteriaList);
    }

    private List<Boolean> getMeetsCriteriaList(String password) {
        List<Boolean> meetsCriteriaList = new ArrayList<>();
        boolean lengthEnough = password.length() >= 8;
        meetsCriteriaList.add(lengthEnough);
        boolean containsNumber = meetsContainingNumberCriteria(password);
        meetsCriteriaList.add(containsNumber);
        boolean containsUppercase = meetsContainingUppercaseCriteria(password);
        meetsCriteriaList.add(containsUppercase);
        return meetsCriteriaList;
    }

    private PasswordStrength getPasswordStrengthByMeetsCriteriaList(List<Boolean> meetsCriteriaList) {
        int meetsCriteriaCount = (int)meetsCriteriaList.stream().filter(criteria->criteria).count();

        if(meetsCriteriaCount <= 1) {
            return PasswordStrength.WEAK;
        }

        if(meetsCriteriaCount == 2) {
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
