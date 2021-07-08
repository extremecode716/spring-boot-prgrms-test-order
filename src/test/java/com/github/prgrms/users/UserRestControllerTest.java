package com.github.prgrms.users;

import com.github.prgrms.configures.JwtTokenConfigure;
import com.github.prgrms.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
class UserRestControllerTest {

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
    @DisplayName("로그인 성공 테스트 (아이디, 비밀번호가 올바른 경우)")
    void loginSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"principal\":\"tester@gmail.com\",\"credentials\":\"1234\"}")
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.token").exists())
                .andExpect(jsonPath("$.response.token").isString())
                .andExpect(jsonPath("$.response.user.name", is("tester")))
                .andExpect(jsonPath("$.response.user.email.address", is("tester@gmail.com")))
                .andExpect(jsonPath("$.response.user.loginCount").exists())
                .andExpect(jsonPath("$.response.user.loginCount").isNumber())
                .andExpect(jsonPath("$.response.user.lastLoginAt").exists())
        ;
    }

    @Test
    @DisplayName("로그인 실패 테스트 (아이디, 비밀번호가 올바르지 않은 경우)")
    void loginFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"principal\":\"tester@gmail.com\",\"credentials\":\"4321\"}")
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)))
        ;
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("내 정보 조회 성공 테스트 (토큰이 올바른 경우)")
    void meSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users/me")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("me"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.name", is("tester")))
                .andExpect(jsonPath("$.response.email.address", is("tester@gmail.com")))
                .andExpect(jsonPath("$.response.loginCount").exists())
                .andExpect(jsonPath("$.response.loginCount").isNumber())
        ;
    }

    @Test
    @DisplayName("내 정보 조회 실패 테스트 (토큰이 올바르지 않을 경우)")
    void meFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users/me")
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

}