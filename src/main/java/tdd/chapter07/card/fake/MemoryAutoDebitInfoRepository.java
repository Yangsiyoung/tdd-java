package tdd.chapter07.card.fake;

import tdd.chapter07.card.AutoDebitInfo;
import tdd.chapter07.card.AutoDebitInfoRepository;

import java.util.HashMap;
import java.util.Map;

public class MemoryAutoDebitInfoRepository implements AutoDebitInfoRepository {

    private Map<String, AutoDebitInfo> autoDebitInfoMap = new HashMap<>();

    @Override
    public void save(AutoDebitInfo registerInfo) {
        autoDebitInfoMap.put(registerInfo.getUserID(), registerInfo);
    }

    @Override
    public AutoDebitInfo findOne(String userID) {
        return autoDebitInfoMap.get(userID);
    }
}
