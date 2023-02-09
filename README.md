# Vending Machine MVP

###### ⚠️ This project is an MVP to demonstrate the coding style, tools as a coding challenge.


### How to deploy in docker:

Simply run this command on the root folder:  `./gradlew composeUp` (requires JDK 17+)

### Tech Stack:
- Spring Boot (Kotlin)
- Gradle
- Spring Security
- Spring Data JPA
- H2 DB (For Development)
- MySQL (For Production)
- OpenApi & Swagger code generator
- Spring Web (Rest)
- Redis (Prod Profile) for Cache and Realtime events
- Github Action for automated testing (configured on pull_requests to main/develop and push to develop)
- Junit (Unit tests & Integration tests)
- Mapstruct
- Liquibase (database migration)


[Postman Collection](https://github.com/ahmadzadeh/mvp-match-task-1/blob/main/postman/vending_machine.postman_collection.json)

More instructions and screenshots will be updated soon...
