
# Toggl -> Tempo (MVP)

Convert Toggl entries into Tempo worklogs. Using toggl api key fetch all entires (last 9 days) that do not have `synced` tag. Task description must consist of task id and decription separated by pipeline `|` character. On sync it will send request to tempo api and add time log.

![image](https://user-images.githubusercontent.com/4154034/152775907-6a85c1f3-d81f-429e-8243-9f60d4b14262.png)


## Requirements

* [Clojure](https://clojure.org/guides/getting_started)
* [Docker compose](https://docs.docker.com/compose/install/)

## Installation

### Secrets

Create `.secrets.end` file in resources folder with following keys (insert valid values)

```
{:db "jdbc:postgresql://localhost:5433/mini?user=postgres&password=postgres"
 :google-oauth2 {:authorize-uri "https://accounts.google.com/o/oauth2/v2/auth"
                 :access-token-uri "https://oauth2.googleapis.com/token"
                 :client-id "{CLIENT ID}"
                 :client-secret "{CLIENT SECRET}"
                 :scopes ["openid"]
                 :launch-uri "/oauth2/google"
                 :redirect-uri "/oauth2/google/callback"
                 :landing-uri "/"}}
```
### Initialize DB

```
docker-compose up -d
```

### Create table

Hook into psql

```
docker-compose exec db psql -U postgres -d mini
```

and then run 

```
CREATE TABLE credentials(
  id SERIAL PRIMAY KEY,
  user_id VARCHAR(64) NOT NULL UNIQUE,
  options JSON
);
```
## Usage

```
clj -m infrastructure.server
```

## TODO
- [] success/failure message on redirect
- [] extract oauth/db credentials to .secrets.edn
- [] handle http requests other than 200
- [] wrap-auth -> it should check validiti of jwt like exp, domain, etc.
- [] test coverage

## NICE TO HAVE
- [] better DI integration
- [] log errors
- [] add validation to request input data
- [] use named routes insted of hardcoded
- [] loading overlay when syncing starts
- [] migration
- [] default 404 and 500 page

