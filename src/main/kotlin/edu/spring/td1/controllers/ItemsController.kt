package edu.spring. td1.controllers

import edu.spring.td1.models.Category
import edu.spring.td1.models.Item
import edu.spring.td1.services.UIMessage
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView

@Controller
@SessionAttributes("categories")
class ItemsController {

    private fun getItemByName(nom:String,items:HashSet<Item>):
            Item?=items.find { nom==it.nom }

    private fun addMsg(resp:Boolean,attrs: RedirectAttributes,title:String,success:String,error:String){
        if(resp) {
            attrs.addFlashAttribute("msg",
                    UIMessage.message(title, success))
        } else {
            attrs.addFlashAttribute("msg",
                    UIMessage.message(title, error,"error","warning circle"))

        }
    }
    @get:ModelAttribute("categories")
    val categories: Set<Category>
        get() {
            val cats= HashSet<Category>()
            val cat=Category("Foo")
            cats.add(cat)
            cat.addAll("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
            cats.add(Category("Bar").apply { addAll("1","2","3","4","5","6","7","8","9","0") })
            return cats
        }
    @RequestMapping("/")
    fun indexAction(@RequestAttribute("msg") msg:UIMessage.Message?):String{
        return "index"
    }

    @GetMapping("/new")
    fun newAction(model:ModelMap):String{
        model["item"]=Item("")
        model["url"]="/addNew"
        return "itemForm"
    }

    @GetMapping("/update/{nom}")
    fun updateAction(
        @PathVariable nom:String,
        @SessionAttribute("items") items:HashSet<Item>,

        ):ModelAndView{
        val mv=ModelAndView("itemForm")
        val item=getItemByName(nom,items)
        mv.addObject("item",item)
        mv.addObject("url","/update")
        return mv
    }

    @PostMapping("/update")
    fun updateSubmitAction(
        @ModelAttribute("nom") nom:String,
        @ModelAttribute("id") id:String,
        @SessionAttribute("items") items:HashSet<Item>,
        attrs:RedirectAttributes):RedirectView {
        val item=getItemByName(id,items)
        if(item!=null){
            item.nom=nom
        }
        addMsg(
            item!=null,
            attrs,
            "Modification",
            "$nom a été modifié avec succès",
            "$nom n'est pas dans les items."
        )
        return RedirectView("/")
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
        val item=getItemByName(nom,items)
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
    @GetMapping("/dec/{nom}")
    fun decAction(
            @PathVariable nom:String,
            @SessionAttribute("items") items:HashSet<Item>,
            attrs:RedirectAttributes
    ):RedirectView{
        val item=getItemByName(nom,items)
        item?.evaluation =item!!.evaluation-1
        addMsg(
                item!=null,
                attrs,
                "Mise à jour",
                "$nom décrémenté",
                "$nom n'existe pas dans les items"
        )
        return RedirectView("/")
    }
}
