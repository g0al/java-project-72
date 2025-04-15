package hexlet.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URL;
import java.net.URI;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.NotFoundResponse;
import io.javalin.rendering.template.JavalinJte;

import static io.javalin.rendering.template.TemplateUtil.model;

import hexlet.code.repository.BaseRepository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Slf4j
public class App {

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();

        app.start(getPort());
    }

    public static Javalin getApp() throws IOException, SQLException {

        String db = System.getenv().getOrDefault("JDBC_DATABASE_URL", "Hikari");
        if (!db.startsWith("jdbc:postgresql")) {
            var hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");

            var dataSource = new HikariDataSource(hikariConfig);
            var sql = readResourceFile("schema.sql");

            try (var connection = dataSource.getConnection();
                 var statement = connection.createStatement()) {
                statement.execute(sql);
            }
            BaseRepository.dataSource = dataSource;
        }

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

        app.get("/", ctx -> {
            var page = new BasePage();
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("index.jte", model("page", page));
        });

        app.post(NamedRoutes.checkUrlPath("{id}"), ctx -> {
            var id = ctx.pathParamAsClass("id", Long.class).get();

            var urlCheck = new UrlCheck(id);
            String url = "";
            if (UrlRepository.find(id).isPresent()) {
                url = UrlRepository.find(id).get().getName();
            }
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
            urlCheck.setStatusCode(jsonResponse.getStatus());

            if (jsonResponse.getStatus() == 200) {
                Document doc = Jsoup.connect(url).get();

                String title = doc.title();
                urlCheck.setTitle(title);

                Element h1 = doc.selectFirst("h1");
                if (h1 != null) {
                    urlCheck.setH1(h1.text());
                }

                Element metaDescription = doc.selectFirst("meta[name=description]");
                if (metaDescription != null) {
                    String content = metaDescription.attr("content");
                    urlCheck.setDescription(content);
                }
            }
            UrlCheckRepository.save(urlCheck);
            ctx.redirect(NamedRoutes.urlPath(id));
        });

        app.get(NamedRoutes.urlsPath(), ctx -> {
            List<Url> urls;
            List<UrlCheck> urlChecks;
            urls = UrlRepository.getEntities();
            urlChecks = UrlCheckRepository.getEntities();
            var page = new UrlsPage(urls, urlChecks);
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("urls/index.jte", model("page", page));
        });

        app.get(NamedRoutes.urlPath("{id}"), ctx -> {
            var id = ctx.pathParamAsClass("id", Long.class).get();
            var url = UrlRepository.find(id)
                    .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));
            List<UrlCheck> urlChecks = List.of();
            if (!UrlCheckRepository.find(id).isEmpty()) {
                urlChecks = UrlCheckRepository.find(id);
            }
            var page = new UrlPage(url, urlChecks);
            ctx.render("urls/show.jte", model("page", page));
        });

        app.post(NamedRoutes.urlsPath(), ctx -> {
            try {
                var name = ctx.formParam("name");
                String finalName;
                URI uri = new URI(name);
                URL urlFromUri = uri.toURL();
                var protocol = urlFromUri.getProtocol();
                var host = urlFromUri.getHost();
                var port = urlFromUri.getPort();
                name = protocol + "://" + host;
                if (port != -1) {
                    finalName = name + ":" + port;
                } else {
                    finalName = name;
                }

                var url = new Url(finalName);

                if (UrlRepository.search(name).isEmpty()) {
                    UrlRepository.save(url);
                    ctx.sessionAttribute("flash", "Страница успешно добавлена");
                    ctx.redirect(NamedRoutes.urlsPath());
                } else {
                    ctx.sessionAttribute("flash", "Страница уже существует");
                    ctx.redirect(NamedRoutes.urlsPath());
                }
            } catch (Exception e) {
                ctx.sessionAttribute("flash", "Ошибка при вводе адреса");
                ctx.redirect(NamedRoutes.indexPath());
            }
        });

        return app;
    }
}
