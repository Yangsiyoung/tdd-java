package programmers;

import java.util.*;

public class Solution64061 {

    private int[][] board;
    private int size;

    public int solution(int[][] board, int[] moves) {
        this.board = board;
        size = board.length;
        int answer = 0;
        List<Queue<Integer>> queues = initDollQueue();

        return moveDollResult(queues, moves);
    }

    private List<Queue<Integer>> initDollQueue() {
        List<Queue<Integer>> result = new ArrayList<>();
        for(int indexOfX = 0; indexOfX < size; indexOfX++) {
            Queue<Integer> queue = new LinkedList<>();
            for(int indexOfY = 0; indexOfY < size; indexOfY++) {
                if(board[indexOfY][indexOfX] != 0) {
                    queue.add(board[indexOfY][indexOfX]);
                }
            }
            result.add(queue);
        }
        return result;
    }

    private int moveDollResult(List<Queue<Integer>> queues, int[] moves) {
        int result = 0;
        Stack<Integer> dollStack = new Stack<>();
        for(int move : moves) {
            Queue<Integer> queue = queues.get(move - 1);
            if(queue.isEmpty()) {
                continue;
            }
            if(!dollStack.isEmpty() && dollStack.peek().equals(queue.peek())) {
                dollStack.pop();
                queue.poll();
                result += 2;
            } else {
                dollStack.push(queue.poll());
            }
        }
        return result;
    }
}
