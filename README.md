# `Framework-less Java API`
This project has the educational purpose of understanding the underlying principles and mechanics involved in handling HTTP requests,
managing routes, processing data and even dependency injection without relying on any frameworks such as Spring, Quarkus, Spark etc.

## Requirements
- Java 17

## Authentication
Basic auth with username: `admin` and password `admin`.

## Endpoints

### `/api/users/register`
Create a user.
Payload:
```json
{
  "login": "login",
  "password": "password"
}

```


`/api/users/list`
List created users.
