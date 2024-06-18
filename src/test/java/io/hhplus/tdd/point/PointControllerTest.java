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

@AutoConfigureMockMvc
@SpringBootTest
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    PointService pointService;

    @Test
    @DisplayName("포인트 조회 컨트롤러 테스트")
    void point() throws Exception {
        long id = 1L;
        UserPoint userPoint = new UserPoint(id,0,System.currentTimeMillis());
        when(pointService.point(id)).thenReturn(userPoint);

        mockMvc.perform(get("/point/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.point").value(0));
        verify(pointService).point(id);
    }

    @Test
    @DisplayName("포인트 히스토리 조회 컨트롤러 테스트")
    void history() throws Exception {
        long id = 1L;
        when(pointService.history(id)).thenReturn(List.of());

        mockMvc.perform(get("/point/{id}/histories", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(pointService).history(id);
    }

    @Test
    @DisplayName("포인트 충전 컨트롤러 테스트")
    void charge() throws Exception {
        long id = 1L;
        long amount = 100L;
        UserPoint userPoint = new UserPoint(id, amount, System.currentTimeMillis());
        when(pointService.charge(id, amount)).thenReturn(userPoint);

        mockMvc.perform(patch("/point/{id}/charge", id).content(String.valueOf(amount)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.point").value(amount));
        verify(pointService).charge(id, amount);
    }

    @Test
    @DisplayName("포인트 사용 컨트롤러 테스트")
    void use() throws Exception {
        long id = 1L;
        long amount = 50L;
        UserPoint userPoint = new UserPoint(id, amount, System.currentTimeMillis());
        when(pointService.use(id, amount)).thenReturn(userPoint);

        mockMvc.perform(patch("/point/{id}/use", id).content(String.valueOf(amount)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.point").value(amount));
        verify(pointService).use(id, amount);
    }
}