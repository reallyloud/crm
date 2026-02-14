@import ru.mentee.power.crm.spring.entity.Deal
@import ru.mentee.power.crm.domain.DealStatus
@param java.util.Map<DealStatus, java.util.List<Deal>> dealsByStatus

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Воронка продаж </title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 p-8">
<div class="flex justify-between items-center mb-6">
    <h1 class="text-3xl font-bold">Воронка продаж</h1>
    <a href="/jpa/deals" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">Список сделок</a>
</div>

<div class="grid grid-cols-6 gap-4">
    @for(DealStatus status : DealStatus.values())
        <div class="bg-white rounded shadow-md p-4">
            <h2 class="font-bold mb-4 text-center">${status}</h2>

            @for(Deal deal : dealsByStatus.getOrDefault(status, java.util.List.of()))
                <div class="bg-gray-50 p-3 mb-2 rounded border">
                    <p class="font-semibold">${deal.getTitle()}</p>
                    <p class="text-sm">${deal.getAmount()} ₽</p>
                    <p class="text-xs text-gray-500">Lead: ${deal.getLeadId() != null ? deal.getLeadId().toString() : "N/A"}</p>

                    @if(status.canTransitionTo(DealStatus.QUALIFIED) || status.canTransitionTo(DealStatus.PROPOSAL_SENT) || status.canTransitionTo(DealStatus.NEGOTIATION) || status.canTransitionTo(DealStatus.WON))
                        <form action="/jpa/deals/${deal.getId().toString()}/transition" method="post" class="mt-2">
                            <select name="newStatus" class="text-xs border rounded px-1 py-0.5 w-full mb-1">
                                @for(DealStatus target : DealStatus.values())
                                    @if(status.canTransitionTo(target))
                                        <option value="${target.name()}">${target}</option>
                                    @endif
                                @endfor
                            </select>
                            <button type="submit" class="text-xs bg-green-500 text-white px-2 py-1 rounded w-full hover:bg-green-600">
                                Перевести
                            </button>
                        </form>
                    @endif
                </div>
            @endfor
        </div>
    @endfor
</div>
</body>
</html>
