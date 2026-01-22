@import ru.mentee.power.crm.model.Lead
@param Lead lead

<!DOCTYPE html>
<html>
<head>
    <title>Редактирование лида</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-6">
    <h1 class="text-2xl font-bold mb-4">Редактирование лида</h1>
    <form method="post" action="/leads/${lead.id().toString()}" class="bg-white p-6 rounded shadow">
        <!-- Hidden field для ID -->
        <input type="hidden" name="id" value="${lead.id().toString()}" />
        <label>
            <input type="text" name="name" value="${lead.name()}"/>
        </label>
        <label>
            <input type="email" name="email" value="${lead.email()}"/>
        </label>
        <label>
            <input type="text" name="phone" value="${lead.phone()}">
        </label>
        <label>
            <input type="text" name="company" value="${lead.company()}">
        </label>

        <!-- Select с conditional selected -->
        <label>
            <select name="status">
                <option value="NEW">Новый</option>
                <option value="CONTACTED">Связались</option>
                <option value="QUALIFIED">Готов к сделке</option>
            </select>
        </label>
        <!-- Buttons -->
        <button type="submit" class="text-blue-600 hover:underline">Готово</button>
    </form>
</div>
</body>
</html>