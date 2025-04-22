@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.UrlPage
@param UrlPage page

@template.layout.page(
content = @`
    @if(page != null && page.getFlash() != null)
        <p>${page.getFlash()}</p>
    @endif
    <h1>Сайт ${page.getUrl().getName()}</h1>
    <div>
        <table class="table">
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${page.getUrl().getCreatedAt().toString()}</td>
            </tr>
        </table>
    </div>

    <h1>Проверки</h1>
    <form action="${NamedRoutes.checkUrlPath(page.getUrl().getId())}" method="post">
        Запустить проверку
        <input type="submit">
    </form>
    @if(page.getUrlChecks().isEmpty())
        <div>
            <p>Проверок пока не было</p>
        </div>
    @else
            <div>
                <table class="table">
                    <tr>
                        <td>ID</td>
                        <td>Код ответа</td>
                        <td>title</td>
                        <td>h1</td>
                        <td>description</td>
                        <td>Дата проверки</td>
                    </tr>
        @for(var lastCheck : page.getUrlChecks())
                    <tr>
                        <td>${lastCheck.getId()}</td>
                        <td>${lastCheck.getStatusCode()}</td>
                        <td>${lastCheck.getTitle()}</td>
                        <td>${lastCheck.getH1()}</td>
                        <td>${lastCheck.getDescription()}</td>
                        <td>${lastCheck.getCreatedAt().toString()}</td>
                    </tr>
                </table>
            </div>
        @endfor
    @endif
`
)