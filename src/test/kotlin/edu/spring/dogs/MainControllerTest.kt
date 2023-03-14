package edu.spring.dogs

import edu.spring.dogs.controllers.MainController
import edu.spring.dogs.entities.Dog
import edu.spring.dogs.entities.Master
import edu.spring.dogs.repositories.DogRepository
import edu.spring.dogs.repositories.MasterRepository
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
    lateinit var masterRepository: MasterRepository

    @MockBean
    lateinit var dogRepository: DogRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @Throws(Exception::class)
    fun defaultPageIsEmptyWithNoMastersAndDogs() {
        `when`(masterRepository.findAll()).thenReturn(listOf())
        this.mockMvc.perform(get("/")).
        andExpect(status().isOk).
        andExpect(content().string(containsString("Il n'y a pas de maître dans la base de données."))).
        andExpect(content().string(containsString("Il n'y a pas de chien à l'adoption dans la base de données."))).
        andExpect(content().string(containsString("Ajout rapide de propriétaire"))).
        andExpect(view().name("index"))
    }

    @Test
    fun displayOneUser() {
        `when`(masterRepository.findAll()).thenReturn(listOf(Master("Bob", "MockDuke")))
        this.mockMvc.perform(
                get("/")).andExpect(status().isOk).andExpect(content().string(containsString("MockDuke")))
    }

    @Test
    fun displayOneUserWithOneDog() {
        val master=Master("Bob", "MockDuke")
        val dog=Dog("MockDog")
        master.dogs.add(dog)
        `when`(masterRepository.findAll()).thenReturn(listOf(master))
        `when`(dogRepository.findAll()).thenReturn(listOf(dog))
        this.mockMvc.perform(
            get("/")).
        andExpect(status().isOk).
        andExpect(content().string(containsString("MockDuke")))
    }


    @Test
    fun displayOneUserWithOneDogAndOneSPADog() {
        val master=Master("Bob", "MockDuke")
        val dog=Dog("MockDog")
        val dogSolo=Dog("MockDogSolo")
        master.dogs.add(dog)
        `when`(masterRepository.findAll()).thenReturn(listOf(master))
        `when`(dogRepository.findAll()).thenReturn(listOf(dog, dogSolo))
        `when`(dogRepository.findByMasterIsNull()).thenReturn(listOf(dogSolo))
        this.mockMvc.perform(
            get("/")).
        andExpect(status().isOk).
        andExpect(content().string(containsString("MockDuke"))).
        andExpect(content().string(containsString("MockDogSolo")))
    }

    @Test
    fun displayOneUserWithOneDogAndRemoveUser() {
        val master=Master("Bob", "MockDuke")
        master.id=1000
        val dog=Dog("MockDog")
        master.dogs.add(dog)
        `when`(masterRepository.findAll()).thenReturn(listOf(master))
        this.mockMvc.perform(
            get("/")).andExpect(status().isOk).andExpect(content().string(containsString("MockDuke")))
        this.mockMvc.perform(
            get("/master/1000/delete")).andExpect(status().isFound)
        Assertions.assertEquals(0, masterRepository.count())
    }

}
