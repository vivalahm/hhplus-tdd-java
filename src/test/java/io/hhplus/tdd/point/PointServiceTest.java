package io.hhplus.tdd.point;

import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.repository.PointRepository;
import io.hhplus.tdd.point.repository.PointRepositoryImpl;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class PointServiceTest {
    private PointService pointService;
    private final PointRepository pointRepository = new PointRepositoryImpl();

    @BeforeEach
    void setUp() {
        pointService = new PointService(pointRepository);
    }

    /**
     *  1. 포인트 조회 테스트, 사용자 ID로 조회시 0포인트를 반환한다.
     */
    @Test
    @DisplayName("포인트 조회 테스트 - 사용자 ID로 조회시 0포인트를 반환한다.")
    void point_by_id_return_zero_point() {
        // given
        long id = 1L;
        // when
        UserPoint userPoint = pointService.getPoint(id);
        // then
        assertEquals(0, userPoint.point());
    }

    /**
     *  2. 포인트 조회 테스트, 사용자 ID가 음수이면 IllegalArgumentException을 발생시킨다.
     */
    @Test
    @DisplayName("포인트 조회 테스트 - 사용자 ID가 음수이면 IllegalArgumentException을 발생시킨다.")
    void point_by_negative_id_throw_exception() {
        // given
        long id = -1L;
        // when, then
        assertThrows(IllegalArgumentException.class, () -> pointService.getPoint(id));
    }

    /**
     *  3. 포인트 충전 테스트, 사용자 ID로 충전시 충전된 포인트를 반환한다.
     */
    @Test
    @DisplayName("포인트 충전 테스트 - 사용자 ID로 충전시 충전된 포인트를 반환한다.")
    void charge_Point_by_id_return_charged_point() {
        // given
        long id = 1L;
        long amount = 100L;
        // when
        UserPoint userPoint = pointService.chargePoint(id, amount);
        // then
        assertEquals(amount, userPoint.point());
    }

    /**
     * 3-1. 포인트 충전 테스트,100원이 충전된 사용자 ID에 1000원을 충전시 충전된 포인트1100원을 반환한다.
     */
    @Test
    @DisplayName("포인트 충전 테스트 - 100원이 충전된 사용자 ID에 1000원을 충전시 충전된 포인트 1100원을 반환한다.")
    void charge_Point_by_id_return_charged_point_1100() {
        // given
        long id = 1L;
        long amount = 100L;
        pointService.chargePoint(id, amount);
        // when
        UserPoint userPoint = pointService.chargePoint(id, amount * 10);
        // then
        assertEquals(amount * 11, userPoint.point());
    }

    /**
     * 4. 포인트 충전 테스트, 충전 금액이 음수이거나 0이면 IllegalArgumentException을 발생시킨다.
     */
    @Test
    @DisplayName("포인트 충전 테스트 - 충전 금액이 음수이거나 0이면 IllegalArgumentException을 발생시킨다.")
    void charge_Point_by_negative_or_zero_amount_throw_exception() {
        // given
        long id = 1L;
        long amount = -100L;
        // when, then
        assertThrows(IllegalArgumentException.class, () -> pointService.chargePoint(id, amount));
    }

    /**
     * 5. 포인트 사용 테스트, 사용자 ID로 1000원 충전 후 1000원 사용시 0포인트를 반환한다.
     */
    @Test
    @DisplayName("포인트 사용 테스트 - 사용자 ID로 1000원 충전 후 1000원 사용시 0포인트를 반환한다.")
    void use_Point_by_id_return_zero_point() {
        // given
        long id = 1L;
        long amount = 1000L;
        pointService.chargePoint(id, amount);
        // when
        UserPoint userPoint = pointService.usePoint(id, amount);
        // then
        assertEquals(0, userPoint.point());
    }


    /**
     * 6. 포인트 사용 테스트, 사용자 ID로 1000원 충전 후 2000원 사용시 IllegalArgumentException을 발생시킨다.
     */
    @Test
    @DisplayName("포인트 사용 테스트 - 사용자 ID로 1000원 충전 후 2000원 사용시 IllegalArgumentException을 발생시킨다.")
    void use_Point_by_over_point_throw_exception() {
        // given
        long id = 1L;
        long amount = 1000L;
        pointService.chargePoint(id, amount);
        // when, then
        assertThrows(IllegalArgumentException.class, () -> pointService.usePoint(id, amount * 2));
    }

    /**
     * 7. 포인트 사용 테스트, 사용자 ID로 1000원 충전 후 500원 사용시 500포인트를 반환한다.
     */
    @Test
    @DisplayName("포인트 사용 테스트 - 사용자 ID로 1000원 충전 후 500원 사용시 500포인트를 반환한다.")
    void use_Point_by_id_return_remain_point() {
        // given
        long id = 1L;
        long amount = 1000L;
        pointService.chargePoint(id, amount);
        // when
        UserPoint userPoint = pointService.usePoint(id, amount / 2);
        // then
        assertEquals(amount / 2, userPoint.point());
    }

    /**
     * 8. 포인트 내역 조회 테스트, 사용자 ID로 조회시 빈 내역을 반환한다.
     */
    @Test
    @DisplayName("포인트 내역 조회 테스트 - 사용자 ID로 조회시 빈 내역을 반환한다.")
    void getHistory_by_id_return_empty_list() {
        // given
        long id = 1L;
        // when
        var histories = pointService.getHistory(id);
        // then
        assertTrue(histories.isEmpty());
    }

    /**
     * 9. 포인트 내역 조회 테스트, 사용자 ID로 조회시 내역을 반환한다.
     */
    @Test
    @DisplayName("포인트 내역 조회 테스트 - 사용자 ID로 조회시 내역을 반환한다.")
    void getHistory_by_id_return_list() {
        // given
        long id = 1L;
        long amount = 1000L;
        pointService.chargePoint(id, amount);
        // when
        var histories = pointService.getHistory(id);
        // then
        assertFalse(histories.isEmpty());
    }

    /**
     * 10. 포인트 내역 조회 테스트, 사용자 ID가 음수이면 IllegalArgumentException을 발생시킨다.
     */
    @Test
    @DisplayName("포인트 내역 조회 테스트 - 사용자 ID가 음수이면 IllegalArgumentException을 발생시킨다.")
    void getHistory_by_negative_id_throw_exception() {
        // given
        long id = -1L;
        // when, then
        assertThrows(IllegalArgumentException.class, () -> pointService.getHistory(id));
    }

    /**
     * 11. 동시에 여러 건의 포인트 충전 요청이 들어온 경우 순차적으로 처리되어야 한다.
     */
    @Test
    @DisplayName("동시에 여러 건의 포인트 충전 요청이 들어온 경우 순차적으로 처리되어야 한다.")
    void charge_Point_concurrently() throws InterruptedException {
        // given
        long id = 1L; // 사용자 ID를 설정합니다.
        long amount = 100L; // 충전하고 사용할 포인트의 양을 설정합니다.
        CountDownLatch chargeLatch = new CountDownLatch(1); // 충전 작업이 완료되었음을 알리는 래치입니다.
        CountDownLatch allLatch = new CountDownLatch(2); // 모든 작업이 완료되었음을 알리는 래치입니다.

        // when
        new Thread(() -> { // 새로운 스레드에서
            pointService.chargePoint(id, amount); // 포인트를 충전하고,
            chargeLatch.countDown(); // 충전 작업이 완료되었음을 알립니다.
            allLatch.countDown(); // 이 스레드의 작업이 완료되었음을 알립니다.
        }).start();

        new Thread(() -> { // 또 다른 새로운 스레드에서
            try {
                chargeLatch.await(); // 충전 작업이 완료될 때까지 기다립니다.
                pointService.usePoint(id, amount); // 그리고 포인트를 사용합니다.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 인터럽트가 발생하면 현재 스레드를 인터럽트 상태로 설정합니다.
            }
            allLatch.countDown(); // 이 스레드의 작업이 완료되었음을 알립니다.
        }).start();

        // then
        allLatch.await(); // 모든 작업이 완료될 때까지 기다립니다.
        assertEquals(0, pointService.getPoint(id).point()); // 그리고 사용자의 포인트가 0인지 확인합니다.
    }
}