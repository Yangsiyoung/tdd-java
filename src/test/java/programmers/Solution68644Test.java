package programmers;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class Solution68644Test  {

    @Test
    public void testSolutionWithCase1() {
        Solution68644 solution68644 = new Solution68644();
        int[] expect = {2,3,4,5,6,7};
        int[] result = solution68644.solution(new int[]{2,1,3,4,1});
        assertArrayEquals(expect, result);
    }

}