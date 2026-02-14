@import ru.mentee.power.crm.spring.entity.Lead
@import ru.mentee.power.crm.model.LeadStatus
@import java.util.List
@param List<Lead> leads
@param String search = ""
@param String status = null

@template.layout.main(content = @`
    <div class="mb-4 flex justify-between items-center">
        <div class="mb-4 flex gap-2">
            <a href="/jpa/leads"
               class="px-4 py-2 rounded ${status == null ? "bg-blue-500 text-white" : "bg-gray-200"}">
                Все
            </a>
            <a href="/jpa/leads?status=NEW"
               class="px-4 py-2 rounded ${"NEW".equals(status) ? "bg-blue-500 text-white" : "bg-gray-200"}">
                NEW
            </a>
            <a href="/jpa/leads?status=CONTACTED"
               class="px-4 py-2 rounded ${"CONTACTED".equals(status) ? "bg-blue-500 text-white" : "bg-gray-200"}">
                CONTACTED
            </a>
            <a href="/jpa/leads?status=QUALIFIED"
               class="px-4 py-2 rounded ${"QUALIFIED".equals(status) ? "bg-blue-500 text-white" : "bg-gray-200"}">
                QUALIFIED
            </a>
            <a href="/jpa/leads?status=DONE"
               class="px-4 py-2 rounded ${"DONE".equals(status) ? "bg-blue-500 text-white" : "bg-gray-200"}">
                DONE
            </a>
        </div>
    </div>

    <div class="mb-4 flex justify-between items-center">

        <a href="/jpa/leads/new"
           class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
            + Добавить лида
        </a>
    </div>

    @if(status != null)
        <p class="text-sm text-gray-600 mb-2">Показаны лиды со статусом: ${status}</p>
    @endif

    <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-4">Lead List</h2>

        <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-100">
            <tr>
                <th class="px-4 py-2 text-left">Имя</th>
                <th class="px-4 py-2 text-left">Email</th>
                <th class="px-4 py-2 text-left">Телефон</th>
                <th class="px-4 py-2 text-left">Компания</th>
                <th class="px-4 py-2 text-left">Статус</th>
                <th class="px-4 py-2 text-left">Действия</th>
            </tr>
            </thead>
            <tbody>
            @for(var lead : leads)
                <tr class="border-t hover:bg-gray-50">
                    <td class="px-4 py-2">${lead.getName()}</td>
                    <td class="px-4 py-2">${lead.getEmail()}</td>
                    <td class="px-4 py-2">${lead.getPhone()}</td>
                    <td class="px-4 py-2">${lead.getCompany() != null ? lead.getCompany().getName() : ""}</td>
                    <td class="px-4 py-2">
                        <span class="px-2 py-1 rounded text-sm bg-green-100 text-green-800">
                            ${lead.getStatus()}
                        </span>
                    </td>
                    <td class="px-4 py-2 flex gap-2">
                        <a href="/jpa/leads/${lead.getId().toString()}/edit"
                           class="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600 text-sm">
                            Редактировать
                        </a>
                        <form action="/jpa/leads/${lead.getId().toString()}/delete" method="post" style="display:inline;">
                            <button type="submit" class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 text-sm">
                                Удалить
                            </button>
                        </form>
                        <a href="/jpa/deals/convert/${lead.getId().toString()}"
                           class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 text-sm">
                            Перевести в сделку
                        </a>
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`)
