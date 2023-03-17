package edu.spring.dogs.controllers

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
    lateinit var dogRepository: DogRepository
    @Autowired
    lateinit var vue:VueJS

    @ModelAttribute("vue")
    fun getVueInstance() = vue

    @RequestMapping(path=["/",""])
    fun index():String {
        vue.addDataRaw("masters", "[]")
        vue.addData("dogs", dogRepository.findByMasterIsNull())
        vue.addDataRaw("newMaster", "{}")

        vue.addMethod("addMaster",
            Http.post("/masters",
                "master",
                JsArray.add("this.masters","master")+
                        "this.newMaster={}",
                "console.log('Erreur')"),
            "master")

        vue.addMethod("removeMaster",
            Http.delete("'/masters/'+master.id",
                JsArray.remove("this.masters","master")+
                        JsArray.addAll("this.dogs","master.dogs"),
                "console.log('Erreur')"),
            "master")

        vue.onMounted(Http.get("/masters",
            Http.responseArrayToArray("this.masters", "masters"),
            "console.log('Erreur')"))
        return "/spa/index"
    }
}