package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void check(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();

        var url = UrlRepository.findById(urlId);
        if (!url.isPresent()) {
            ctx.status(404).result("URL not found");
            return;
        }

        try {
            HttpResponse<String> response = Unirest.get(url.get().getName()).asString();
            UrlCheck check = parseHtml(urlId, response.getBody());
            UrlCheckRepository.save(check);
            ctx.redirect(NamedRoutes.urlPath(urlId));

        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Invalid URL.");
            ctx.redirect(NamedRoutes.urlPath(urlId));
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static UrlCheck parseHtml(Long urlId, String html) {
        Document doc = Jsoup.parse(html);
        String title = doc.title();
        Element h1Element = doc.selectFirst("h1");
        String h1 = h1Element != null ? h1Element.text() : null;

        Element metaDescriptionElement = doc.selectFirst("meta[name=description]");
        String metaDescription = metaDescriptionElement != null ? metaDescriptionElement
                .attr("content") : null;
        return new UrlCheck(urlId, 200, title, h1, metaDescription);
    }

    public static void index(Context ctx) throws SQLException {
        List<Url> urls;
        Map<Long, UrlCheck> urlChecks;
        urls = UrlRepository.getEntities();
        urlChecks = UrlCheckRepository.getLastChecks();
        var page = new UrlsPage(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));
        List<UrlCheck> urlChecks = List.of();
        if (!UrlCheckRepository.getChecksForUrl(id).isEmpty()) {
            urlChecks = UrlCheckRepository.getChecksForUrl(id);
        }
        var page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = ctx.formParam("url");
        URL parsedUrl;
        try {
            var uri = new URI(inputUrl);
            parsedUrl = uri.toURL();
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.indexPath());
            return;
        }

        String normalizedUrl = String
                .format(
                        "%s://%s%s",
                        parsedUrl.getProtocol(),
                        parsedUrl.getHost(),
                        parsedUrl.getPort() == -1 ? "" : ":" + parsedUrl.getPort()
                )
                .toLowerCase();

        Url url = UrlRepository.findByName(normalizedUrl).orElse(null);

        if (url != null) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "info");
        } else {
            Url newUrl = new Url(normalizedUrl);
            UrlRepository.save(newUrl);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");
        }

        ctx.redirect("/urls", HttpStatus.forStatus(302));
    }
}
