package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.config.ROLE_ADMIN
import co.mvpmatch.backendtask1.service.UserService
import co.mvpmatch.backendtask1.web.api.model.RegistrationDTO
import co.mvpmatch.backendtask1.web.api.model.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class UserApiImpl(
    private val userService: UserService
) : UserApi {

    override fun _registration(registrationDTO: RegistrationDTO): ResponseEntity<Void> {
        userService.register(registrationDTO)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Secured(ROLE_ADMIN)
    override fun _getAllUsersByAdmin(): ResponseEntity<List<UserDTO>> {
        return ResponseEntity.ok(userService.findAll())
    }

    @Secured(ROLE_ADMIN)
    override fun _updateUserByAdmin(username: String, registrationDTO: RegistrationDTO): ResponseEntity<Void> {
        userService.updateUser(username, registrationDTO)
        return ResponseEntity.ok().build()
    }

    @Secured(ROLE_ADMIN)
    override fun _deleteUserByAdmin(username: String): ResponseEntity<Void> {
        userService.deleteUser(username)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
