package programmers;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class Solution42839Test {

    private Solution42839 solution42839;

    @Before
    public void init() {
        solution42839 = new Solution42839();
    }

    @Test
    public void testSolution() {
        String numbers = "17";
        int expect = 3;
        int result = solution42839.solution(numbers);
        assertEquals(expect, result);
    }

    @Test
    public void testCalculateCase() {
        String numbers = "17";
        Set<Integer> expectSet = new HashSet<>();
        expectSet.add(1);
        expectSet.add(7);
        expectSet.add(17);
        expectSet.add(71);
        solution42839.solution(numbers);
        Set<Integer> result = solution42839.calculateCase(numbers);
        assertEquals(expectSet, result);
    }
}