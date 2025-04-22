@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes
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
            <table class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>URL</th>
                    <th>Creation date</th>
                    <th>Last check</th>
                    <th>Response code</th>
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
`
)
