package tdd.chapter03;


import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayData {
    private LocalDate billingDate;
    private int payAmount;
}
