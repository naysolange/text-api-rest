package org.naysolange.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.naysolange.entity.Text;
import org.naysolange.repository.TextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TextRepository repository;

    private ResponseEntity<List<Text>> response;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void shouldReturnItsAlive() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/", String.class))
                .contains("It's alive!");
    }

    @Test
    public void shouldReturnTwoTexts() {
        givenATableWithTexts();
        whenGetTexts();
        thenReturnTwoTextsSuccessfully();
    }

    @Test
    public void shouldReturnNoContentStatus() {
        whenGetTexts();
        thenReturnNoContentStatus();
    }

    private void givenATableWithTexts() {
        repository.save(new Text("Texto 1"));
        repository.save(new Text("Texto 2"));
        repository.save(new Text("Texto 3"));
    }

    private void whenGetTexts() {
        this.response = restTemplate.exchange(
                "http://localhost:" + port + "/{amount}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Text>>() {},
                2
        );
    }

    private void thenReturnTwoTextsSuccessfully() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    private void thenReturnNoContentStatus() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
