@import hexlet.code.model.UrlCheck
@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.UrlPage
@param UrlPage page

@template.layout.page(
content = @`
    <h1>${page.getUrl().getName()}</h1>
    <p>${page.getUrl().getId()}</p>
    <p>${page.getUrl().getCreatedAt().toString()}</p>
    
    <h1>Проверки доступности</h1>
    @if(page.getUrlChecks().isEmpty())
        <p>Url checks list is empty</p>
    @else
        @for(var urlCheck : page.getUrlChecks())
            <div>
                <p>${urlCheck.getId()}</p>
                <p>${urlCheck.getCreatedAt().toString()}</p>
                <p>${urlCheck.getStatusCode()}</p>
                <p>${urlCheck.getTitle()}</p>
                <p>${urlCheck.getH1()}</p>
                <p>${urlCheck.getDescription()}</p>
            </div>
        @endfor
    @endif
    <form action="${NamedRoutes.checkUrlPath(page.getUrl().getId())}" method="post">
        Проверка доступности
        <input type="submit">
    </form>
`
)