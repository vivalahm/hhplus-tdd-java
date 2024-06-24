package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PointRepository {
    UserPoint insertOrUpdate(long id, long amount);

    UserPoint selectById(long id);

    List<PointHistory> selectHistoriesById(long id);

    PointHistory insertHistory(long id, long amount, TransactionType type, long updateMillis);
}
