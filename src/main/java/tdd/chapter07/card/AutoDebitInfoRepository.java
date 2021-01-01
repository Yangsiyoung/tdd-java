package tdd.chapter07.card;

// JPA Repository
public interface AutoDebitInfoRepository {
    void save(AutoDebitInfo registerInfo);
    AutoDebitInfo findOne(String userID);

}
