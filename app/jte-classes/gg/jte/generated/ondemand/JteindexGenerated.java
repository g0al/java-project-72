package gg.jte.generated.ondemand;
import hexlet.code.dto.MainPage;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,3,3,5,5,8,8,10,10,11,11,12,12,12,13,13,14,14,14,15,15,15,16,16,16,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, MainPage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <h1 class=\"text-body-emphasis\">Hi Hexlet!</h1>\r\n    <p>Javalin + jte</p>\r\n    ");
				if (!page.isVisited()) {
					jteOutput.writeContent("\r\n        This message is showed just once. To see it again, remove cookies\r\n    ");
				}
				jteOutput.writeContent("\r\n    ");
				if (page.getCurrentUser() != null) {
					jteOutput.writeContent("\r\n        Welcome, ");
					jteOutput.setContext("html", null);
					jteOutput.writeUserContent(page.getCurrentUser());
					jteOutput.writeContent(". if you want to sign out remove cookie called JSESSIONID\r\n    ");
				}
				jteOutput.writeContent("\r\n    <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(page.getDb());
				jteOutput.writeContent("</p>\r\n");
			}
		}, null);
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		MainPage page = (MainPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
