package co.mvpmatch.backendtask1.service

import co.mvpmatch.backendtask1.config.ResourceAlreadyExistsException
import co.mvpmatch.backendtask1.config.ResourceNotFoundException
import co.mvpmatch.backendtask1.domain.User
import co.mvpmatch.backendtask1.mapper.UserMapper
import co.mvpmatch.backendtask1.repository.UserRepository
import co.mvpmatch.backendtask1.web.api.model.RegistrationDTO
import co.mvpmatch.backendtask1.web.api.model.UserDTO
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder
) {

    fun findAll(): List<UserDTO> {
        return userMapper.toDTO(userRepository.findAll().toList())
    }

    @Transactional
    fun deleteUser(username: String) {
        val user = userRepository.findOneByUsernameIgnoreCase(username)
            .orElseThrow { ResourceNotFoundException() }
        userRepository.delete(user)
    }

    @Transactional
    @Throws(ResourceAlreadyExistsException::class)
    fun register(registrationDTO: RegistrationDTO) {
        preventDuplicatedUserName(registrationDTO.username)
        val entity = userMapper.toEntity(registrationDTO)
        entity.passwordHash = passwordEncoder.encode(registrationDTO.password)
        entity.jwtSalt = UUID.randomUUID().toString()
        entity.activated = true
        userRepository.save(entity)
    }

    @Throws(ResourceAlreadyExistsException::class, ResourceNotFoundException::class)
    fun updateUser(username: String, payload: RegistrationDTO) {
        preventDuplicatedUserName(payload.username) //new username should not be taken
        val user = getByUserName(username)
        //only update non-null fields in payload
        payload.username?.let { user.username = it }
        payload.fullName?.let { user.fullName = it }
        payload.password?.let { user.passwordHash = passwordEncoder.encode(it) }
        payload.roles?.let {
            user.authorities = userMapper.rolesToAuth(it).toMutableSet()
        }
    }

    @Throws(ResourceNotFoundException::class)
    fun getUserByUserName(username: String): UserDTO {
        val user = userRepository.findOneByUsernameIgnoreCase(username)
            .orElseThrow { ResourceNotFoundException() }
        return userMapper.toDTO(user)
    }

    @Throws(ResourceNotFoundException::class)
    fun getByUserName(username: String): User {
        return userRepository.findOneByUsernameIgnoreCase(username)
            .orElseThrow { ResourceNotFoundException() }
    }

    private fun preventDuplicatedUserName(usernameToCheck: String) {
        if (userRepository.existsByUsernameIgnoreCase(usernameToCheck))
            throw ResourceAlreadyExistsException()
    }
}
