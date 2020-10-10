package programmers;

public class Solution60057 {

    public int solution(String s) {
        int answer = Integer.MAX_VALUE;
        for(int index = 1; index <= s.length(); index++) {
            answer = Math.min(answer, processString(s, index).length());
        }
        return answer;
    }

    public String processString(String parameter, int size) {
        StringBuilder result = new StringBuilder();
        String temp = parameter;
        while (temp.length() > 0) {
            String firstString;
            if(temp.length() <= size) {
                firstString = temp;
                result.append(firstString);
                break;
            }
            firstString = temp.substring(0, size);
            temp = temp.substring(size);
            int count = 0;
            for(int index = 0; index <= temp.length() - size; index += size) {
                if(temp.substring(index, index + size).equals(firstString)) {
                    count++;
                } else {
                    break;
                }
            }
            temp = temp.substring(count * size);

            if(temp.equals(firstString)) {
                count++;
                temp = "";
            }

            if(count > 0) {
                result.append(count + 1).append(firstString);
            } else {
                result.append(firstString);
            }

        }
        return result.toString();
    }
}
