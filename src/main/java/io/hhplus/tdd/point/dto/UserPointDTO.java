package io.hhplus.tdd.point.dto;

/**
 * UserPointDTO는 사용자 포인트 정보를 전달하기 위한 데이터 전송 객체
 */
public class UserPointDTO {
    private long id;
    private long point;
    private long updateMillis;

    public UserPointDTO() {
    }

    public UserPointDTO(long id, long point, long updateMillis) {
        this.id = id;
        this.point = point;
        this.updateMillis = updateMillis;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getUpdateMillis() {
        return updateMillis;
    }

    public void setUpdateMillis(long updateMillis) {
        this.updateMillis = updateMillis;
    }
}
