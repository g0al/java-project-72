package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void check(Context ctx) throws SQLException, IOException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var urlCheck = new UrlCheck(id);
        String url = "";
        if (UrlRepository.find(id).isPresent()) {
            url = UrlRepository.find(id).get().getName();
        }
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.get(url).asJson();
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.redirect(NamedRoutes.urlPath(id));
        }

        if (jsonResponse != null) {
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
        }
    }

    public static void index(Context ctx) throws SQLException {
        List<Url> urls;
        List<UrlCheck> urlChecks;
        urls = UrlRepository.getEntities();
        urlChecks = UrlCheckRepository.getEntities();
        var page = new UrlsPage(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));
        List<UrlCheck> urlChecks = List.of();
        if (!UrlCheckRepository.find(id).isEmpty()) {
            urlChecks = UrlCheckRepository.find(id);
        }
        var page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {
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
    }
}
