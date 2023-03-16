package edu.spring.dogs.services

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class OnlyAdminService {
    @PreAuthorize("#username == authentication.principal.username and hasRole('ROLE_ADMIN')")
    fun onlyAdmin(username:String):String {
        return "Only admin $username can see this"
    }
}