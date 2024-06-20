package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PointService {
private final PointRepository pointRepository;
// ReentrantLock을 사용하면 명시적으로 락을 획득하고 해제할 수 있으며, 이는 동시성 문제를 예방하는 데 유용.
private final ReentrantLock lock = new ReentrantLock();

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    /**
     * 특정 사용자의 포인트 정보를 조회하는 기능
     * @param id 사용자 ID
     * @return 사용자의 포인트 정보 반환
     */
    public UserPoint point(long id) {
        UserPoint userPoint = getUserPoint(id); // 유저 정보를 조회
        return userPoint;
    }

    /**
     * 유저의 포인트를 충전. 동시성 제어를 위해 ReentrantLock을 사용한다.
     * @param id 유저 ID
     * @param amount 충전할 포인트 양
     * @return 충전 후 유저의 현재 포인트
     */
    public UserPoint charge(long id, long amount) {
        lock.lock(); // 동시성 문제를 방지하기 위해 락을 사용한다.
        try{
            UserPoint userPoint = getUserPoint(id);
            if(amount <= 0){
                throw new IllegalArgumentException("충전 포인트는 0보다 커야 합니다.");
            }
            userPoint = pointRepository.insertOrUpdate(id, userPoint.point() + amount);
            return userPoint;
        }finally {
            insertHistory(id, amount, TransactionType.CHARGE, System.currentTimeMillis());// 충전 내역을 저장
            lock.unlock();// 작업이 끝난 후 락을 해제
        }
    }

    /**
     * 유저의 포인트를 사용. 동시성 제어를 위해 ReentrantLock을 사용한다.
     * @param id 유저 ID
     * @param amount 사용할 포인트 양
     * @return 사용 후 유저의 현재 포인트
     */
    public UserPoint use(long id, long amount) {
        lock.lock(); // 동시성 문제를 방지하기 위해 락을 사용한다.
        try{
            UserPoint userPoint = getUserPoint(id);
            if(amount <= 0){
                throw new IllegalArgumentException("사용 포인트는 0보다 커야 합니다.");
            }
            if(userPoint.point() < amount){
                throw new IllegalArgumentException("포인트가 부족합니다.");
            }
            userPoint = pointRepository.insertOrUpdate(id, userPoint.point() - amount);
            return userPoint;
        }finally {
            insertHistory(id, amount, TransactionType.CHARGE, System.currentTimeMillis());// 사용 내역을 저장
            lock.unlock();// 작업이 끝난 후 락을 해제
        }
    }

    /**
     * 포인트 내역을 저장하는 메서드
     * @param id 사용자 ID
     * @param amount 포인트 양
     * @param type 거래 유형
     * @param updateMillis 업데이트 시간
     * @return 저장된 포인트 내역
     */
    public PointHistory insertHistory(long id, long amount, TransactionType type, long updateMillis) {
        if (id < 0) {
            throw new IllegalArgumentException("사용자 ID는 0보다 커야 합니다.");
        }
        return pointRepository.insertHistory(id, amount, type, updateMillis);
    }

    /**
     * 사용자의 포인트 내역을 조회하는 메서드
     * @param id 사용자 ID
     * @return 사용자의 포인트 내역
     */
    public List<PointHistory> history(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("사용자 ID는 0보다 커야 합니다.");
        }
        Optional<UserPoint> optionalUserPoint = Optional.ofNullable(pointRepository.selectById(id));
        optionalUserPoint.orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        return pointRepository.selectHistoriesById(id);
    }

    //-----------------------------------------------중복 메서드--------------------------------------------------------

    /**
     * 사용자 정보를 조회하는 메서드
     * @param id
     * @return
     */
     private UserPoint getUserPoint(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("사용자 ID는 0보다 커야 합니다.");
        }
        Optional<UserPoint> optionalUserPoint = Optional.ofNullable(pointRepository.selectById(id)); // 유저 정보를 조회
        UserPoint userPoint = optionalUserPoint.orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        return userPoint;
    }
}
