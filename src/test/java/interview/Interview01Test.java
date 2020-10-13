package interview;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Interview01Test {


    private Interview01 interview01;

    @Before
    public void init() {
        interview01 = new Interview01();
    }

    @Test
    public void testPivot() throws Exception {
        // n 번째 해당하는 피보나치 값
        int expect = 3;
        int n = 4;
        int result = interview01.calculateN(n);
        assertEquals(expect, result);

        expect = 1;
        n = 2;
        result = interview01.calculateN(n);
        assertEquals(expect, result);

        expect = 1;
        n = 1;
        result = interview01.calculateN(n);
        assertEquals(expect, result);

    }
}