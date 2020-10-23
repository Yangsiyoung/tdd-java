package refactoring.technique;

public class ExtractVariable {

    public double price(int quantity, int itemPrice) {
        // 가격 = 기본 가격 - 수량 할인 + 배송비
        int basePrice = quantity * itemPrice;
        double quantityDiscount = Math.max(0, quantity - 500) * itemPrice * 0.05;
        double shipping = Math.min(quantity * itemPrice * 0.1, 100);
        return basePrice
                - quantityDiscount
                + shipping;
    }
}
