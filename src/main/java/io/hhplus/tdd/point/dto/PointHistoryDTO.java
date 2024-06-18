package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.TransactionType;

/**
 * PointHistoryDTO는 포인트 거래 내역 정보를 전달하기 위한 데이터 전송 객체
 */
public class PointHistoryDTO {
    private long id;
    private long userId;
    private long amount;
    private TransactionType type;
    private long updateMillis;

    public PointHistoryDTO() {
    }

    public PointHistoryDTO(long id, long userId, long amount, TransactionType type, long updateMillis) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.updateMillis = updateMillis;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public long getUpdateMillis() {
        return updateMillis;
    }

    public void setUpdateMillis(long updateMillis) {
        this.updateMillis = updateMillis;
    }
}
