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

    <div class="container mt-5">
        <h1 class="text-center">Проверки</h1>

        <form action="${NamedRoutes.checkUrlPath(page.getUrl().getId())}" method="post" class="text-center mb-4">
            <button type="submit" class="btn btn-primary">Запустить проверку</button>
        </form>

        @if(page.getUrlChecks().isEmpty())
            <div class="alert alert-info text-center">
                <p>Проверок пока не было</p>
            </div>
        @else
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Код ответа</th>
                        <th>Title</th>
                        <th>H1</th>
                        <th>Description</th>
                        <th>Дата проверки</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var lastCheck : page.getUrlChecks())
                        <tr>
                            <td>${lastCheck.getId()}</td>
                            <td>${lastCheck.getStatusCode()}</td>
                            <td>${lastCheck.getTitle()}</td>
                            <td>${lastCheck.getH1()}</td>
                            <td>${lastCheck.getDescription()}</td>
                            <td>${lastCheck.getCreatedAt().toString()}</td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        @endif
    </div>
`
)