package refactoring.technique;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InlineVariableTest {

    private InlineVariable inlineVariable;

    @Before
    public void init() {
        inlineVariable = new InlineVariable();
    }

    @Test
    public void testBasePriceOverThan1000() {
        assertTrue(inlineVariable.basePriceOverThan1000(1001));
        assertFalse(inlineVariable.basePriceOverThan1000(900));
    }

}