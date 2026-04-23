package io.github.onsaenaro.endpoint.user.service

//@Service
//class CustomUserDetailsService(
//    private val userRepository: UserRepository
//) : UserDetailsService {
//
//    override fun loadUserByUsername(identifier: String): UserDetails {
//        val user = if (identifier.contains("@")) {
//            userRepository.findByEmail(identifier)
//        } else {
//            userRepository.findByUsername(identifier)
//        } ?: throw UsernameNotFoundException("User not found: $identifier")
//
//        return User.builder()
//            .username(user.username)
//            .password(user.password)
//            .roles(user.role.name)
//            .build()
//    }
//}