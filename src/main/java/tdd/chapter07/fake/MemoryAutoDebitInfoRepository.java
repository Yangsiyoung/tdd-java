package tdd.chapter07.fake;

import tdd.chapter07.AutoDebitInfo;
import tdd.chapter07.AutoDebitInfoRepository;

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
