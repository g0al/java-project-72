@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes
@param UrlsPage page

@template.layout.page(
content = @`
    @if(page != null && page.getFlash() != null)
        <p>${page.getFlash()}</p>
    @endif
    <div class="container mt-5">
        <h1 class="text-center">Список URL</h1>

        @if(page.getUrls().isEmpty())
            <p class="text-center">Список URL пуст</p>
        @else
            <div class="table-responsive">
                <table class="table table-striped table-bordered table-hover">
                    <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>URL</th>
                        <th>Дата создания</th>
                        <th>Последняя проверка</th>
                        <th>Код ответа</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var url : page.getUrls())
                        <tr>
                            <td>${url.getId()}</td>
                            <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                            <td>${url.getCreatedAt().toString()}</td>

                            @if(page.getLastChecks().containsKey(url.getId()))
                                <td>${page.getLastChecks().get(url.getId()).getCreatedAt().toString()}</td>
                                <td>${page.getLastChecks().get(url.getId()).getStatusCode()}</td>
                            @else
                                <td>No data</td>
                                <td>---</td>
                            @endif
                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        @endif
    </div>
`
)
