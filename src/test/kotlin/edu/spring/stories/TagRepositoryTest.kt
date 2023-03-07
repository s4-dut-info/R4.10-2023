package edu.spring.stories

import edu.spring.stories.entities.Story
import edu.spring.stories.entities.Tag
import edu.spring.stories.repositories.StoryRepository
import edu.spring.stories.repositories.TagRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class TagRepositoryTest {
    @Autowired
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var storyRepository: StoryRepository

    @Test
    fun emptyAtInitialization(){
        assert(tagRepository.count()==0L)
    }

    @Test
    fun addToy(){
        var tag=Tag("Red","Admin")
        tag=tagRepository.save(tag)
        assert(tagRepository.count()==1L)
        Assertions.assertThat(tag).hasFieldOrPropertyWithValue("label", "Admin")
        Assertions.assertThat(tag).hasFieldOrPropertyWithValue("color", "Red")
    }

    @Test
    fun removeToy(){
        var tag=Tag("Red","Admin")
        tag=tagRepository.save(tag)
        assert(tagRepository.count()==1L)
        tagRepository.delete(tag)
        assert(tagRepository.count()==0L)
    }

    @Test
    fun findByType(){
        var tag=Tag("Red","Admin")
        tag=tagRepository.save(tag)
        tag=Tag("White","User")
        tag=tagRepository.save(tag)
        tag=Tag("Red","Discussion")
        tag=tagRepository.save(tag)
        assert(tagRepository.count()==3L)
        var tags=tagRepository.findByColor("Red")
        assert(tags.count()==2)
        tags=tagRepository.findByColor("White")
        assert(tags.count()==1)
    }

    @Test
    fun canAddToyToDog(){
        var story=storyRepository.save(Story("Imprimer"))
        var tag=tagRepository.save(Tag("Red","User"))
        story.tags.add(tag)
        story=storyRepository.save(story)
        assert(story.tags.count()==1)
        assert(story.tags.first().label=="User")
        assert(story.tags.first().color=="Red")
    }

    @Test
    fun canRemoveToyFromDog(){
        var story=storyRepository.save(Story("Imprimer"))
        var tag=tagRepository.save(Tag("Red","User"))
        story.tags.add(tag)
        story=storyRepository.save(story)
        assert(story.tags.count()==1)
        story.tags.remove(tag)
        story=storyRepository.save(story)
        assert(story.tags.isEmpty())
    }

    @Test
    fun cannotRemoveToyFromDog(){
        var story=storyRepository.save(Story("Imprimer"))
        var tag=tagRepository.save(Tag("Red","User"))
        story.tags.add(tag)
        story=storyRepository.save(story)
        assert(story.tags.count()==1)
        tagRepository.delete(tag)
        story=storyRepository.save(story)
        assert(story.tags.count()==1)
    }
}