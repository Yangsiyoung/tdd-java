package refactoring.technique;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ExtractVariableTest {

    private ExtractVariable extractVariable;

    @Before
    public void init() {
        extractVariable = new ExtractVariable();
    }

    @Test
    public void testPrice() {
        double expect = 5000100.0;
        int quantity = 500;
        int itemPrice = 10000;
        double result = extractVariable.price(quantity, itemPrice);
        assertThat(expect, is(result));
        expect = 5009600.0;
        quantity = 501;
        itemPrice = 10000;
        result = extractVariable.price(quantity, itemPrice);
    }



}