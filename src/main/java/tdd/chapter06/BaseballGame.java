package tdd.chapter06;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseballGame {
    private final String answer;

    public Score guess(String guess) {
        return countScore(guess);
    }

    private Score countScore(String guess) {
        int ballCount = 0;
        int strikeCount = 0;
        for(int index = 0; index < guess.length(); index++) {
            if(answer.charAt(index) == guess.charAt(index)) {
                strikeCount++;
                continue;
            }

            if(answer.indexOf(guess.charAt(index)) > -1) {
                ballCount++;
            }
        }
        return new Score(strikeCount, ballCount);
    }
}
