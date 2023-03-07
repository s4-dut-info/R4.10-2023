package edu.spring.stories

import edu.spring.stories.entities.Story
import edu.spring.stories.entities.Developer
import edu.spring.stories.repositories.StoryRepository
import edu.spring.stories.repositories.DeveloperRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.InvalidDataAccessApiUsageException

@DataJpaTest
class StoryRepositoryTest {
    @Autowired
    lateinit var storyRepository: StoryRepository

    @Autowired
    lateinit var developerRepository: DeveloperRepository

    @Test
    fun emptyAtInitialization(){
        assert(storyRepository.count()==0L)
    }

    @Test
    fun addDog(){
        var story=Story("Imprimer")
        story=storyRepository.save(story)
        assert(storyRepository.count()==1L)
        assert(story.name=="Imprimer")
    }

    @Test
    fun removeDog(){
        var story=Story("Imprimer")
        story=storyRepository.save(story)
        assert(storyRepository.count()==1L)
        assert(storyRepository.findByDeveloperIsNull().count()==1)
        storyRepository.delete(story)
        assert(storyRepository.count()==0L)
        assert(storyRepository.findByDeveloperIsNull().isEmpty())
    }

    @Test
    fun cannotAddDogWithUnsavedMaster(){
        val developer=Developer("John","DOE")
        var story=Story("Imprimer")
        story.developer=developer
        assertThrows<InvalidDataAccessApiUsageException> {
            story=storyRepository.save(story)
            assert(storyRepository.count()==0L)
            assert(storyRepository.findByDeveloperIsNull().isEmpty())
        }
    }

    @Test
    fun findDogByNameAndMasterIdTest(){
        var developer=Developer("John","DOE")
        developer.addStory(Story("Imprimer"))
        developer.addStory(Story("Se connecter"))
        developer=developerRepository.save(developer)
        val story=storyRepository.findByNameAndDeveloperId("Imprimer",developer.id)
        assert(story!=null)
        assert(story!!.name=="Imprimer")
        assert(story.developer==developer)
    }
}