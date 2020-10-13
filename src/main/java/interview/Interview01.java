package interview;

public class Interview01 {

    public int calculateN(int n) throws Exception{
        int result = 0;
        int firstPreviousNumber = 1;
        int secondPreviousNumber = 1;

        if (n <= 0) {
            throw new Exception("n은 양수만 가능합니다.");
        }

        if(n <= 2) {
            return firstPreviousNumber;
        }

        for(int index = 3; index <= n; index++) {
                result = firstPreviousNumber + secondPreviousNumber;
                secondPreviousNumber = firstPreviousNumber;
                firstPreviousNumber = result;
        }

        return result;
    }
}


