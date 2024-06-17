package io.hhplus.tdd.point;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public UserPoint point(long id) {
        if(id < 0){
            throw new IllegalArgumentException("id must be positive");
        }
        return pointRepository.selectById(id);
    }

    public synchronized UserPoint charge(long id, long amount) {
        if(id < 0){
            throw new IllegalArgumentException("id must be positive");
        }
        if(amount <= 0){
            throw new IllegalArgumentException("amount must be positive");
        }
        long currentPoint = point(id).point();
        long newPoint = currentPoint + amount;
        pointRepository.insertOrUpdate(id, newPoint);
        insertHistory(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return pointRepository.selectById(id);
    }

    public synchronized UserPoint use(long id, long amount) {
        if(id < 0){
            throw new IllegalArgumentException("id must be positive");
        }
        long currentPoint = point(id).point();
        if(currentPoint < amount){
            throw new IllegalArgumentException("not enough point");
        }
        long remainPoint = currentPoint - amount;
        pointRepository.insertOrUpdate(id, remainPoint);
        insertHistory(id, amount, TransactionType.USE, System.currentTimeMillis());

        return pointRepository.selectById(id);
    }

    public PointHistory insertHistory(long id, long amount, TransactionType type, long updateMillis) {
        return pointRepository.insertHistory(id, amount, type, updateMillis);
    }

    public List<PointHistory> history(long id) {
        if(id < 0){
            throw new IllegalArgumentException("id must be positive");
        }
        return pointRepository.selectHistoriesById(id);
    }
}
