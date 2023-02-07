package edu.spring.td2.controllers

import edu.spring.td2.entities.Organization
import edu.spring.td2.entities.User
import edu.spring.td2.repositories.OrganizationRepository
import edu.spring.td2.services.OrgaService
import edu.spring.td2.services.UserService
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

    @Autowired
    lateinit var orgaService:OrgaService

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
        orgaService.addUsersFromString(users, orga)
        orgaRespository.save(orga)
        return RedirectView("/orgas/")
    }

    @GetMapping("/delete/{id}")
    fun deleteAction(@PathVariable id:Int):RedirectView{
        orgaRespository.deleteById(id)
        return RedirectView("/orgas/")
    }
}