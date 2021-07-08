package com.github.prgrms.orders;

import com.github.prgrms.configures.JwtTokenConfigure;
import com.github.prgrms.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderRestControllerTest {

    private static final Map<Integer, Integer> productIdForOrderId = new HashMap<Integer, Integer>() {{
        put(1, 1);
        put(2, 1);
        put(3, 2);
        put(4, 2);
        put(5, 3);
        put(6, 3);
        put(7, 3);
    }};

    private static final Map<Integer, String> stateForOrderId = new HashMap<Integer, String>() {{
        put(1, "REQUESTED");
        put(2, "ACCEPTED");
        put(3, "SHIPPING");
        put(4, "COMPLETED");
        put(5, "COMPLETED");
        put(6, "REJECTED");
        put(7, "REQUESTED");
    }};

    private static final Map<Integer, String> requestMessageForOrderId = new HashMap<Integer, String>() {{
        put(4, "plz send it quickly!");
    }};

    private static final Map<Integer, String> rejectMessageForOrderId = new HashMap<Integer, String>() {{
        put(6, "No stock");
    }};

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
    @DisplayName("주문 목록 페이징 조회 성공 테스트 (offset=0,size=2)")
    void findAllSuccessTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("offset", "0")
                        .param("size", "2")
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("findAll"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response.length()", is(2)))
                .andExpect(jsonPath("$.response[0].seq", is(7)))
                .andExpect(jsonPath("$.response[0].productId", is(productIdForOrderId.get(7))))
                .andExpect(jsonPath("$.response[0].state", is(stateForOrderId.get(7))))
                .andExpect(jsonPath("$.response[1].seq", is(6)))
                .andExpect(jsonPath("$.response[1].productId", is(productIdForOrderId.get(6))))
                .andExpect(jsonPath("$.response[1].state", is(stateForOrderId.get(6))))
                .andExpect(jsonPath("$.response[1].rejectMessage", is(rejectMessageForOrderId.get(6))))
                .andExpect(jsonPath("$.response[1].rejectedAt").exists())
        ;
    }

    @Test
    @Order(2)
    @WithMockJwtAuthentication
    @DisplayName("주문 목록 페이징 조회 성공 테스트 (offset=2,size=1)")
    void findAllSuccessTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("offset", "2")
                        .param("size", "1")
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("findAll"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response.length()", is(1)))
                .andExpect(jsonPath("$.response[0].seq", is(5)))
                .andExpect(jsonPath("$.response[0].productId", is(productIdForOrderId.get(5))))
                .andExpect(jsonPath("$.response[0].state", is(stateForOrderId.get(5))))
                .andExpect(jsonPath("$.response[0].completedAt").exists())
        ;
    }

    @Test
    @Order(3)
    @WithMockJwtAuthentication
    @DisplayName("주문 목록 페이징 조회 성공 테스트 (offset=-1,size=10)")
    void findAllSuccessTest3() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("offset", "-1")
                        .param("size", "10")
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("findAll"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response.length()", is(5)))
                .andExpect(jsonPath("$.response[0].seq", is(7)))
                .andExpect(jsonPath("$.response[0].productId", is(productIdForOrderId.get(7))))
                .andExpect(jsonPath("$.response[0].state", is(stateForOrderId.get(7))))
                .andExpect(jsonPath("$.response[1].seq", is(6)))
                .andExpect(jsonPath("$.response[1].productId", is(productIdForOrderId.get(6))))
                .andExpect(jsonPath("$.response[1].state", is(stateForOrderId.get(6))))
                .andExpect(jsonPath("$.response[1].rejectMessage", is(rejectMessageForOrderId.get(6))))
                .andExpect(jsonPath("$.response[1].rejectedAt").exists())
                .andExpect(jsonPath("$.response[2].seq", is(5)))
                .andExpect(jsonPath("$.response[2].productId", is(productIdForOrderId.get(5))))
                .andExpect(jsonPath("$.response[2].state", is(stateForOrderId.get(5))))
                .andExpect(jsonPath("$.response[2].completedAt").exists())
                .andExpect(jsonPath("$.response[3].seq", is(4)))
                .andExpect(jsonPath("$.response[3].productId", is(productIdForOrderId.get(4))))
                .andExpect(jsonPath("$.response[3].state", is(stateForOrderId.get(4))))
                .andExpect(jsonPath("$.response[3].requestMessage", is(requestMessageForOrderId.get(4))))
                .andExpect(jsonPath("$.response[3].completedAt").exists())
                .andExpect(jsonPath("$.response[3].review").exists())
                .andExpect(jsonPath("$.response[3].review.content", is("I like it!")))
                .andExpect(jsonPath("$.response[4].seq", is(3)))
                .andExpect(jsonPath("$.response[4].productId", is(productIdForOrderId.get(3))))
                .andExpect(jsonPath("$.response[4].state", is(stateForOrderId.get(3))))
        ;
    }

    @Test
    @Order(4)
    @WithMockJwtAuthentication
    @DisplayName("주문 목록 페이징 조회 성공 테스트 (offset=null,size=null)")
    void findAllSuccessTest4() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("findAll"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response.length()", is(5)))
                .andExpect(jsonPath("$.response[0].seq", is(7)))
                .andExpect(jsonPath("$.response[0].productId", is(productIdForOrderId.get(7))))
                .andExpect(jsonPath("$.response[0].state", is(stateForOrderId.get(7))))
                .andExpect(jsonPath("$.response[1].seq", is(6)))
                .andExpect(jsonPath("$.response[1].productId", is(productIdForOrderId.get(6))))
                .andExpect(jsonPath("$.response[1].state", is(stateForOrderId.get(6))))
                .andExpect(jsonPath("$.response[1].rejectMessage", is(rejectMessageForOrderId.get(6))))
                .andExpect(jsonPath("$.response[1].rejectedAt").exists())
                .andExpect(jsonPath("$.response[2].seq", is(5)))
                .andExpect(jsonPath("$.response[2].productId", is(productIdForOrderId.get(5))))
                .andExpect(jsonPath("$.response[2].state", is(stateForOrderId.get(5))))
                .andExpect(jsonPath("$.response[2].completedAt").exists())
                .andExpect(jsonPath("$.response[3].seq", is(4)))
                .andExpect(jsonPath("$.response[3].productId", is(productIdForOrderId.get(4))))
                .andExpect(jsonPath("$.response[3].state", is(stateForOrderId.get(4))))
                .andExpect(jsonPath("$.response[3].requestMessage", is(requestMessageForOrderId.get(4))))
                .andExpect(jsonPath("$.response[3].completedAt").exists())
                .andExpect(jsonPath("$.response[3].review").exists())
                .andExpect(jsonPath("$.response[3].review.content", is("I like it!")))
                .andExpect(jsonPath("$.response[4].seq", is(3)))
                .andExpect(jsonPath("$.response[4].productId", is(productIdForOrderId.get(3))))
                .andExpect(jsonPath("$.response[4].state", is(stateForOrderId.get(3))))
        ;
    }

    @Test
    @Order(5)
    @DisplayName("주문 목록 페이징 조회 실패 테스트 (토큰이 올바르지 않을 경우)")
    void findAllFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(jwtTokenConfigure.getHeader(), "Bearer " + randomAlphanumeric(60))
                        .param("offset", "0")
                        .param("size", "5")
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
    @Order(6)
    @WithMockJwtAuthentication
    @DisplayName("주문 조회 성공 테스트")
    void findByIdSuccessTest() throws Exception {
        for (int i = 1; i < 8; i++) {
            ResultActions result = mockMvc.perform(
                    get("/api/orders/" + i)
                            .accept(MediaType.APPLICATION_JSON)
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(OrderRestController.class))
                    .andExpect(handler().methodName("findById"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response.seq", is(i)))
                    .andExpect(jsonPath("$.response.productId", is(productIdForOrderId.get(i))))
                    .andExpect(jsonPath("$.response.state", is(stateForOrderId.get(i))))
            ;
            if (i == 4) {
                result
                        .andExpect(jsonPath("$.response.requestMessage", is(requestMessageForOrderId.get(i))))
                        .andExpect(jsonPath("$.response.completedAt").exists())
                        .andExpect(jsonPath("$.response.rejectMessage").doesNotExist())
                        .andExpect(jsonPath("$.response.rejectedAt").doesNotExist())
                        .andExpect(jsonPath("$.response.review").exists())
                        .andExpect(jsonPath("$.response.review.content", is("I like it!")))
                ;
            } else if (i == 5) {
                result.andExpect(jsonPath("$.response.requestMessage").doesNotExist())
                        .andExpect(jsonPath("$.response.completedAt").exists())
                        .andExpect(jsonPath("$.response.rejectMessage").doesNotExist())
                        .andExpect(jsonPath("$.response.rejectedAt").doesNotExist())
                        .andExpect(jsonPath("$.response.review").doesNotExist())
                ;
            } else if (i == 6) {
                result.andExpect(jsonPath("$.response.requestMessage").doesNotExist())
                        .andExpect(jsonPath("$.response.completedAt").doesNotExist())
                        .andExpect(jsonPath("$.response.rejectMessage", is("No stock")))
                        .andExpect(jsonPath("$.response.rejectedAt").exists())
                        .andExpect(jsonPath("$.response.review").doesNotExist())
                ;
            } else {
                result.andExpect(jsonPath("$.response.requestMessage").doesNotExist())
                        .andExpect(jsonPath("$.response.completedAt").doesNotExist())
                        .andExpect(jsonPath("$.response.rejectMessage").doesNotExist())
                        .andExpect(jsonPath("$.response.rejectedAt").doesNotExist())
                        .andExpect(jsonPath("$.response.review").doesNotExist())
                ;
            }
        }
    }

    @Test
    @Order(7)
    @DisplayName("주문 조회 실패 테스트 (토큰이 올바르지 않을 경우)")
    void findByIdFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/orders/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(jwtTokenConfigure.getHeader(), "Bearer " + randomAlphanumeric(60))
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
    @Order(8)
    @WithMockJwtAuthentication
    @DisplayName("접수 상태 변경 성공 테스트")
    void acceptSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/api/orders/1/accept")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("accept"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)))
        ;
        result = mockMvc.perform(
                get("/api/orders/1")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.seq", is(1)))
                .andExpect(jsonPath("$.response.state", is("ACCEPTED")))
        ;
    }

    @Test
    @Order(9)
    @WithMockJwtAuthentication
    @DisplayName("접수 상태 변경 실패 테스트 (변경 불가 상태)")
    void acceptFailureTest() throws Exception {
        for (int i = 2; i < 7; i++) {
            ResultActions result = mockMvc.perform(
                    patch("/api/orders/" + i + "/accept")
                            .accept(MediaType.APPLICATION_JSON)
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(OrderRestController.class))
                    .andExpect(handler().methodName("accept"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response", is(false)))
            ;
            result = mockMvc.perform(
                    get("/api/orders/" + i)
                            .accept(MediaType.APPLICATION_JSON)
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(OrderRestController.class))
                    .andExpect(handler().methodName("findById"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response.seq", is(i)))
                    .andExpect(jsonPath("$.response.state", is(stateForOrderId.get(i))))
            ;
        }
    }

    @Test
    @Order(10)
    @WithMockJwtAuthentication
    @DisplayName("배송 상태 변경 성공 테스트")
    void shippingSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/api/orders/2/shipping")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("shipping"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)))
        ;
        result = mockMvc.perform(
                get("/api/orders/2")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.seq", is(2)))
                .andExpect(jsonPath("$.response.state", is("SHIPPING")))
        ;
    }

    @Test
    @Order(11)
    @WithMockJwtAuthentication
    @DisplayName("배송 상태 변경 실패 테스트 (변경 불가 상태)")
    void shippingFailureTest() throws Exception {
        for (int i = 3; i < 8; i++) {
            ResultActions result = mockMvc.perform(
                    patch("/api/orders/" + i + "/shipping")
                            .accept(MediaType.APPLICATION_JSON)
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(OrderRestController.class))
                    .andExpect(handler().methodName("shipping"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response", is(false)))
            ;
            result = mockMvc.perform(
                    get("/api/orders/" + i)
                            .accept(MediaType.APPLICATION_JSON)
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(OrderRestController.class))
                    .andExpect(handler().methodName("findById"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response.seq", is(i)))
                    .andExpect(jsonPath("$.response.state", is(stateForOrderId.get(i))))
            ;
        }
    }


    @Test
    @Order(12)
    @WithMockJwtAuthentication
    @DisplayName("완료 상태 변경 성공 테스트")
    void completeSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/api/orders/3/complete")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("complete"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)))
        ;
        result = mockMvc.perform(
                get("/api/orders/3")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.seq", is(3)))
                .andExpect(jsonPath("$.response.state", is("COMPLETED")))
                .andExpect(jsonPath("$.response.completedAt").exists())
        ;
    }

    @Test
    @Order(13)
    @WithMockJwtAuthentication
    @DisplayName("완료 상태 변경 실패 테스트 (변경 불가 상태)")
    void completeFailureTest() throws Exception {
        for (int i = 4; i < 8; i++) {
            ResultActions result = mockMvc.perform(
                    patch("/api/orders/" + i + "/complete")
                            .accept(MediaType.APPLICATION_JSON)
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(OrderRestController.class))
                    .andExpect(handler().methodName("complete"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response", is(false)))
            ;
            result = mockMvc.perform(
                    get("/api/orders/" + i)
                            .accept(MediaType.APPLICATION_JSON)
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(OrderRestController.class))
                    .andExpect(handler().methodName("findById"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response.seq", is(i)))
                    .andExpect(jsonPath("$.response.state", is(stateForOrderId.get(i))))
            ;
        }
    }

    @Test
    @Order(14)
    @WithMockJwtAuthentication
    @DisplayName("거절 상태 변경 성공 테스트")
    void rejectSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/api/orders/7/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"this is reject message!\"}")
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("reject"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)))
        ;
        result = mockMvc.perform(
                get("/api/orders/7")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.seq", is(7)))
                .andExpect(jsonPath("$.response.state", is("REJECTED")))
                .andExpect(jsonPath("$.response.rejectMessage", is("this is reject message!")))
                .andExpect(jsonPath("$.response.rejectedAt").exists())
        ;
    }

    @Test
    @Order(15)
    @WithMockJwtAuthentication
    @DisplayName("거절 상태 변경 실패 테스트 (거절 메시지 누락)")
    void rejectFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/api/orders/7/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(OrderRestController.class))
                .andExpect(handler().methodName("reject"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
        ;
    }

    @Test
    @Order(16)
    @WithMockJwtAuthentication
    @DisplayName("거절 상태 변경 실패 테스트 (변경 불가 상태)")
    void rejectFailureTest2() throws Exception {
        for (int i = 2; i < 7; i++) {
            ResultActions result = mockMvc.perform(
                    patch("/api/orders/" + i + "/reject")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content("{\"message\":\"this is reject message!\"}")
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(OrderRestController.class))
                    .andExpect(handler().methodName("reject"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response", is(false)))
            ;
            result = mockMvc.perform(
                    get("/api/orders/" + i)
                            .accept(MediaType.APPLICATION_JSON)
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(OrderRestController.class))
                    .andExpect(handler().methodName("findById"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response.seq", is(i)))
            ;
            if (i == 2) {
                result.andExpect(jsonPath("$.response.state", anyOf(is("ACCEPTED"), is("SHIPPING"))));
            } else if (i == 3) {
                result.andExpect(jsonPath("$.response.state", anyOf(is("SHIPPING"), is("COMPLETED"))));
            } else {
                result.andExpect(jsonPath("$.response.state", is(stateForOrderId.get(i))));
            }
        }
    }
}