package programmers;

import org.junit.Test;

import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class Solution64061Test {

    @Test
    public void testSolution() {
        Solution64061 solution64061 = new Solution64061();
        int expect = 4;
        int[][] board = {{0,0,0,0,0},{0,0,1,0,3},{0,2,5,0,1},{4,2,4,4,2},{3,5,1,3,1}};
        int[] moves = {1,5,3,5,1,2,1,4};
        int result = solution64061.solution(board, moves);
        assertEquals(expect, result);
    }
}