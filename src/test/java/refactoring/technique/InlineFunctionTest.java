package refactoring.technique;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InlineFunctionTest {

    private InlineFunction inlineFunction;

    @Before
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
