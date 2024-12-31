package wf.garnier.spring.security.thegoodparts;

import org.htmlunit.WebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
class RobotAuthenticationTests {

    @Autowired
    MockMvcTester mvc;

    @Test
    void noHeader() {
        mvc.get()
                .uri("/private")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void withRobotHeader() {
        mvc.get()
                .uri("/private")
                .header("x-robot-password", "beep-boop")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK)
                .bodyText()
                .contains("Ms Robot");
    }

    @Test
    void withRobotHeaderWrongSecret() {
        mvc.get()
                .uri("/private")
                .header("x-robot-password", "beep-beep")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.FORBIDDEN);
    }

}
