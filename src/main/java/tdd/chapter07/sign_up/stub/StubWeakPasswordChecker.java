package tdd.chapter07.sign_up.stub;

import tdd.chapter07.sign_up.WeakPasswordChecker;

public class StubWeakPasswordChecker implements WeakPasswordChecker {
    private boolean weak;
    public void setWeak(boolean weak) {
        this.weak = weak;
    }

    @Override
    public boolean checkPasswordWeak(String password) {
        return this.weak;
    }
}
