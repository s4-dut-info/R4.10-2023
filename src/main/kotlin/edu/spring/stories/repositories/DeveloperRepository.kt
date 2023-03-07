package edu.spring.stories.repositories

import edu.spring.stories.entities.Developer
import org.springframework.data.repository.CrudRepository

interface DeveloperRepository:CrudRepository<Developer, Int> {
    fun findByFirstnameAndLastname(firstname:String, lastname:String):Developer?
    fun findByStoriesName(name:String):List<Developer>
}