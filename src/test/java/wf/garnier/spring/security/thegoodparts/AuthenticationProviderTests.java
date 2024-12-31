package wf.garnier.spring.security.thegoodparts;

import java.io.IOException;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlPasswordInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationProviderTests {

    @Autowired
    MockMvcTester mvc;

    @Autowired
    WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient.getCookieManager().clearCookies();
    }

    @Test
    void httpBasicDaniel() {
        mvc.get()
                .uri("/private")
                .with(httpBasic("daniel", "foobar"))
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK);
    }

    @Test
    void httpBasicUser() {
        mvc.get()
                .uri("/private")
                .with(httpBasic("user", "password"))
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK);
    }

    @Test
    void httpBasicUserWrongPassword() {
        mvc.get()
                .uri("/private")
                .with(httpBasic("user", "foobar"))
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void formLoginDaniel() throws IOException {
        HtmlPage page = webClient.getPage("/private");

        page.<HtmlInput>querySelector("#username").type("daniel");
        page.<HtmlPasswordInput>querySelector("#password").type("some random password");
        page = page.<HtmlButton>querySelector("button").click();

        assertThat(page.getBody().getTextContent()).contains("daniel");
    }

    @Test
    void formLoginUser() throws IOException {
        HtmlPage page = webClient.getPage("/private");

        page.<HtmlInput>querySelector("#username").type("user");
        page.<HtmlPasswordInput>querySelector("#password").type("password");
        page = page.<HtmlButton>querySelector("button").click();

        assertThat(page.getBody().getTextContent()).contains("user");
    }

    @Test
    void formLoginUserWrongPassword() throws IOException {
        HtmlPage page = webClient.getPage("/private");

        page.<HtmlInput>querySelector("#username").type("user");
        page.<HtmlPasswordInput>querySelector("#password").type("some random password");
        page = page.<HtmlButton>querySelector("button").click();

        assertThat(page.getUrl()).hasPath("/login").hasParameter("error");
        assertThat(page.getBody().getTextContent()).contains("Invalid credentials");
    }

}
