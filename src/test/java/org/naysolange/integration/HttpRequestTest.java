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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TextRepository repository;

    private ResponseEntity<List<Text>> getResponse;

    private ResponseEntity<Text> postResponse;

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
    public void shouldReturnOkAndTwoTexts() {
        givenATableWithTexts();
        whenGetTexts();
        thenReturnTwoRandomTextsWithOkStatus();
    }

    @Test
    public void shouldReturnNoContent() {
        whenGetTexts();
        thenReturnNoContentStatus();
    }

    @Test
    public void shouldSaveTextAndReturnCreated() {
        whenSaveText(new Text("A new text"));
        thenSaveTextAndReturnCreatedStatus();
    }

    @Test
    public void shouldNotSaveTextAndReturnBadRequestWhenContentIsBlank() {
        whenSaveText(new Text(""));
        thenNotSaveTextAndReturnBadRequestStatus();
    }

    @Test
    public void shouldNotSaveTextAndReturnBadRequestWhenContentIsNull() {
        whenSaveText(new Text(null));
        thenNotSaveTextAndReturnBadRequestStatus();
    }

    private void givenATableWithTexts() {
        repository.save(new Text("Text 1"));
        repository.save(new Text("Text 2"));
        repository.save(new Text("Text 3"));
    }

    private void whenGetTexts() {
        this.getResponse = restTemplate.exchange(
                "http://localhost:" + port + "/{amount}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Text>>() {},
                2
        );
    }

    private void thenReturnTwoRandomTextsWithOkStatus() {
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).hasSize(2);
    }

    private void thenReturnNoContentStatus() {
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void whenSaveText(Text text) {
        HttpEntity<Text> request = new HttpEntity<>(text);

        this.postResponse = restTemplate.exchange(
                "http://localhost:" + port + "/",
                HttpMethod.POST,
                request,
                Text.class
        );
    }

    private void thenSaveTextAndReturnCreatedStatus() {
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Text createdText = Objects.requireNonNull(postResponse.getBody());
        assertThat(createdText.getContent()).isEqualTo("A new text");
        assertThat(createdText.getId()).isNotNull();
    }

    private void thenNotSaveTextAndReturnBadRequestStatus() {
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        whenGetTexts();
        assertThat(getResponse.getBody()).isNullOrEmpty();
    }
}
