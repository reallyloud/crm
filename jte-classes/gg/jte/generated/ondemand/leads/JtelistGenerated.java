package gg.jte.generated.ondemand.leads;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "leads/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,2,2,2,2,15,15,17,17,17,18,18,18,21,21,21,25,25,29,29,29,29,29,0,0,0,0};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.List<ru.mentee.power.crm.model.Lead> leads) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"bg-white rounded-lg shadow-md p-6\">\r\n        <h2 class=\"text-2xl font-bold mb-4\">Lead List</h2>\r\n\r\n        <table class=\"min-w-full bg-white border border-gray-200\">\r\n            <thead class=\"bg-gray-100\">\r\n                <tr>\r\n                    <th class=\"px-4 py-2 text-left\">Email</th>\r\n                    <th class=\"px-4 py-2 text-left\">Company</th>\r\n                    <th class=\"px-4 py-2 text-left\">Status</th>\r\n                </tr>\r\n            </thead>\r\n            <tbody>\r\n                ");
				for (var lead : leads) {
					jteOutput.writeContent("\r\n                    <tr class=\"border-t hover:bg-gray-50\">\r\n                        <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.email());
					jteOutput.writeContent("</td>\r\n                        <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.company());
					jteOutput.writeContent("</td>\r\n                        <td class=\"px-4 py-2\">\r\n                            <span class=\"px-2 py-1 rounded text-sm bg-green-100 text-green-800\">\r\n                                ");
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(lead.status());
					jteOutput.writeContent("\r\n                            </span>\r\n                        </td>\r\n                    </tr>\r\n                ");
				}
				jteOutput.writeContent("\r\n            </tbody>\r\n        </table>\r\n    </div>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		java.util.List<ru.mentee.power.crm.model.Lead> leads = (java.util.List<ru.mentee.power.crm.model.Lead>)params.get("leads");
		render(jteOutput, jteHtmlInterceptor, leads);
	}
}
