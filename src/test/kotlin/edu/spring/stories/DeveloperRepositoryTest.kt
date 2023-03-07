package edu.spring.stories

import edu.spring.stories.entities.Story
import edu.spring.stories.entities.Developer
import edu.spring.stories.repositories.StoryRepository
import edu.spring.stories.repositories.DeveloperRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.junit.jupiter.api.Assertions.*
@DataJpaTest
class DeveloperRepositoryTest {
    @Autowired
    lateinit var developerRepository: DeveloperRepository

    @Autowired
    lateinit var storyRepository: StoryRepository

    private fun createDevWithStories():Developer{
        var developer=Developer("John","DOE")
        developer.id=1000
        developer.addStory(Story("Imprimer"))
        developer.addStory(Story("Se connecter"))
        developer=developerRepository.save(developer)
        return developer
    }

    @Test
    fun emptyAtInitialization(){
        assert(developerRepository.count()==0L)
    }

    @Test
    fun addMaster(){
        var developer=Developer("John","DOE")
        developer=developerRepository.save(developer)
        assert(developerRepository.count()==1L)
        Assertions.assertThat(developer).hasFieldOrPropertyWithValue("firstname", "John")
        Assertions.assertThat(developer).hasFieldOrPropertyWithValue("lastname", "DOE")
    }

    @Test
    fun addMasterAndDogs(){
        var dev=createDevWithStories()
        assert(developerRepository.count()==1L)
        assertTrue(dev.stories.elementAt(0).name=="Imprimer")
        assertTrue(dev.stories.elementAt(1).name=="Se connecter")
        assertEquals(dev.stories.elementAt(0).developer,dev)
        assertEquals(dev.stories.elementAt(1).developer,dev)
        assertTrue(storyRepository.count()==2L)
        assertTrue(storyRepository.findByDeveloperIsNull().isEmpty())
    }

    @Test
    fun giveUpStories(){
        var dev=createDevWithStories()
        dev.giveUpStory(storyRepository.findByNameAndDeveloperId("Imprimer",dev.id)!!)
        dev.giveUpStory(storyRepository.findByNameAndDeveloperId("Se connecter", dev.id)!!)
        developerRepository.save(dev)
        assertTrue(storyRepository.findByDeveloperIsNull().count()==2)
    }

    @Test
    fun deleteDev(){
        var dev=createDevWithStories()
        developerRepository.deleteById(dev.id)
        assertTrue(storyRepository.findByDeveloperIsNull().count()==2)
    }

    @Test
    fun findDevByStoryName(){
        var dev=createDevWithStories()
        var devS=developerRepository.findByStoriesName("Imprimer")
        assertTrue(devS.count()==1)
        assertEquals(devS.elementAt(0),dev)
    }

    @Test
    fun findDevByFirstnameAndLastname(){
        var dev=createDevWithStories()
        var theDev=developerRepository.findByFirstnameAndLastname("John","DOE")
        assertEquals(theDev,dev)
    }
}