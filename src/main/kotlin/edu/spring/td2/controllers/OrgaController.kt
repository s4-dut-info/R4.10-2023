package edu.spring.td2.controllers

import edu.spring.td2.entities.Organization
import edu.spring.td2.exceptions.ElementNotFoundException
import edu.spring.td2.repositories.OrganizationRepository
import edu.spring.td2.services.OrgaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping("/orgas")
class OrgaController {

    @Autowired
    lateinit var orgaRepository:OrganizationRepository

    @Autowired
    lateinit var orgaService: OrgaService

    @RequestMapping(path = ["","/","/index"])
    fun indexAction(model:ModelMap):String{
        val orgas=orgaRepository.findAll()
        model["orgas"]=orgas
        val i=1/0
        return "/orgas/index"
    }

    @GetMapping("/new")
    fun addNewAction(model:ModelMap):String{
        model["orga"]=Organization("")
        return "/orgas/form"
    }

    @PostMapping("/new")
    fun submitAddNewAction(
        @ModelAttribute orga:Organization,
        @ModelAttribute("users") users:String
    ):RedirectView{
        orgaService.addUsersFromString(users,orga)
        orgaRepository.save(orga)
        return RedirectView("/orgas/")
    }
    @GetMapping("/display/{id}")
    fun display(@PathVariable id:Int,model:ModelMap):String{
        val option=orgaRepository.findById(id)
        if(option.isPresent) {
            model["orga"]=option.get()
            return "/orgas/display"
        }
        throw ElementNotFoundException("Organisation d'identifiant $id non trouvée")
    }

    @ExceptionHandler//(value = [ElementNotFoundException::class])
    fun exceptionHandler(ex:RuntimeException):ModelAndView {
        val mv=ModelAndView("/main/error")
        mv.addObject("message",ex.message)
        return mv
    }
}