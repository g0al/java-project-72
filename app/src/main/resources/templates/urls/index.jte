@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes
@import java.util.Collections
@import java.util.Comparator
@param UrlsPage page

@template.layout.page(
content = @`
    @if(page != null && page.getFlash() != null)
        <p>${page.getFlash()}</p>
    @endif
    <h1>Urls List</h1>
    @if(page.getUrls().isEmpty())
        <p>Url list is empty</p>
    @else
        <div>
                <table>
                    <tr>
                        <td>ID</td>
                        <td>URL</td>
                        <td>Код ответа</td>
                        <td>Последняя проверка</td>
                    </tr>
        @for(var url : page.getUrls())
            !{var urlCheck = page.getUrlChecks().stream()
                          .filter(a -> a.getUrlId() == url.getId())
                          .max((o1, o2) -> Long.compare(o1.getId(), o2.getId()))
                          .orElse(null);}
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                        <td>
                            @if(urlCheck != null)
                                ${urlCheck.getStatusCode()}
                            @endif
                        </td>
                        <td>
                            @if(urlCheck != null)
                                ${urlCheck.getCreatedAt().toString()}
                            @endif
                        </td>
                    </tr>
        @endfor
                </table>
            </div>
    @endif
`
)
