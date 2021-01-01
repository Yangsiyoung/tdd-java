package tdd.chapter07.sign_up.spy;

import tdd.chapter07.sign_up.EmailNotifier;

public class SpyEmailNotifier implements EmailNotifier {
    // EmailNotifier 고유기능에 내가 원하는 값을 확인할 수 있도록 추가한 모습이다.
    private boolean called;
    private String email;

    public boolean isCalled() {
        return called;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public void sendSignUpEmail(String email) {
        this.called = true;
        this.email = email;
        // 더이상은 필요없다. 왜냐면 UserRegister 를 테스트하기위한 Spy 대역이니까
        // 이 객체가 호출되었는지, 전달된 이메일이 내가 원한 이메일이 맞는지만 확인할 수 있으면 된다.
    }
}
