package edu.spring.dogs.repositories

import edu.spring.dogs.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Int> {
    @Query("SELECT u FROM User u WHERE u.username=:usernameOrEmail OR u.email=:usernameOrEmail")
    fun findByUsernameOrEmail(usernameOrEmail: String?): User?
}