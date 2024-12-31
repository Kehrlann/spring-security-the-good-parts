package wf.garnier.spring.security.thegoodparts;

import java.io.IOException;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlPasswordInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class EventsTests {

    @Autowired
    MockMvcTester mvc;

    @Autowired
    WebClient webClient;

    @Test
    void httpBasicLogin(CapturedOutput capturedOutput) throws Exception {
        mvc.get()
                .uri("/private")
                .with(httpBasic("user", "password"))
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK);

        assertThat(capturedOutput).contains("🎉 [UsernamePasswordAuthenticationToken] user");
    }

    @Test
    void formLogin(CapturedOutput capturedOutput) throws IOException {
        HtmlPage page = webClient.getPage("/private");

        page.<HtmlInput>querySelector("#username").type("daniel");
        page.<HtmlPasswordInput>querySelector("#password").type("some random password");
        page.<HtmlButton>querySelector("button").click();

        assertThat(capturedOutput).contains("🎉 [UsernamePasswordAuthenticationToken] daniel");
    }

    @Test
    void robotLogin(CapturedOutput capturedOutput) {
        mvc.get()
                .uri("/private")
                .header("x-robot-password", "beep-boop")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK);

        assertThat(capturedOutput).contains("🎉 [RobotAuthenticationToken] Ms Robot 🤖");
    }

}
