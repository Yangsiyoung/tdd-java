package tdd.chapter07.sign_up;

public interface UserRepository {
    User findByUserID(String userID);
    void save(User user);
}
