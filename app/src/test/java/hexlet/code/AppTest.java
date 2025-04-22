package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

public class AppTest {

    private Javalin app;
    static MockWebServer mockWebServer = new MockWebServer();
    String baseUrl = mockWebServer.url("/")
            .toString();

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @BeforeAll
    public static void startTestServer() throws IOException, SQLException {
        String testHtmlResponse = "<html><body><h1>Hello, World!</h1></body></html>";
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(testHtmlResponse);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.start();
    }

    @AfterAll
    public static void stopTestServer() throws IOException, SQLException {
        mockWebServer.shutdown();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testShow() throws SQLException {

        String testUrl = "https://example.com";
        Url urlEntity = new Url(testUrl);
        UrlRepository.save(urlEntity);
        Long id = urlEntity.getId();

        JavalinTest.test(app, (server, client) -> {

            var response = client.get("/urls");

            assertThat(response.code()).isEqualTo(200);

            String responseBody = response.body().string();
            assertThat(responseBody).contains(testUrl);
            assertThat(responseBody).contains("/urls/" + id);
        });
    }

    @Test
    void testUrlNotFound() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/9999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testGetUrlById() throws SQLException {
        var testUrl = "https://www.example.com";
        var url = new Url(testUrl);
        UrlRepository.save(url);
        Long id = url.getId();

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + id);

            assertThat(response.code()).isEqualTo(200);

            String responseBody = response.body().string();
            assertThat(responseBody).contains(testUrl);
        });
    }

    @Test
    public void testAddUrlHandlerWithInvalidUrl() {
        String invalidUrl = "invalid-url";

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + invalidUrl;

            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(200);

            List<Url> savedUrls = UrlRepository.getEntities();
            assertThat(savedUrls).noneMatch(url -> url.getName().equals(invalidUrl));
        });
    }

    @Test
    public void testAddUrlHandlerWithExistingUrl() throws SQLException {
        var existingUrl = new Url("https://www.example.com");
        UrlRepository.save(existingUrl);

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";
            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(200);

            List<Url> savedUrls = UrlRepository.getEntities();
            assertThat(savedUrls)
                    .filteredOn(url -> url.getName().equals("https://www.example.com"))
                    .hasSize(1);
        });
    }

    @Test
    public void testTests() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var url = "https://www.ok.ru";
            var requestBody = "url=" + url;
            assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

            var actualUrl = UrlRepository.findByName(url);
            assertThat(actualUrl).isNotNull();
        });
    }
}
