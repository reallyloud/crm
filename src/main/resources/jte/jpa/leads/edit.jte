@param ru.mentee.power.crm.spring.controller.LeadForm lead
@param java.util.UUID leadId

<!DOCTYPE html>
<html>
<head>
    <title>Редактирование лида </title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-6">
    <h1 class="text-2xl font-bold mb-4">Редактирование лида </h1>
    <form method="post" action="/jpa/leads/${leadId.toString()}" class="bg-white p-6 rounded shadow max-w-md space-y-4">

        <div>
            <label for="name" class="block text-sm font-medium text-gray-700 mb-1">Имя</label>
            <input type="text" id="name" name="name" value="${lead.name()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md" />
        </div>

        <div>
            <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input type="email" id="email" name="email" value="${lead.email()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md" />
        </div>

        <div>
            <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">Телефон</label>
            <input type="text" id="phone" name="phone" value="${lead.phone()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md" />
        </div>

        <div>
            <label for="companyName" class="block text-sm font-medium text-gray-700 mb-1">Компания</label>
            <input type="text" id="companyName" name="companyName" value="${lead.companyName()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md" />
        </div>

        <div>
            <label for="status" class="block text-sm font-medium text-gray-700 mb-1">Статус</label>
            <select name="status" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                @if(lead.status().name().equals("NEW"))
                    <option value="NEW" selected>Новый</option>
                @else
                    <option value="NEW">Новый</option>
                @endif
                @if(lead.status().name().equals("CONTACTED"))
                    <option value="CONTACTED" selected>Связались</option>
                @else
                    <option value="CONTACTED">Связались</option>
                @endif
                @if(lead.status().name().equals("QUALIFIED"))
                    <option value="QUALIFIED" selected>Готов к сделке</option>
                @else
                    <option value="QUALIFIED">Готов к сделке</option>
                @endif
                @if(lead.status().name().equals("DONE"))
                    <option value="DONE" selected>Готово</option>
                @else
                    <option value="DONE">Готово</option>
                @endif
            </select>
        </div>

        <div class="flex gap-4">
            <button type="submit"
                    class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                Сохранить
            </button>
            <a href="/jpa/leads"
               class="bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400">
                Отмена
            </a>
        </div>
    </form>
</div>
</body>
</html>
