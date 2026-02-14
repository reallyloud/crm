@param ru.mentee.power.crm.spring.controller.LeadForm lead
@param org.springframework.validation.BindingResult errors

@template.layout.main(
content = @`
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Добавить нового лида (JPA)</h1>

        <form action="/jpa/leads" method="post" class="space-y-4">

            <!-- Name -->
            <div class="mb-4">
                <label for="name" class="block text-sm font-medium text-gray-700 mb-1">
                    Имя
                </label>
                <input
                        type="text"
                        id="name"
                        name="name"
                        value="${lead.name()}"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="Иван"
                />
                @if(errors != null && errors.hasFieldErrors("name"))
                    <p class="text-red-600 text-sm mt-1">
                        ${errors.getFieldError("name").getDefaultMessage()}
                    </p>
                @endif
            </div>

            <!-- Phone -->
            <div class="mb-4">
                <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">
                    Телефон
                </label>
                <input
                        type="text"
                        id="phone"
                        name="phone"
                        value="${lead.phone()}"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="+785936794"
                />
                @if(errors != null && errors.hasFieldErrors("phone"))
                    <p class="text-red-600 text-sm mt-1">
                        ${errors.getFieldError("phone").getDefaultMessage()}
                    </p>
                @endif
            </div>

            <!-- Email -->
            <div class="mb-4">
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
                    Email
                </label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        value="${lead.email()}"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="example@company.com"
                />
                @if(errors != null && errors.hasFieldErrors("email"))
                    <p class="text-red-600 text-sm mt-1">
                        ${errors.getFieldError("email").getDefaultMessage()}
                    </p>
                @endif
            </div>

            <!-- Company -->
            <div class="mb-4">
                <label for="companyName" class="block text-sm font-medium text-gray-700 mb-1">
                    Компания
                </label>
                <input
                        type="text"
                        id="companyName"
                        name="companyName"
                        value="${lead.companyName()}"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="Компания"
                />
            </div>

            <!-- Select Status -->
            <div class="mb-4">
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
