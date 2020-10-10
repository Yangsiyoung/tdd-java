package programmers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Solution42839 {
    private String numbers;
    private Set<Integer> result = new HashSet<>();

    public int solution(String numbers) {
        this.numbers = numbers;
        Set<Integer> resultSet = calculateCase(numbers);
        return countPrime(resultSet);
    }

    public Set<Integer> calculateCase(String numbers) {
        boolean[] isVisited = new boolean[numbers.length()];
        for (int index = 0; index < numbers.length(); index++) {
            findCase(-1, index, isVisited);
        }
        return this.result;
    }

    private void findCase(int currentValue, int nextIndex, boolean[] isVisited) {
        boolean[] currentIsVisited = Arrays.copyOf(isVisited, isVisited.length);
        StringBuilder stringBuilder;
        if(currentValue > -1) {
            stringBuilder = new StringBuilder(String.valueOf(currentValue));
        } else {
            stringBuilder = new StringBuilder();
        }

        currentValue = Integer.parseInt(stringBuilder.append(numbers.charAt(nextIndex)).toString());
        currentIsVisited[nextIndex] = true;
        result.add(currentValue);
        for (int index = 0; index < numbers.length(); index++) {
            if(!currentIsVisited[index]) {
                findCase(currentValue, index, currentIsVisited);
            }
        }

    }

    private int countPrime(Set<Integer> numberSet) {
        int result = 0;
        for(int number : numberSet) {
            for(int index = 2; index <= number; index++) {
                if(index == number) {
                    result++;
                }

                if(number % index == 0) {
                    break;
                }

            }
        }
        return result;
    }
}
