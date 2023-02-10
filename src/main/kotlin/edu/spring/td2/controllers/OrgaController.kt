package edu.spring.td2.controllers

import edu.spring.td2.entities.Organization
import edu.spring.td2.entities.User
import edu.spring.td2.repositories.OrgaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping("/orgas")
class OrgaController {

    @Autowired
    lateinit var orgaRepository:OrgaRepository

    @RequestMapping(path = ["","/index","/"])
    fun indexAction(model:ModelMap):String {
        model["orgas"]=orgaRepository.findAll()
        return "orgas/index"
    }

    @GetMapping("/new")
    fun newAction():ModelAndView{
     val mv=ModelAndView("/orgas/form")
     mv.addObject("orga",Organization(""))
     return mv
    }
    @PostMapping("/new")
    fun newSubmitAction(
        @ModelAttribute orga:Organization,
        @ModelAttribute("users") users:String
    ):RedirectView{
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
        orgaRepository.save(orga)
        return RedirectView("/orgas")
    }
}