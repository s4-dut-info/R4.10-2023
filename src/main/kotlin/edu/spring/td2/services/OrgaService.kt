package edu.spring.td2.services

import edu.spring.td2.entities.Group
import edu.spring.td2.entities.Organization
import edu.spring.td2.entities.User
import org.springframework.stereotype.Service

@Service
class OrgaService {
    fun addUsersToOrga(orga:Organization, users:String){
        if(users.isNotEmpty()){
            users.split("\n").forEach{
                val user=User()
                val (firstname,lastname)=it.trim().split(" ", limit = 2)
                user.firstname=firstname
                user.lastname=lastname
                user.email="$firstname.$lastname@${orga.domain}".lowercase()
                orga.addUser(user)
            }
        }
    }

    fun addGroupsToOrga(orga:Organization, groups:String){
        if(groups.isNotEmpty()){
            groups.split(",").forEach{
                val group=Group()
                group.name=it.trim()
                orga.addGroup(group)
            }
        }
    }
}