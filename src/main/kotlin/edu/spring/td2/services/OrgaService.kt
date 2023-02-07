package edu.spring.td2.services

import edu.spring.td2.entities.Organization
import edu.spring.td2.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrgaService {
    @Autowired
    lateinit var userService:UserService

    fun addUsersFromString(users:String, orga:Organization){
        if(users.trim().isNotEmpty()) {
            users.split("\n").forEach {
                val user = User()
                val values = it.trim().split(" ", limit = 2)
                user.firstname = values.getOrElse(0) { "" }
                user.lastname = values.getOrElse(1) { "" }
                user.email = "${user.firstname}.${user.lastname}@${orga.domain}".lowercase()
                user.password = userService.generatePassword()
                orga.addUser(user)
            }
        }
    }
}