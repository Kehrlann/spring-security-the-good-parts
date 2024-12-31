package wf.garnier.spring.security.thegoodparts;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
class ForbiddenFilterTests {

    @Autowired
    MockMvcTester mvc;

    @Test
    void noHeader() {
        mvc.get()
                .uri("/")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK);
    }

    @Test
    void withForbiddenHeader() {
        mvc.get()
                .uri("/")
                .header("x-forbidden", "true")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    void withForbiddenHeaderWrongValue() {
        mvc.get()
                .uri("/")
                .header("x-forbidden", "foobar")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK);
    }


}
