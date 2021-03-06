package net.madvirus.eval.web.security;

import net.madvirus.eval.testhelper.AbstractIntTest;
import net.madvirus.eval.testhelper.AuthCookieHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class AccessTest extends AbstractIntTest {
    // http://stackoverflow.com/questions/23335200/spring-boot-setup-security-for-testing
    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(filterChainProxy).build();
    }

    @Test
    public void anonymousUser_Access_Main_shouldRedirectLoginForm() throws Exception {
        mockMvc.perform(get("/main").contextPath(""))
                .andDo(print())
                .andExpect(redirectedUrlPattern("**/loginForm"));
    }

    @Test
    public void authUser_Access_Main_shouldAccessMain() throws Exception {
        mockMvc.perform(get("/main").cookie(AuthCookieHelper.authCookie("ratee11")))
                .andExpect(status().isOk());
    }

    @Test
    public void notGrantedUser_Access_Admin_shouldResponseForbidden() throws Exception {
        mockMvc.perform(get("/admin").cookie(AuthCookieHelper.authCookie("ratee11")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void userWithSystemAdminRole_Access_Admin_shouldAccess() throws Exception {
        mockMvc.perform(get("/admin").cookie(AuthCookieHelper.authCookie("systemadmin")))
                .andExpect(status().isOk());
    }

}
