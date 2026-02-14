@param ru.mentee.power.crm.spring.entity.Lead lead

@template.layout.main(
content = @`
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Создать Deal для лида${" \"" + lead.getName() + "\""}</h1>

        <form action="/jpa/deals/convert" method="post" class="space-y-4">

            <!-- Lead info -->
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-1">
                    Лид: ${lead.getName()} (${lead.getEmail()})
                </label>
                <p class="text-sm text-gray-500">leadId = ${lead.getId().toString()}</p>
                <input type="hidden" name="leadId" value="${lead.getId().toString()}"/>
            </div>

            <!-- Amount -->
            <div class="mb-4">
                <label for="amount" class="block text-sm font-medium text-gray-700 mb-1">
                    Сумма сделки
                </label>
                <input
                        type="text"
                        id="amount"
                        name="amount"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="50000"
                />
            </div>

            <!-- Submit Button -->
            <button
                    type="submit"
                    class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500"
            >
                Создать сделку
            </button>

            <!-- Cancel Link -->
            <a
                    href="/jpa/leads"
                    class="block text-center text-sm text-gray-600 hover:text-gray-900"
            >
                Отмена
            </a>
        </form>
    </div>
`
)
