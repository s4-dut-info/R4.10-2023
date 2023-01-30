package edu.spring. td1.controllers

import edu.spring.td1.models.Item
import edu.spring.td1.services.UIMessage
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView

@Controller
@SessionAttributes("items")
class ItemsController {

    private fun getItemByName(nom:String,items:HashSet<Item>):Item?=items.find { nom==it.nom }

    private fun addMsg(resp:Boolean,attrs: RedirectAttributes,title:String,success:String,error:String){
        if(resp) {
            attrs.addFlashAttribute("msg",
                    UIMessage.message(title, success))
        } else {
            attrs.addFlashAttribute("msg",
                    UIMessage.message(title, error,"error","warning circle"))

        }
    }
    @get:ModelAttribute("items")
    val items: Set<Item>
        get() {
            var items= HashSet<Item>()
            items.add(Item("Foo"))
            return items
        }
    @RequestMapping("/")
    fun indexAction(@RequestAttribute("msg") msg:UIMessage.Message?):String{
        return "index"
    }

    @GetMapping("/new")
    fun newAction():String{
        return "newForm"
    }

    @PostMapping("/addNew")
    fun addNewAction(
            @ModelAttribute("nom") nom:String,
            @SessionAttribute("items") items:HashSet<Item>,
            attrs:RedirectAttributes):RedirectView{
        addMsg(
                items.add(Item(nom)),
                attrs,
                "Ajout",
                "$nom a été ajouté avec succès",
                "$nom est déjà dans la liste,<br>Il n'a pas été ajouté."
        )
        return RedirectView("/")
    }
    @GetMapping("/inc/{nom}")
    fun incAction(
            @PathVariable nom:String,
            @SessionAttribute("items") items:HashSet<Item>,
            attrs:RedirectAttributes
    ):RedirectView{
        var item=getItemByName(nom,items)
        item?.evaluation =item!!.evaluation+1
        addMsg(
                item!=null,
                attrs,
                "Mise à jour",
                "$nom incrémenté",
                "$nom n'existe pas dans les items"
        )
        return RedirectView("/")
    }
}