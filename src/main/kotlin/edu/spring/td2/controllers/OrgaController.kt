package edu.spring.td2.controllers

import edu.spring.td2.entities.Organization
import edu.spring.td2.entities.User
import edu.spring.td2.repositories.OrganizationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView
import java.util.*

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
        @ModelAttribute("users") users:String
    ):RedirectView{
        if(users.trim().isNotEmpty()) {
            users.split("\n").forEach {
                val user = User()
                val values = it.trim().split(" ", limit = 2)
                user.firstname = values.getOrElse(0) { "" }
                user.lastname = values.getOrElse(1) { "" }
                user.email = "${user.firstname}.${user.lastname}@${orga.domain}".lowercase()
                user.password = List(8) { (0x21..0x7e).random().toChar() }.joinToString("")
                orga.addUser(user)
            }
        }
        orgaRespository.save(orga)
        return RedirectView("/orgas/")
    }

    @GetMapping("/delete/{id}")
    fun deleteAction(@PathVariable id:Int):RedirectView{
        orgaRespository.deleteById(id)
        return RedirectView("/orgas/")
    }
}