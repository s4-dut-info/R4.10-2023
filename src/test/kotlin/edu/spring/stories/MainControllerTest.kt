package edu.spring.stories

import edu.spring.stories.controllers.MainController
import edu.spring.stories.entities.Story
import edu.spring.stories.entities.Developer
import edu.spring.stories.repositories.StoryRepository
import edu.spring.stories.repositories.DeveloperRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(MockitoExtension::class)
@WebMvcTest(MainController::class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MainControllerTest {

    @MockBean
    lateinit var developerRepository: DeveloperRepository

    @MockBean
    lateinit var storyRepository: StoryRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @Throws(Exception::class)
    fun defaultPageIsEmptyWithNoDevsAndStories() {
        `when`(developerRepository.findAll()).thenReturn(listOf())
        this.mockMvc.perform(get("/")).
        andExpect(status().isOk).
        andExpect(content().string(containsString("Il n'y a pas de développeurs dans la base de données."))).
        andExpect(content().string(containsString("Il n'y a pas d'US à attribuer dans la base de données."))).
        andExpect(content().string(containsString("Ajout rapide de développeur"))).
        andExpect(view().name("index"))
    }

    @Test
    fun displayOneUser() {
        `when`(developerRepository.findAll()).thenReturn(listOf(Developer("Bob", "MockDuke")))
        this.mockMvc.perform(
                get("/")).andExpect(status().isOk).andExpect(content().string(containsString("MockDuke")))
    }

    @Test
    fun displayOneUserWithOneStory() {
        val developer=Developer("Bob", "MockDuke")
        val story=Story("Imprimer")
        developer.stories.add(story)
        `when`(developerRepository.findAll()).thenReturn(listOf(developer))
        `when`(storyRepository.findAll()).thenReturn(listOf(story))
        this.mockMvc.perform(
            get("/")).
        andExpect(status().isOk).
        andExpect(content().string(containsString("Imprimer")))
    }


    @Test
    fun displayOneUserWithOneStoryAndOneNonAffectedStory() {
        val developer=Developer("Bob", "MockDuke")
        val story=Story("Imprimer")
        val storySolo=Story("Se connecter")
        developer.stories.add(story)
        `when`(developerRepository.findAll()).thenReturn(listOf(developer))
        `when`(storyRepository.findAll()).thenReturn(listOf(story, storySolo))
        `when`(storyRepository.findByDeveloperIsNull()).thenReturn(listOf(storySolo))
        this.mockMvc.perform(
            get("/")).
        andExpect(status().isOk).
        andExpect(content().string(containsString("Imprimer"))).
        andExpect(content().string(containsString("Se connecter")))
    }

    @Test
    fun displayOneUserWithOneStoryAndRemoveUser() {
        val developer=Developer("Bob", "MockDuke")
        developer.id=1000
        val story=Story("Imprimer")
        developer.stories.add(story)
        `when`(developerRepository.findAll()).thenReturn(listOf(developer))
        this.mockMvc.perform(
            get("/")).andExpect(status().isOk).andExpect(content().string(containsString("Imprimer")))
        this.mockMvc.perform(
            get("/developer/1000/delete")).andExpect(status().isFound)
        Assertions.assertEquals(0, developerRepository.count())
    }

}
