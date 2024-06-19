package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.dto.PointHistoryDTO;
import io.hhplus.tdd.point.dto.UserPointDTO;
import io.hhplus.tdd.point.service.PointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/point")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }
    /**
     * 특정 유저의 포인트를 조회하는 기능
     * @param id 사용자 ID
     * @return 사용자의 포인트 정보 반환
     */
    @GetMapping("{id}")
    public UserPointDTO point(
            @PathVariable long id
    ) {
        UserPoint userPoint = pointService.point(id);
        return new UserPointDTO(userPoint.id(), userPoint.point(), userPoint.updateMillis());
    }

    /**
     * 특정 유저의 포인트 충전/이용 내역을 조회하는 기능
     * @param id 사용자 ID
     * @return 사용자의 포인트 충전/이용 내역 반환
     */
    @GetMapping("{id}/histories")
    public List<PointHistoryDTO> history(
            @PathVariable long id
    ) {
        return pointService.history(id).stream()
                .map(history -> new PointHistoryDTO(history.id(), history.userId(), history.amount(), history.type(), history.updateMillis()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 유저의 포인트를 충전하는 기능
     * @param id 사용자 ID
     * @param amount 충전할 포인트
     * @return 충전 후 사용자의 포인트 정보 반환
     */
    @PatchMapping("{id}/charge")
    public UserPointDTO charge(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        UserPoint userPoint = pointService.charge(id, amount);
        return new UserPointDTO(userPoint.id(), userPoint.point(), userPoint.updateMillis());
    }

    /**
     * 특정 유저의 포인트를 사용하는 기능
     * @param id 사용자 ID
     * @param amount 사용할 포인트
     * @return 사용 후 사용자의 포인트 정보 반환
     */
    @PatchMapping("{id}/use")
    public UserPointDTO use(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        UserPoint userPoint = pointService.use(id, amount);
        return new UserPointDTO(userPoint.id(), userPoint.point(), userPoint.updateMillis());
    }
}
