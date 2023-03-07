package edu.spring.stories.repositories

import edu.spring.stories.entities.Story
import org.springframework.data.repository.CrudRepository

interface StoryRepository:CrudRepository<Story, Int> {
    fun findByDeveloperIsNull():List<Story>
    fun findByNameAndDeveloperId(name:String, developerId:Int):Story?
}