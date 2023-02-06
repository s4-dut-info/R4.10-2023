package edu.spring.td2.controllers

import edu.spring.td2.entities.Organization
import edu.spring.td2.entities.User
import edu.spring.td2.repositories.OrganizationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/orgas/")
class OrgaController {

    @Autowired
    lateinit var orgaRepository:OrganizationRepository

    @RequestMapping(path = ["","index"])
    fun indexAction(model:ModelMap):String{
        val orgas=orgaRepository.findAll()
        model["orgas"]=orgas
        return "/orgas/index"
    }
    @GetMapping("add/{name}")
    @ResponseBody
    fun testAddAction(@PathVariable name:String):String{
        val orga=Organization()
        orga.name=name
        var user=User()
        user.firstname="Bob"
        user.lastname="Leponge"
        user.email="boob@mail.com"
        orga.addUser(user)
        orgaRepository.save(orga)
        return "Organisation $name ajoutée avec 1 user : ${user.email}"
    }
}