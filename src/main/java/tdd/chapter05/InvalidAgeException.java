package tdd.chapter05;

public class InvalidAgeException extends Throwable {
    public InvalidAgeException() {
        super("나이가 올바르지 않습니다.");
    }
}
