package co.mvpmatch.backendtask1.mapper

import co.mvpmatch.backendtask1.domain.Authority
import co.mvpmatch.backendtask1.domain.User
import co.mvpmatch.backendtask1.web.api.model.BaseUserDTO
import co.mvpmatch.backendtask1.web.api.model.RegistrationDTO
import co.mvpmatch.backendtask1.web.api.model.RolesEnum
import co.mvpmatch.backendtask1.web.api.model.UserDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named

@Mapper(componentModel = "spring", uses = [])
abstract class UserMapper {
    @Mapping(source = "authorities", target = "roles", qualifiedByName = ["authToRoles"])
    abstract fun toDTO(entity: User): UserDTO

    @Mapping(source = "authorities", target = "roles", qualifiedByName = ["authToRoles"])
    abstract fun toBaseDTO(entity: User): BaseUserDTO

    abstract fun toDTO(entities: List<User>): List<UserDTO>
    abstract fun toBaseDTO(entity: List<User>): List<BaseUserDTO>

    @Mappings(
        Mapping(source = "roles", target = "authorities", qualifiedByName = ["rolesToAuth"]),

        // these fields are not present on dto.
        // we set them to empty, to prevent null pointer exception
        Mapping(target = "jwtSalt", expression = "java(\"\")"),
        Mapping(target = "passwordHash", expression = "java(\"\")")
    )
    abstract fun toEntity(dto: RegistrationDTO): User

    @Named("authToRoles")
    fun authSetToList(set: Set<Authority>): List<RolesEnum> {
        return set.map { RolesEnum.fromValue(it.name.lowercase()) }
    }

    @Named("rolesToAuth")
    fun rolesToAuth(roles: List<RolesEnum>): Set<Authority> {
        return roles.map { Authority(it.value.uppercase()) }.toSet()
    }
}
