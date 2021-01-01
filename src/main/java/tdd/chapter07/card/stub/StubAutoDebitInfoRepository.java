package tdd.chapter07.card.stub;

import tdd.chapter07.card.AutoDebitInfoRepository;
import tdd.chapter07.card.AutoDebitInfo;

public class StubAutoDebitInfoRepository implements AutoDebitInfoRepository {
    @Override
    public void save(AutoDebitInfo registerInfo) {

    }

    @Override
    public AutoDebitInfo findOne(String userID) {
        return null;
    }
}
