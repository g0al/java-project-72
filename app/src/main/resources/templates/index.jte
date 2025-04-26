@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@param BasePage page

@template.layout.page(
content = @`
    @if(page != null && page.getFlash() != null)
        <p>${page.getFlash()}</p>
    @endif<div class="container mt-5">
    <h1 class="text-body-emphasis text-center">Анализатор страниц</h1>
    <p class="text-center">Добро пожаловать!</p>

    <form action="${NamedRoutes.urlsPath()}" method="post" class="text-center">
        <div class="form-group">
            <label for="url">Введите URL:</label>
            <input type="text" class="form-control" id="url" required name="url" placeholder="Пример: https://www.example.com">
        </div>
        <button type="submit" class="btn btn-primary">Отправить</button>
    </form>
    </div>
`
)
