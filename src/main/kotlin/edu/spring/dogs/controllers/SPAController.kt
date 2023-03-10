package edu.spring.dogs.controllers

import edu.spring.dogs.entities.Master
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
    lateinit var vue:VueJS

    @ModelAttribute("vue")
    fun getVueInstance():VueJS=vue
    @RequestMapping(path=["/",""])
    fun index():String {
        vue.addDataRaw("masters", "[]")
        vue.addData("newMaster",Master("",""))
        vue.addMethod("addMaster",
            Http.post("/masters","master",
                "master.id=response.data.id;"+
                            JsArray.add("this.masters","master")+
                            "this.newMaster={};"
                ,"console.log('Erreur');")
            ,"master")

        vue.addMethod("deleteMaster",
            Http.delete("'/masters/'+master.id",
                JsArray.remove("this.masters","master")
                ,"console.log('Erreur');")
            ,"master")

        vue.onMounted(
            Http.get("/masters",
                Http.responseArrayToArray("this.masters","masters")
            )
        )
        return "/spa/index"
    }
}