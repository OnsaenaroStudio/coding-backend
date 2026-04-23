package io.github.onsaenaro.endpoint.user.service

import io.github.onsaenaro.endpoint.user.dto.UserRequestDto
import io.github.onsaenaro.endpoint.user.entity.UserTable
import io.github.onsaenaro.endpoint.user.extension.toUserResponseDto
import io.github.onsaenaro.endpoint.user.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    @Transactional
    fun createUser(dto: UserRequestDto): UUID {
        if (userRepository.findByUsername(dto.username) != null)
            throw IllegalArgumentException("Username already exists")

        val encodedPassword = passwordEncoder.encode(dto.password)!!
        return userRepository.insert(dto, encodedPassword)
    }

    fun findAll() =
        userRepository.findAll().map { it.toUserResponseDto() }

    fun findUserById(uuid: UUID) =
        userRepository.findById(uuid)?.toUserResponseDto()

    fun findUserByUsername(username: String) =
        userRepository.findByUsername(username)?.toUserResponseDto()

    override fun loadUserByUsername(username: String): UserDetails {
        val row = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        return User.builder()
            .username(row[UserTable.username])
            .password(row[UserTable.password])
            .roles(row[UserTable.role].name)
            .build()
    }

//    @Transactional
//    fun createUser(dto: UserRequestDto): UUID {
//        if (findUserByUsername(dto.username) != null)
//            throw IllegalArgumentException("Username already exists")
//
//        return UserTable.insertAndGetId {
//            it[username] = dto.username
//            it[email] = dto.email
//            it[password] = passwordEncoder.encode(dto.password)!! // 절대 null로 받을 수 없음.
//            it[nickname] = dto.nickname
//            it[role] = UserRole.USER
//        }.value
//    }
//
//    fun findAll() = UserTable.selectAll().map {
//        it.toUserResponseDto()
//    }
//
//    fun findUserByUUID(uuid: UUID): UserResponseDto? =
//        findUserEntityByUUID(uuid)?.toUserResponseDto()
//
//    fun findUserByUsername(username: String): UserResponseDto? =
//        findUserEntityByUsername(username)?.toUserResponseDto()
//
//    private fun findUserEntityByUUID(uuid: UUID): ResultRow? =
//        UserTable.selectAll().where {
//            UserTable.id eq uuid
//        }.firstOrNull()
//
//    private fun findUserEntityByUsername(username: String): ResultRow? =
//        UserTable.selectAll().where {
//            UserTable.username eq username
//        }.firstOrNull()

}