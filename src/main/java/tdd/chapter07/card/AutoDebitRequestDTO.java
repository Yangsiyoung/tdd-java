package tdd.chapter07.card;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AutoDebitRequestDTO {
    private final String userID;
    private final String cardNumber;
}
