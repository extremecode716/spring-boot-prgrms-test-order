package com.github.prgrms.orders;

import com.github.prgrms.configures.JwtTokenConfigure;
import com.github.prgrms.products.ProductRestController;
import com.github.prgrms.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewRestControllerTest {

    private MockMvc mockMvc;

    private JwtTokenConfigure jwtTokenConfigure;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setJwtTokenConfigure(JwtTokenConfigure jwtTokenConfigure) {
        this.jwtTokenConfigure = jwtTokenConfigure;
    }

    @Test
    @Order(1)
    @WithMockJwtAuthentication
    @DisplayName("리뷰 작성 실패 테스트 (리뷰 내용 누락)")
    void reviewFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/orders/5/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"content\":null}")
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ReviewRestController.class))
                .andExpect(handler().methodName("review"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
        ;
    }

    @Test
    @Order(2)
    @DisplayName("리뷰 작성 실패 테스트 (토큰이 올바르지 않을 경우)")
    void reviewFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/orders/5/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(jwtTokenConfigure.getHeader(), "Bearer " + randomAlphanumeric(60))
                        .content("{\"content\":\"this is review content!\"}")
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)))
                .andExpect(jsonPath("$.error.message", is("Unauthorized")))
        ;
    }

    @Test
    @Order(3)
    @WithMockJwtAuthentication
    @DisplayName("리뷰 작성 실패 테스트 (리뷰 작성 불가 상태)")
    void reviewFailureTest3() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/orders/1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"this is review content!\"}")
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ReviewRestController.class))
                .andExpect(handler().methodName("review"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
        ;
    }

    @Test
    @Order(4)
    @WithMockJwtAuthentication
    @DisplayName("리뷰 작성 실패 테스트 (이미 리뷰가 존재함)")
    void reviewFailureTest4() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/orders/4/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"this is review content!\"}")
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ReviewRestController.class))
                .andExpect(handler().methodName("review"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
        ;
    }

    @Test
    @Order(5)
    @WithMockJwtAuthentication
    @DisplayName("리뷰 작성 실패 테스트 (리뷰 내용 최대 길이 초과)")
    void reviewFailureTest5() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/orders/5/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"" + randomAlphanumeric(1001) + "\"}")
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ReviewRestController.class))
                .andExpect(handler().methodName("review"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
        ;
    }

    @Test
    @Order(6)
    @WithMockJwtAuthentication
    @DisplayName("리뷰 작성 성공 테스트")
    void reviewSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/orders/5/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"this is review content!\"}")
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ReviewRestController.class))
                .andExpect(handler().methodName("review"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.productId", is(3)))
                .andExpect(jsonPath("$.response.content", is("this is review content!")))
        ;
        result = mockMvc.perform(
                get("/api/products/3")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.seq", is(3)))
                .andExpect(jsonPath("$.response.reviewCount", is(1)))
        ;
        result = mockMvc.perform(
                get("/api/orders/5")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.seq", is(5)))
                .andExpect(jsonPath("$.response.productId", is(3)))
                .andExpect(jsonPath("$.response.state", is("COMPLETED")))
                .andExpect(jsonPath("$.response.review").exists())
                .andExpect(jsonPath("$.response.review.seq").isNumber())
                .andExpect(jsonPath("$.response.review.productId", is(3)))
                .andExpect(jsonPath("$.response.review.content", is("this is review content!")))
        ;
    }

}