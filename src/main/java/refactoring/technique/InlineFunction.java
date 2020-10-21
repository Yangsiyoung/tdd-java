package refactoring.technique;

public class InlineFunction {

    public int rating(int numberOfLateDeliveries) {
        return numberOfLateDeliveries > 5 ? 2 : 1;
    }

}
