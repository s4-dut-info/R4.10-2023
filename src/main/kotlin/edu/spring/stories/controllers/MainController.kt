package edu.spring.stories.controllers

import edu.spring.stories.entities.Story
import edu.spring.stories.entities.Developer
import edu.spring.stories.repositories.StoryRepository
import edu.spring.stories.repositories.DeveloperRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class MainController {
    @Autowired
    lateinit var developerRepository: DeveloperRepository

    @Autowired
    lateinit var storyRepository: StoryRepository

    @RequestMapping("/")
    fun index(model:ModelMap): String {
        val developers=developerRepository.findAll()
        model["developers"]= developers
        model["hasDevelopers"]= developers.count()>0
        model["stories"]= storyRepository.findByDeveloperIsNull()
        return "index"
    }

    @PostMapping("/developer/{id}/story")
    fun addOrGiveUp(@ModelAttribute story:Story, @PathVariable id:Int): String {
        val developer=developerRepository.findById(id).get()
        developer.addStory(story)
        developerRepository.save(developer)
        return "redirect:/"
    }

    @GetMapping("/story/{id}/giveup")
    fun removeDevStory(@PathVariable id:Int): String {
        val story=storyRepository.findById(id).get()
        val developer=story.developer
        developer?.giveUpStory(story)
        developerRepository.save(developer!!)
        return "redirect:/"
    }

    @PostMapping("/developer/add")
    fun addMaster(@ModelAttribute developer:Developer): String {
        developerRepository.save(developer)
        return "redirect:/"
    }

    @GetMapping("/developer/{id}/delete")
    fun deleteDeveloper(@PathVariable id:Int): String {
        developerRepository.deleteById(id)
        return "redirect:/"
    }

    @PostMapping("/story/{id}/action")
    fun storyAction(@PathVariable id:Int, @RequestParam("story-action") storyAction:String, @RequestParam(value="developerId", required = false) developerId:Int?): String {
        val story=storyRepository.findById(id).get()
        if(storyAction=="remove") {
            storyRepository.delete(story)
        }else{
            if(developerId!=null) {
                val developer = developerRepository.findById(developerId).get()
                developer.addStory(story)
                developerRepository.save(developer)
            }
        }
        return "redirect:/"
    }


}