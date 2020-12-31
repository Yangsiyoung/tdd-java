package tdd.chapter07.stub;

import tdd.chapter07.AutoDebitInfoRepository;
import tdd.chapter07.AutoDebitInfo;

public class StubAutoDebitInfoRepository implements AutoDebitInfoRepository {
    @Override
    public void save(AutoDebitInfo registerInfo) {

    }

    @Override
    public AutoDebitInfo findOne(String userID) {
        return null;
    }
}
