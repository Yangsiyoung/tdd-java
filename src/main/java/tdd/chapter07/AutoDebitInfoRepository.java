package tdd.chapter07;

// JPA Repository
public interface AutoDebitInfoRepository {
    void save(AutoDebitInfo registerInfo);
    AutoDebitInfo findOne(String userID);

}
