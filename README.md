# Wallets Service
Wallet service that allows users to:
1. Query the balance of a wallet
2. Top up the wallet by using a credit card

## Pre-filled data
Please refer to the [data.sql](src/main/resources/data.sql) file to see the pre-filled wallets

## Run
`mvn spring-boot:run`

## Examples
### Get a wallet
```shell
curl --location 'localhost:8090/api/wallets/123e4567-e89b-12d3-a456-426614174000'
```

### Top up a wallet

```shell
curl --location 'localhost:8090/api/wallets/123e4567-e89b-12d3-a456-426614174000/deposits' \
--header 'Content-Type: application/json' \
--data '{
"creditCardNumber": "9283247273",
"amount": 2000
}'

```

## H2 database console
localhost:8090/h2-console

## 