package co.mvpmatch.backendtask1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import co.mvpmatch.backendtask1.config.ApplicationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class BackendTask1Application

fun main(args: Array<String>) {
    runApplication<BackendTask1Application>(*args)
}
