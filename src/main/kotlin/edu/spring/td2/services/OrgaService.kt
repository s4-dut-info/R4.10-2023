package edu.spring.td2.services

import edu.spring.td2.entities.Organization
import edu.spring.td2.entities.User
import org.springframework.stereotype.Service

@Service
class OrgaService {
    fun addUsersFromString(users:String,organization:Organization){
        if(users.isNotEmpty()){
            users.split("\n").forEach{
                var user=User()
                val (firstname,lastname)=it.trim().split(" ", limit = 2)
                user.firstname=firstname
                user.lastname=lastname
                user.email="${user.firstname}.${user.lastname}@${organization.domain}".lowercase()
                organization.addUser(user)
            }
        }
    }
}