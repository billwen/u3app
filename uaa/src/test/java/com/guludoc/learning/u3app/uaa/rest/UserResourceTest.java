package com.guludoc.learning.u3app.uaa.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@RequiredArgsConstructor
@SpringBootTest
@ActiveProfiles("dev")
public class UserResourceTest {

    private final WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser("user")
    @Test
    public void givenRoleUserOrAdmin_thenAccessSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", "user"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser(username = "zhangsan", roles = "ADMIN")
    @Test
    public void givenAdminRoleUserOrAdmin_thenAccessSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", "user"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser(username = "zhangsan", roles = "USER")
    @Test
    public void givenUnauthorizedRoleUserOrAdmin_thenAccessDenied() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", "user"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
}
