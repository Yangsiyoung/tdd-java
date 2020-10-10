package programmers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Solution60057Test {

    private Solution60057 solution60057;

    @Before
    public void init() {
        solution60057 = new Solution60057();
    }

    @Test
    public void testSolution() {
        int expect = 7;
        String parameter = "aabbaccc";
        int result = solution60057.solution(parameter);
        assertEquals(expect, result);
    }

    @Test
    public void testProcessString() {
        String parameter = "aabbaccc";
        String expect = "2a2ba3c";
        String result = solution60057.processString(parameter, 1);
        assertEquals(expect, result);
        parameter = "ababcdcdababcdcd";
        expect = "2ab2cd2ab2cd";
        result = solution60057.processString(parameter, 2);
        assertEquals(expect, result);
        parameter = "aaaaaaaaaa";
        expect = "10a";
        result = solution60057.processString(parameter, 1);
        assertEquals(expect, result);
    }

}