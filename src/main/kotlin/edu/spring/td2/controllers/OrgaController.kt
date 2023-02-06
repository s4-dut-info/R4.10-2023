package edu.spring.td2.controllers

import edu.spring.td2.entities.Organization
import edu.spring.td2.entities.User
import edu.spring.td2.repositories.OrganizationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView
import java.util.UUID

@Controller
@RequestMapping("/orgas/")
class OrgaController {

    @Autowired
    lateinit var orgaRespository: OrganizationRepository

    @RequestMapping(path = ["","index"])
    fun indexAction(model:ModelMap):String{
        model["orgas"]=orgaRespository.findAll()
        return "/orgas/index"
    }

    @GetMapping("/new")
    fun newAction(model:ModelMap):String{
        model["orga"]=Organization()
        return "/orgas/form";
    }

    @PostMapping("/new")
    fun submitNewAction(
        @ModelAttribute orga:Organization,
        @ModelAttribute users:String
    ):RedirectView{
        val usersArray=users.split("\n").forEach{
            val user=User()
            val values=it.split(" ")
            user.firstname=values.getOrNull(0)
            user.lastname=values.getOrNull(1)
            user.email="${user.lastname}.${user.firstname}@${orga.domain}"
            user.password=UUID.randomUUID().toString()
            orga.addUser(user)
        }
        orgaRespository.save(orga)
        return RedirectView("/orgas/")
    }
}