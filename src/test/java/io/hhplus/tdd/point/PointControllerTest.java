package io.hhplus.tdd.point;

import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc // MockMvc를 자동으로 구성한다
@SpringBootTest // 통합 테스트를 위해 Spring Boot 애플리케이션 컨텍스트를 로드한다
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc 인스턴스를 주입받는다

    @MockBean
    PointService pointService; // PointService를 모킹하여 실제 빈 대신 모킹된 객체를 사용한다

    @Test
    @DisplayName("포인트 조회 컨트롤러 테스트")
    void point() throws Exception {
        long id = 1L; // 테스트에 사용할 사용자 ID를 설정한다
        UserPoint userPoint = new UserPoint(id, 0, System.currentTimeMillis()); // 사용자 포인트 초기값을 설정한다
        when(pointService.getPoint(id)).thenReturn(userPoint); // getPoint 메서드가 userPoint를 반환하도록 설정한다

        mockMvc.perform(get("/point/{id}", id)) // GET 요청을 수행한다
                .andExpect(status().isOk()) // 응답 상태가 200 OK인지 확인한다
                .andExpect(jsonPath("$.id").value(id)) // JSON 응답에서 id 필드를 검증한다
                .andExpect(jsonPath("$.point").value(0)) // JSON 응답에서 point 필드를 검증한다
                .andExpect(jsonPath("$.updateMillis").isNumber()); // JSON 응답에서 updateMillis 필드를 검증한다
        verify(pointService).getPoint(id); // getPoint 메서드가 호출되었는지 확인한다
    }

    @Test
    @DisplayName("포인트 히스토리 조회 컨트롤러 테스트")
    void history() throws Exception {
        long id = 1L; // 테스트에 사용할 사용자 ID를 설정한다
        when(pointService.getHistory(id)).thenReturn(List.of()); // getHistory 메서드가 빈 리스트를 반환하도록 설정한다

        mockMvc.perform(get("/point/{id}/histories", id)) // GET 요청을 수행한다
                .andExpect(status().isOk()) // 응답 상태가 200 OK인지 확인한다
                .andExpect(jsonPath("$").isArray()); // JSON 응답이 배열인지 확인한다
        verify(pointService).getHistory(id); // getHistory 메서드가 호출되었는지 확인한다
    }

    @Test
    @DisplayName("포인트 충전 컨트롤러 테스트")
    void charge() throws Exception {
        long id = 1L; // 테스트에 사용할 사용자 ID를 설정한다
        long amount = 100L; // 충전할 포인트 양을 설정한다
        UserPoint userPoint = new UserPoint(id, amount, System.currentTimeMillis()); // 충전 후의 사용자 포인트를 설정한다
        when(pointService.chargePoint(id, amount)).thenReturn(userPoint); // chargePoint 메서드가 userPoint를 반환하도록 설정한다

        String jsonContent = String.valueOf(amount); // JSON 형식으로 포인트 양을 설정한다

        mockMvc.perform(patch("/point/{id}/charge", id) // PATCH 요청을 수행한다
                        .content(jsonContent) // 요청 본문에 포인트 양을 설정한다
                        .contentType(MediaType.APPLICATION_JSON)) // 요청 본문 타입을 JSON으로 설정한다
                .andExpect(status().isOk()) // 응답 상태가 200 OK인지 확인한다
                .andExpect(jsonPath("$.id").value(id)) // JSON 응답에서 id 필드를 검증한다
                .andExpect(jsonPath("$.point").value(amount)) // JSON 응응답에서 point 필드를 검증한다
                .andExpect(jsonPath("$.updateMillis").isNumber()); // JSON 응답에서 updateMillis 필드를 검증한다
        verify(pointService).chargePoint(id, amount); // chargePoint 메서드가 호출되었는지 확인한다
    }

    @Test
    @DisplayName("포인트 사용 컨트롤러 테스트")
    void use() throws Exception {
        long id = 1L; // 테스트에 사용할 사용자 ID를 설정한다
        long initialAmount = 100L; // 초기 포인트 양을 설정한다
        long useAmount = 50L; // 사용할 포인트 양을 설정한다
        UserPoint initialUserPoint = new UserPoint(id, initialAmount, System.currentTimeMillis()); // 초기 사용자 포인트를 설정한다
        UserPoint updatedUserPoint = new UserPoint(id, initialAmount - useAmount, System.currentTimeMillis()); // 사용 후의 사용자 포인트를 설정한다

        when(pointService.getPoint(id)).thenReturn(initialUserPoint); // getPoint 메서드가 초기 포인트를 반환하도록 설정한다
        when(pointService.usePoint(id, useAmount)).thenReturn(updatedUserPoint); // usePoint 메서드가 사용 후 포인트를 반환하도록 설정한다

        String jsonContent = String.valueOf(useAmount); // JSON 형식으로 사용할 포인트 양을 설정한다

        mockMvc.perform(patch("/point/{id}/use", id) // PATCH 요청을 수행한다
                        .content(jsonContent) // 요청 본문에 포인트 양을 설정한다
                        .contentType(MediaType.APPLICATION_JSON)) // 요청 본문 타입을 JSON으로 설정한다
                .andExpect(status().isOk()) // 응답 상태가 200 OK인지 확인한다
                .andExpect(jsonPath("$.id").value(id)) // JSON 응답에서 id 필드를 검증한다
                .andExpect(jsonPath("$.point").value(initialAmount - useAmount)) // JSON 응답에서 사용 후 포인트 양을 검증한다
                .andExpect(jsonPath("$.updateMillis").isNumber()); // JSON 응답에서 updateMillis 필드를 검증한다
        verify(pointService).usePoint(id, useAmount); // usePoint 메서드가 호출되었는지 확인한다
    }
}