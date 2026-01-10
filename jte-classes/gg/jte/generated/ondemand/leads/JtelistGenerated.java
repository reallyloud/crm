package gg.jte.generated.ondemand.leads;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import java.util.List;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "leads/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,3,3,3,9,9,9,9,9,13,13,13,13,17,17,17,17,21,21,21,21,28,28,29,29,29,30,30,32,32,32,45,45,47,47,47,48,48,48,51,51,51,55,55,59,59,59,59,59,3,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<Lead> leads, LeadStatus currentFilter) {
		jteOutput.writeContent("\r\n<div class=\"mb-4 flex justify-between items-center\">\r\n    <div class=\"mb-4 flex gap-2\">\r\n        <a href=\"/leads\"\r\n           class=\"px-4 py-2 rounded ");
		jteOutput.setContext("a", "class");
		jteOutput.writeUserContent(currentFilter == null ? "bg-blue-500 text-white" : "bg-gray-200");
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">\r\n            Все\r\n        </a>\r\n        <a href=\"/leads?status=NEW\"\r\n           class=\"px-4 py-2 rounded ");
		jteOutput.setContext("a", "class");
		jteOutput.writeUserContent(currentFilter == LeadStatus.NEW ? "bg-blue-500 text-white" : "bg-gray-200");
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">\r\n            NEW\r\n        </a>\r\n        <a href=\"/leads?status=CONTACTED\"\r\n           class=\"px-4 py-2 rounded ");
		jteOutput.setContext("a", "class");
		jteOutput.writeUserContent(currentFilter == LeadStatus.CONTACTED ? "bg-blue-500 text-white" : "bg-gray-200");
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">\r\n            CONTACTED\r\n        </a>\r\n        <a href=\"/leads?status=QUALIFIED\"\r\n           class=\"px-4 py-2 rounded ");
		jteOutput.setContext("a", "class");
		jteOutput.writeUserContent(currentFilter == LeadStatus.QUALIFIED ? "bg-blue-500 text-white" : "bg-gray-200");
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">\r\n            QUALIFIED\r\n        </a>\r\n    </div>\r\n\r\n</div>\r\n\r\n");
		if (currentFilter != null) {
			jteOutput.writeContent("\r\n    <p class=\"text-sm text-gray-600 mb-2\">Показаны лиды со статусом: ");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(currentFilter);
			jteOutput.writeContent("</p>\r\n");
		}
		jteOutput.writeContent("\r\n\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"bg-white rounded-lg shadow-md p-6\">\r\n        <h2 class=\"text-2xl font-bold mb-4\">Lead List</h2>\r\n\r\n        <table class=\"min-w-full bg-white border border-gray-200\">\r\n            <thead class=\"bg-gray-100\">\r\n            <tr>\r\n                <th class=\"px-4 py-2 text-left\">Email</th>\r\n                <th class=\"px-4 py-2 text-left\">Company</th>\r\n                <th class=\"px-4 py-2 text-left\">Status</th>\r\n            </tr>\r\n            </thead>\r\n            <tbody>\r\n            ");
				for (var lead : leads) {
					jteOutput.writeContent("\r\n                <tr class=\"border-t hover:bg-gray-50\">\r\n                    <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.email());
					jteOutput.writeContent("</td>\r\n                    <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.company());
					jteOutput.writeContent("</td>\r\n                    <td class=\"px-4 py-2\">\r\n                            <span class=\"px-2 py-1 rounded text-sm bg-green-100 text-green-800\">\r\n                                ");
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(lead.status());
					jteOutput.writeContent("\r\n                            </span>\r\n                    </td>\r\n                </tr>\r\n            ");
				}
				jteOutput.writeContent("\r\n            </tbody>\r\n        </table>\r\n    </div>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<Lead> leads = (List<Lead>)params.get("leads");
		LeadStatus currentFilter = (LeadStatus)params.get("currentFilter");
		render(jteOutput, jteHtmlInterceptor, leads, currentFilter);
	}
}
