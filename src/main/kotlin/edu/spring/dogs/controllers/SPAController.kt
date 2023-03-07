package edu.spring.dogs.controllers

import edu.spring.dogs.entities.Master
import edu.spring.dogs.repositories.DogRepository
import edu.spring.dogs.repositories.MasterRepository
import io.github.jeemv.springboot.vuejs.VueJS
import io.github.jeemv.springboot.vuejs.utilities.Http
import io.github.jeemv.springboot.vuejs.utilities.JsArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/spa")
class SPAController {

    @Autowired
    lateinit var dogRepo: DogRepository

    @Autowired
    lateinit var masterRepo: MasterRepository

    @Autowired
    lateinit var vue: VueJS

    @ModelAttribute("vue")
    fun vue(): VueJS = vue

    @RequestMapping(path=["/",""])
    fun index(): String {
        vue.addData("masters", masterRepo.findAll())
        vue.addData("dogs", dogRepo.findByMasterIsNull())
        vue.addData("master", Master("",""))
        vue.addMethod("addMaster",
            Http.post("/masters","master",
                JsArray.add("this.masters","master")),
            "master")
        vue.addMethod("remove",
            Http.delete("'/masters/'+master.id",
                JsArray.remove("this.masters","master")+
                JsArray.addAll("this.dogs","master.dogs"),
                "console.log('erreur sur suppression')"),
            "master")
        return "/spa/index"
    }
}