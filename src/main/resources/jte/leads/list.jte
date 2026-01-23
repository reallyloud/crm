@import ru.mentee.power.crm.model.Lead
@import ru.mentee.power.crm.model.LeadStatus
@import java.util.List
@param List<Lead> leads
@param LeadStatus currentFilter
@param String search

@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-4">Lead List</h2>
        <form method="get" action="/leads" class="bg-white shadow rounded-lg p-4 mb-6">
            <input type="text" name="search" value="${search}">
            <label>
                <select name="status">
                    <option value="">По статусу</option>
                    <option value="NEW">Новый</option>
                    <option value="CONTACTED">Связались</option>
                    <option value="QUALIFIED">Готов к сделке</option>
                </select>
            </label>
            <button type="submit" class="px-4 py-0.6 rounded bg-blue-500 text-white">искать</button>
        </form>
        <a
                href="/leads/new"
                class="bg-green-500 text-white px-4 py-0.9 rounded hover:bg-green-600"
        >
            + Добавить лида
        </a>

        <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-100">
            <tr>
                <th class="px-4 py-2 text-left">Name</th>
                <th class="px-4 py-2 text-left">Email</th>
                <th class="px-4 py-2 text-left">Phone</th>
                <th class="px-4 py-2 text-left">Company</th>
                <th class="px-4 py-2 text-left">Status</th>
                <th class="px-4 py-2 text-left"></th>
            </tr>
            </thead>
            <tbody>
            @for(var lead : leads)
                <tr class="border-t hover:bg-gray-50">
                    <td class="px-4 py-2">${lead.name()}</td>
                    <td class="px-4 py-2">${lead.email()}</td>
                    <td class="px-4 py-2">${lead.phone()}</td>
                    <td class="px-4 py-2">${lead.company()}</td>
                    <td class="px-4 py-2">
                            <span class="px-2 py-1 rounded text-sm bg-green-100 text-green-800">
                                ${lead.status()}
                            </span>
                    </td>
                    <td class="text-blue-600 hover:underline">
                        <a
                                href="/leads/${lead.id().toString()}/edit"
                                class="bg-green-500 text-white px-4 py-0.6 rounded hover:bg-green-600"
                        >
                            Редактировать
                        </a>

                        <a
                                href="deals/convert/${lead.id().toString()}"
                                class="bg-orange-500 text-white px-4 py-0.6 rounded hover:bg-green-600"
                        >
                            Создать deal
                        </a>
                        <form method="post" action="/leads/${lead.id().toString()}/delete" class="inline"
                              onclick="return confirm('Удалить лида ${lead.email()}?')">
                            <button type="submit" class="bg-red-600 text-white px-3 py-0.4 rounded hover:bg-red-700">
                                Удалить
                            </button>
                        </form>
                    </td>
                </tr>

            @endfor
            </tbody>
        </table>
    </div>
`)