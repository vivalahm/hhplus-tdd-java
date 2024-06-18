package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;

import java.util.List;

public class PointRepositoryImpl implements PointRepository {
    UserPointTable userPointTable = new UserPointTable();
    PointHistoryTable pointHistoryTable = new PointHistoryTable();

    @Override
    public UserPoint insertOrUpdate(long id, long amount) {
        return userPointTable.insertOrUpdate(id, amount);
    }

    @Override
    public UserPoint selectById(long id) {
        return userPointTable.selectById(id);
    }

    @Override
    public List<PointHistory> selectHistoriesById(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }

    @Override
    public PointHistory insertHistory(long id, long amount, TransactionType type, long updateMillis) {
        return pointHistoryTable.insert(id, amount, type, updateMillis);
    }
}
