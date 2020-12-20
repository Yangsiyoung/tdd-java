package refactoring.technique;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InlineFunctionTest {

    private InlineFunction inlineFunction;

    @BeforeEach
    public void init() {
        inlineFunction = new InlineFunction();
    }

    @Test
    public void testRating() {
        int expect = 2;
        int result = inlineFunction.rating(6);
        assertEquals(expect, result);
        expect = 1;
        result = inlineFunction.rating(5) ;
        assertEquals(expect, result);
    }
}
