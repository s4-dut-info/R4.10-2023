package edu.spring.dogs.controllers

import edu.spring.dogs.entities.Dog
import edu.spring.dogs.entities.Master
import edu.spring.dogs.repositories.DogRepository
import edu.spring.dogs.repositories.MasterRepository
import edu.spring.dogs.services.OnlyAdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class MainController {
    @Autowired
    lateinit var onlyAdminService: OnlyAdminService

    @Autowired
    lateinit var masterRepository: MasterRepository

    @Autowired
    lateinit var dogRepository: DogRepository

    @RequestMapping("/")
    fun index(model:ModelMap,auth:Authentication): String {
        val masters=masterRepository.findAll()
        model["masters"]= masters
        model["hasMasters"]= masters.count()>0
        model["user"]=auth
        model["dogs"]= dogRepository.findByMasterIsNull()
        return "index"
    }

    @PostMapping("/master/{id}/dog")
    fun addOrGiveUp(@ModelAttribute dog:Dog, @PathVariable id:Int, @RequestParam("dog-action") dogAction:String): String {
        val master=masterRepository.findById(id).get()
        if(dogAction=="add")
            master.addDog(dog)
        else {
            val myDog=dogRepository.findByNameAndMasterId(dog.name,id)
            if(myDog!=null) {
                master.giveUpDog(myDog)
            }
        }
        masterRepository.save(master)
        return "redirect:/"
    }

    @PostMapping("/master/add")
    fun addMaster(@ModelAttribute master:Master): String {
        masterRepository.save(master)
        return "redirect:/"
    }

    @GetMapping("/master/{id}/delete")
    fun deleteMaster(@PathVariable id:Int): String {
        masterRepository.deleteById(id)
        return "redirect:/"
    }

    @PostMapping("/dog/{id}/action")
    fun dogAction(@PathVariable id:Int, @RequestParam("dog-action") dogAction:String, @RequestParam(value="master", required = false) masterId:Int?): String {
        val dog=dogRepository.findById(id).get()
        if(dogAction=="remove") {
            dogRepository.delete(dog)
        }else{
            if(masterId!=null) {
                val master = masterRepository.findById(masterId).get()
                master.addDog(dog)
                masterRepository.save(master)
            }
        }
        return "redirect:/"
    }
    @RequestMapping("/admin")
    @ResponseBody
    fun adminAction(user:Authentication):String {
        return onlyAdminService.onlyAdmin(user.name)
    }
}