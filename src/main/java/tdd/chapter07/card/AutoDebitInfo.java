package tdd.chapter07.card;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutoDebitInfo {
    private String userID;
    private String cardNumber;

    public void changeCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
