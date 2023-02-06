package co.mvpmatch.backendtask1.mapper

import co.mvpmatch.backendtask1.domain.Authority
import co.mvpmatch.backendtask1.domain.User
import co.mvpmatch.backendtask1.web.api.model.RegistrationDTO
import co.mvpmatch.backendtask1.web.api.model.RolesEnum
import co.mvpmatch.backendtask1.web.api.model.UserDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring", uses = [])
abstract class UserMapper {
    @Mapping(source = "authorities", target = "roles", qualifiedByName = ["authToRoles"])
    abstract fun toDTO(entity: User): UserDTO
    abstract fun toDTO(entities: List<User>): List<UserDTO>

    @Mapping(source = "roles", target = "authorities", qualifiedByName = ["rolesToAuth"])
    abstract fun toEntity(dto: RegistrationDTO): User

    @Named("authToRoles")
    fun authSetToList(set: Set<Authority>): List<RolesEnum> {
        return set.map { RolesEnum.fromValue(it.name.lowercase()) }
    }

    @Named("rolesToAuth")
    fun rolesToAuth(roles: List<RolesEnum>): Set<Authority> {
        return roles.map { Authority(it.name.uppercase()) }.toSet()
    }
}
