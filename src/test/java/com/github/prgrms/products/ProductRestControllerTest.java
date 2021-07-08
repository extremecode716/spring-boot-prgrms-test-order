package com.github.prgrms.products;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductRestControllerTest {

    private static final Map<Integer, String> productNameForProductId = new HashMap<Integer, String>() {{
        put(1, "Product A");
        put(2, "Product B");
        put(3, "Product C");
    }};

    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("상품 목록 조회 테스트")
    void findAllSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/products")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("findAll"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response.length()", is(3)))
                .andExpect(jsonPath("$.response[0].seq", is(3)))
                .andExpect(jsonPath("$.response[0].name", is("Product C")))
                .andExpect(jsonPath("$.response[0].details", is("Very good product")))
                .andExpect(jsonPath("$.response[0].reviewCount").exists())
                .andExpect(jsonPath("$.response[0].reviewCount").isNumber())
                .andExpect(jsonPath("$.response[1].seq", is(2)))
                .andExpect(jsonPath("$.response[1].name", is("Product B")))
                .andExpect(jsonPath("$.response[1].details", is("Almost sold out!")))
                .andExpect(jsonPath("$.response[1].reviewCount").exists())
                .andExpect(jsonPath("$.response[1].reviewCount").isNumber())
                .andExpect(jsonPath("$.response[2].seq", is(1)))
                .andExpect(jsonPath("$.response[2].name", is("Product A")))
                .andExpect(jsonPath("$.response[2].reviewCount").exists())
                .andExpect(jsonPath("$.response[2].reviewCount").isNumber())
        ;
    }

    @Test
    @DisplayName("상품 조회 성공 테스트")
    void findByIdSuccessTest() throws Exception {
        for (int i = 1; i < 4; i++) {
            ResultActions result = mockMvc.perform(
                    get("/api/products/" + i)
                            .accept(MediaType.APPLICATION_JSON)
            );
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(ProductRestController.class))
                    .andExpect(handler().methodName("findById"))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response.seq", is(i)))
                    .andExpect(jsonPath("$.response.name", is(productNameForProductId.get(i))))
                    .andExpect(jsonPath("$.response.reviewCount").exists())
                    .andExpect(jsonPath("$.response.reviewCount").isNumber())
            ;
        }
    }

    @Test
    @DisplayName("상품 조회 실패 테스트 (존재하지 않는 ID)")
    void findByIdFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/products/100")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ProductRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(404)))
        ;
    }

}