package toby.spring.inha.refactor.learningTst.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import toby.spring.inha.refactor.learningTst.mockito.UserController;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    /**
    @Test
    @DisplayName("사용자 목록 조회")
    public void getUserList() throws Exception {

        doReturn(userList()).when(userService).findAll();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/user/list")
        );

        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        new Gson().fromJson(mvcResult.getResponse().getContentAsString(), Object.class)
    }
    */
}
