package edu.spring.td2.controllers

import edu.spring.td2.entities.Organization
import edu.spring.td2.exceptions.ElementNotFoundException
import edu.spring.td2.repositories.OrgaRepository
import edu.spring.td2.services.OrgaService
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
@RequestMapping("/orgas")
class OrgaController {

    @Autowired
    lateinit var orgaRepository: OrgaRepository

    @Autowired
    lateinit var orgaService:OrgaService

    @RequestMapping(path = ["","/","/index"])
    fun indexAction(model:ModelMap):String{
        model["orgas"]=orgaRepository.findAll()
        return "/orgas/index"
    }

    @GetMapping("/new")
    fun newAction(model:ModelMap):String{
        model["orga"]=Organization("")
        return "/orgas/form";
    }

    @GetMapping("/display/{id}")
    fun displayAction(@PathVariable id:Int,model:ModelMap):String{
        val option=orgaRepository.findById(id)
        if(option.isPresent) {
            model["orga"]=option.get()
            return "/orgas/display"
        }
        throw ElementNotFoundException("Organisation d'identifiant $id inexistante !")
    }

    @PostMapping("/new")
    fun submitNewAction(
        @ModelAttribute orga:Organization,
        @ModelAttribute("users") users:String
    ):RedirectView{
        orgaService.addUsersToOrga(orga,users)
        orgaRepository.save(orga)
        return RedirectView("/orgas/")
    }

    @GetMapping("/delete/{id}")
    fun deleteAction(@PathVariable id:Int):RedirectView{
        val option=orgaRepository.findById(id)
        if(option.isPresent){
            orgaRepository.delete(option.get())
            return RedirectView("/orgas")
        }
        throw ElementNotFoundException("Organisation $id introuvable")
    }

    @GetMapping("/{id}/groups/new")
    fun addNewGroup(@PathVariable id:Int,model:ModelMap):String{
        val option=orgaRepository.findById(id)
        if(option.isPresent){
            model["orga"]=option.get()
            return "/orgas/groups/form"
        }
        throw ElementNotFoundException("Organisation $id introuvable")
    }

    @PostMapping("/{id}/groups/new")
    fun addNewSubmitAction(
        @ModelAttribute("idOrga") idOrga:Int,
        @ModelAttribute("groups") groups:String
    ):RedirectView {
        val option = orgaRepository.findById(idOrga)
        if (option.isPresent) {
            val orga = option.get()
            orgaService.addGroupsToOrga(orga, groups)
            orgaRepository.save(orga)
            return RedirectView("/orgas/$idOrga")
        }
        throw ElementNotFoundException("Organisation $idOrga introuvable")
    }
}
