package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;
import java.sql.SQLException;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
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
    String baseUrl = mockWebServer.url("/").toString();

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
        UrlCheckRepository.removeAll();
        UrlRepository.removeAll();
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
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            Url url = new Url(baseUrl);
            UrlRepository.save(url);
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
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
    public void testUrlCheck() throws IOException, SQLException {
        var url = new Url(baseUrl);
        UrlRepository.save(url);
        var urlCheck = new UrlCheck((long) 1);
        UrlCheckRepository.save(urlCheck);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/1");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("title");
        });
    }
}
