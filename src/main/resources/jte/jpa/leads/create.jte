@param ru.mentee.power.crm.spring.controller.LeadForm lead

@template.layout.main(
content = @`
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Добавить нового лида </h1>

        <form action="/jpa/leads" method="post" class="space-y-4">
            <!-- Name -->
            <div>
                <label for="name" class="block text-sm font-medium text-gray-700 mb-1">
                    Имя
                </label>
                <input
                        type="text"
                        id="name"
                        name="name"
                        value="${lead.name()}"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="Иван"
                />
            </div>
            <!-- Phone -->
            <div>
                <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">
                    Телефон
                </label>
                <input
                        type="text"
                        id="phone"
                        name="phone"
                        value="${lead.phone()}"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="+7 (999) 999-99-99"
                />
            </div>
            <!-- Email -->
            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
                    Email
                </label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        value="${lead.email()}"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="example@company.com"
                />
            </div>
            <!-- Company -->
            <div>
                <label for="companyName" class="block text-sm font-medium text-gray-700 mb-1">
                    Компания
                </label>
                <input
                        type="text"
                        id="companyName"
                        name="companyName"
                        value="${lead.companyName()}"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="Яндекс"
                />
            </div>
            <!-- Select Status -->
            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-1">
                    Статус
                </label>
                <select name="status" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                    <option value="NEW">Новый</option>
                    <option value="CONTACTED">Связались</option>
                    <option value="QUALIFIED">Готов к сделке</option>
                </select>
            </div>
            <!-- Submit Button -->
            <button
                    type="submit"
                    class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500"
            >
                Создать лида
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
