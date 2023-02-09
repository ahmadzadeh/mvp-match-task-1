package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.config.ROLE_ADMIN
import co.mvpmatch.backendtask1.config.ROLE_BUYER
import co.mvpmatch.backendtask1.config.ROLE_SELLER
import co.mvpmatch.backendtask1.config.UnauthorizedException
import co.mvpmatch.backendtask1.config.security.getCurrentUserLogin
import co.mvpmatch.backendtask1.service.UserService
import co.mvpmatch.backendtask1.web.api.model.BaseUserDTO
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

    override fun _registration(registrationDTO: RegistrationDTO): ResponseEntity<BaseUserDTO> {
        return ResponseEntity(userService.register(registrationDTO), HttpStatus.CREATED)
    }

    @Secured(ROLE_ADMIN)
    override fun _getAllUsersByAdmin(): ResponseEntity<List<UserDTO>> {
        return ResponseEntity.ok(userService.findAll())
    }

    @Secured(ROLE_SELLER, ROLE_BUYER, ROLE_ADMIN)
    override fun _getCurrentUserInfo(): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(
            userService.getUserByUserName(
                getCurrentUserLogin() ?: throw UnauthorizedException()
            )
        )
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
