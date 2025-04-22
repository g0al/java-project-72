package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class UrlsPage extends BasePage {
    private List<Url> urls;
    private Map<Long, UrlCheck> lastChecks;

    public UrlsPage(List<Url> urls, Map<Long, UrlCheck> lastChecks) {
        setUrls(urls);
        setLastChecks(lastChecks);
    }
}
