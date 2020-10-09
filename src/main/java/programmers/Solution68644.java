package programmers;

import java.util.*;

public class Solution68644 {

    private int[] numbers;
    private boolean[] visited;
    public int[] solution(int[] numbers) {
        this.numbers = numbers;
        this.visited = new boolean[numbers.length];
        int[] answer = getDistinctTotalCombinationOrderByASC();
        return answer;
    }

    private int[] getDistinctTotalCombinationOrderByASC() {
        Set<Integer> result = new TreeSet<>();
        for(int index = 0; index < visited.length; index++) {
            visited[index] = true;
            countCombination(index, result);
        }
        return parseSetToIntArray(result);
    }
    private void countCombination(int visitIndex, Set<Integer> result) {
        List<Integer> resultList = new ArrayList<>();
        for(int index = 0; index < visited.length; index++) {
            if(!visited[index]) {
                result.add(numbers[visitIndex] + numbers[index]);
            }
        }
    }

    private int[] parseSetToIntArray(Set<Integer> set) {
        int[] resultArray = new int[set.size()];
        Integer[] resultIntegerArray = set.toArray(new Integer[0]);
        for(int index = 0; index < resultIntegerArray.length; index++) {
            resultArray[index] = resultIntegerArray[index];
        }
        return resultArray;
    }
}
