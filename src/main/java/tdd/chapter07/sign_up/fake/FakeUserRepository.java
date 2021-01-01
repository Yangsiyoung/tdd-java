package tdd.chapter07.sign_up.fake;

import tdd.chapter07.sign_up.User;
import tdd.chapter07.sign_up.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeUserRepository implements UserRepository {
    private Map<String, User> userMap = new HashMap<>();

    @Override
    public User findByUserID(String userID) {
        return userMap.get(userID);
    }

    @Override
    public void save(User user) {
        userMap.put(user.getUserID(), user);
    }
}
