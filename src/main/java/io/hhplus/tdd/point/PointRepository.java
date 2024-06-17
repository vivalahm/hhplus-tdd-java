package io.hhplus.tdd.point;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository {
    UserPoint insertOrUpdate(long id, long amount);

    UserPoint selectById(long id);

    List<PointHistory> selectHistoriesById(long id);

    PointHistory insertHistory(long id, long amount, TransactionType type, long updateMillis);
}
