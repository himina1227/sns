package com.example.sns.controller;

import com.example.sns.controller.request.UserJoinRequest;
import com.example.sns.controller.request.UserLoginRequest;
import com.example.sns.exception.ErrorCode;
import com.example.sns.model.User;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Test
    public void 회원가입() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.join(userName, password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(new UserJoinRequest("name", "password"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_이미_회원가입된_userName으로_회원가입을_하는경우_에러반환() throws Exception {
        String userName = "himina1227";
        String password = "test123!@#";

        when(userService.join(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "유저이름이 중복되었습니다."));
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception {
        String userName = "himina1227";
        String password = "test123!@#";

        when(userService.login(userName, password)).thenReturn("test_token");
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(mapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입이안된_userName을_입력할경우_에러반환() throws Exception {
        String userName = "himina1227";
        String password = "test123!@#";

        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(mapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isNotFound());
//        .andExpect(status().is(ErrorCode.USER_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithAnonymousUser
    public void 로그인시_비밀번호가_다르면_에러발생() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(new UserLoginRequest("name", "password"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
