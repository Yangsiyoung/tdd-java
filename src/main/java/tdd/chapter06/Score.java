package tdd.chapter06;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Score {
    private final int strikeCount;
    private final int ballCount;

    public int strikes() {
        return strikeCount;
    }

    public int balls() {
        return ballCount;
    }
}
