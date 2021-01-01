package tdd.chapter07.sign_up;

import lombok.RequiredArgsConstructor;
import tdd.chapter07.sign_up.exception.DuplicateIdException;
import tdd.chapter07.sign_up.exception.WeakPasswordException;

@RequiredArgsConstructor
public class UserRegister {
    private final WeakPasswordChecker weakPasswordChecker;
    private final UserRepository userRepository;
    private final EmailNotifier emailNotifier;

    public void register(User user) {
        if(weakPasswordChecker.checkPasswordWeak(user.getPassword())) {
            throw new WeakPasswordException();
        }

        User findUser = userRepository.findByUserID(user.getUserID());
        if(findUser != null) {
            throw new DuplicateIdException();
        }

        userRepository.save(user);
        emailNotifier.sendSignUpEmail(user.getEmail());
    }
}
