ЗАПУСК С DOCKER

В корне проекта вызвать команды:
  1. docker-compose buil
  2. docker-compose up
  3. docker exec -it wallet-app-app-1 /bin/bash      #подключение к контейнеру приложения
  4. mvn test                                        #находясь в контейнере для запуска тестов

ДОПОЛНИТЕЛЬНО

  При запуске проекта добавляется строка с кошельком id: "59c9b8e8-b582-4224-a8a3-ef098dd9118c"

API ЭНДПОИНТЫ

Все запросы - JSON:
- Выполнить операцию (DEPOSIT/WITHDRAW)
  POST /api/v1/wallet
  Тело запроса:
  
  {
    "walletId": "59c9b8e8-b582-4224-a8a3-ef098dd9118c",
    "operationType": "DEPOSIT",
    "amount": 1000.00
  }
  
  Ответ: 200 OK или.
- Получить баланс кошелька
  GET /api/v1/wallet/59c9b8e8-b582-4224-a8a3-ef098dd9118c
   Ответ:

  {
    "balance": 1500.00
  }
