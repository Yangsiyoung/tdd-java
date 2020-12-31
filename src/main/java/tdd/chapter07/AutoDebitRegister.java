package tdd.chapter07;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutoDebitRegister {
    private final CardNumberValidator cardNumberValidator;
    private final AutoDebitInfoRepository autoDebitInfoRepository;

    public String register(AutoDebitRequestDTO autoDebitRequestDTO) {
        CardValidity validity = cardNumberValidator.validate(autoDebitRequestDTO.getCardNumber());
        if(validity != CardValidity.VALID) {
            return validity.toString();
        }
        AutoDebitInfo registerInfo = autoDebitInfoRepository.findOne(autoDebitRequestDTO.getUserID());
        if(registerInfo != null) {
            registerInfo.changeCardNumber(autoDebitRequestDTO.getCardNumber());
        } else {
            registerInfo = new AutoDebitInfo(autoDebitRequestDTO.getUserID(), autoDebitRequestDTO.getCardNumber());
            autoDebitInfoRepository.save(registerInfo);
        }
        return CardValidity.VALID.toString();
    }
}
