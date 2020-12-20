package refactoring.technique;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InlineVariableTest {

    private InlineVariable inlineVariable;

    @BeforeEach
    public void init() {
        inlineVariable = new InlineVariable();
    }


    @Test
    public void testBasePriceOverThan1000() {
        assertTrue(inlineVariable.basePriceOverThan1000(1001));
        assertFalse(inlineVariable.basePriceOverThan1000(900));
    }

}