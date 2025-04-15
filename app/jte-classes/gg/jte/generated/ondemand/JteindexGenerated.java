package gg.jte.generated.ondemand;
import hexlet.code.dto.BasePage;
import hexlet.code.util.NamedRoutes;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,4,4,6,6,7,7,8,8,8,9,9,13,13,13,13,13,13,13,13,13,19,19,19,20,20,20,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BasePage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    ");
				if (page != null && page.getFlash() != null) {
					jteOutput.writeContent("\r\n        <p>");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(page.getFlash());
					jteOutput.writeContent("</p>\r\n    ");
				}
				jteOutput.writeContent("\r\n    <h1 class=\"text-body-emphasis\">Анализатор страниц</h1>\r\n    <p>Добро пожаловать!</p>\r\n\r\n    <form");
				var __jte_html_attribute_0 = NamedRoutes.urlsPath();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" action=\"");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("form", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" method=\"post\">\r\n        Введите URL:\r\n        <input type=\"text\" required name=\"name\">\r\n        <input type=\"submit\">\r\n    </form>\r\n    <p>Пример: https://www.example.com</p>\r\n");
			}
		});
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BasePage page = (BasePage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
