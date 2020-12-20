package refactoring.technique;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import refactoring.technique.dto.ChangeFunctionDeclarationCustomer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeFunctionDeclarationTest {

    private ChangeFunctionDeclaration changeFunctionDeclaration;

    @BeforeEach
    public void init() {
        changeFunctionDeclaration = new ChangeFunctionDeclaration();

    }

    @Test
    public void testCirCum() {
        int radius = 2;
        double expect = 2 * Math.PI * radius;
        double result = changeFunctionDeclaration.circum(radius);
        //assertThat(expect, is(result));
    }

    @Test
    public void testCircumference() {
        int radius = 2;
        double expect = 2 * Math.PI * radius;
        double result = changeFunctionDeclaration.circumference(radius);
        //assertThat(expect, is(result));
    }

    @Test
    public void testIsNewEngland() {
        ChangeFunctionDeclarationCustomer changeFunctionDeclarationCustomer = new ChangeFunctionDeclarationCustomer("ysjleader", "Seoul");
        boolean result = changeFunctionDeclaration.isNewEngland(changeFunctionDeclarationCustomer.getState());
        assertFalse(result);
        changeFunctionDeclarationCustomer = new ChangeFunctionDeclarationCustomer("ysjleader", "NH");
        result = changeFunctionDeclaration.isNewEngland(changeFunctionDeclarationCustomer.getState());
        assertTrue(result);

    }

}