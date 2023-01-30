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

    private fun getCategoryByLabel(label:String,categories:HashSet<Category>):
            Category?=categories.find { label==it.label }

    private fun addMsg(category:String,resp:Boolean,attrs: RedirectAttributes,title:String,success:String,error:String){
        attrs.addFlashAttribute("category",category)
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
            cat.addAll("A","B","C","D","E","F","G","H","I")
            cats.add(Category("Bar").apply { addAll("1","2","3","4","5","6","7","8","9") })
            return cats
        }
    @RequestMapping("/")
    fun indexAction(@RequestAttribute("msg") msg:UIMessage.Message?):String{
        return "index"
    }

    @GetMapping("/new/{category}")
    fun newAction(model:ModelMap,@PathVariable category: String):String{
        model["item"]=Item("")
        model["url"]="/addNew/$category"
        return "itemForm"
    }

    @GetMapping("/update/{category}/{nom}")
    fun updateAction(
        @PathVariable nom:String,
        @PathVariable category:String,
        @SessionAttribute("categories") categories: HashSet<Category>,

        ):ModelAndView{
        val mv=ModelAndView("itemForm")
        val item= getCategoryByLabel(category,categories)?.get(nom)
        mv.addObject("item",item)
        mv.addObject("url","/update/$category")
        return mv
    }

    @PostMapping("/update/{category}")
    fun updateSubmitAction(
        @ModelAttribute("nom") nom:String,
        @ModelAttribute("id") id:String,
        @PathVariable category:String,
        @SessionAttribute("categories") categories: HashSet<Category>,
        attrs:RedirectAttributes):RedirectView {
        val item=getCategoryByLabel(category,categories)?.get(nom)
        if(item!=null){
            item.nom=nom
        }
        addMsg(
            category,
            item!=null,
            attrs,
            "Modification",
            "$nom a été modifié avec succès",
            "$nom n'est pas dans les items."
        )
        return RedirectView("/")
    }

    @PostMapping("/addNew/{category}")
    fun addNewAction(
            @ModelAttribute("nom") nom:String,
            @SessionAttribute("categories") categories: HashSet<Category>,
            @PathVariable category:String,
            attrs:RedirectAttributes):RedirectView{
        addMsg(
                category,
                getCategoryByLabel(category,categories)?.add(nom)?:false,
                attrs,
                "Ajout",
                "$nom a été ajouté avec succès dans $category",
                "$nom est déjà dans la catégorie $category,<br>Il n'a pas été ajouté."
        )
        return RedirectView("/")
    }
    @GetMapping("/inc/{category}/{nom}")
    fun incAction(
            @PathVariable nom:String,
            @PathVariable category:String,
            @SessionAttribute("categories") categories: HashSet<Category>,
            attrs:RedirectAttributes
    ):RedirectView{
        val item= getCategoryByLabel(category,categories)?.get(nom)
        item?.evaluation =item!!.evaluation+1
        addMsg(
                category,
                item!=null,
                attrs,
                "Mise à jour",
                "$nom incrémenté",
                "$nom n'existe pas dans les items"
        )
        return RedirectView("/")
    }
    @GetMapping("/dec/{category}/{nom}")
    fun decAction(
            @PathVariable nom:String,
            @PathVariable category:String,
            @SessionAttribute("categories") categories: HashSet<Category>,
            attrs:RedirectAttributes
    ):RedirectView{
        val item= getCategoryByLabel(category,categories)?.get(nom)
        item?.evaluation =item!!.evaluation-1
        addMsg(
                category,
                item!=null,
                attrs,
                "Mise à jour",
                "$nom décrémenté",
                "$nom n'existe pas dans les items"
        )
        return RedirectView("/")
    }
}
