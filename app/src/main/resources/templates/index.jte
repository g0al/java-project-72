@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@param BasePage page

@template.layout.page(
content = @`
    @if(page != null && page.getFlash() != null)
        <p>${page.getFlash()}</p>
    @endif
    <h1 class="text-body-emphasis">Анализатор страниц</h1>
    <p>Добро пожаловать!</p>

    <form action="${NamedRoutes.urlsPath()}" method="post">
        Введите URL:
        <input type="text" required name="name">
        <input type="submit">
    </form>
    <p>Пример: https://www.example.com</p>
`
)
