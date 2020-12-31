package tdd.chapter07.stub;

import tdd.chapter07.CardNumberValidator;
import tdd.chapter07.CardValidity;

public class StubCardNumberValidator extends CardNumberValidator {
    private String invalidCardNumber;
    private String theftCardNumber;

    public void setInvalidCardNumber(String invalidCardNumber) {
        this.invalidCardNumber = invalidCardNumber;
    }

    public void setTheftCardNumber(String theftCardNumber) {
        this.theftCardNumber = theftCardNumber;
    }

    public CardValidity validate(String cardNumber) {

        if(invalidCardNumber != null && invalidCardNumber.equals(cardNumber)) {
            return CardValidity.INVALID;
        }

        if(theftCardNumber != null && theftCardNumber.equals(cardNumber)) {
            return CardValidity.THEFT;
        }

        return CardValidity.VALID;
    }

}
