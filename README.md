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


[Postman Collection](https://github.com/ahmadzadeh/mvp-match-task-1/blob/main/postman/mvp_match_postman_collection.json)

[Postman Environment](https://github.com/ahmadzadeh/mvp-match-task-1/blob/main/postman/mvp_match_.postman_environment.json)


[Deploy and Run API Screencast](https://github.com/ahmadzadeh/mvp-match-task-1/blob/main/assets/Screencast.mp4)


Automated Testing (CI):

![Checking Tests](https://github.com/ahmadzadeh/mvp-match-task-1/blob/main/assets/test-wait.png)

![Checks Passed](https://github.com/ahmadzadeh/mvp-match-task-1/blob/main/assets/check-passed.png)

More instructions and screenshots will be updated soon...
