package edu.spring.stories.repositories

import edu.spring.stories.entities.Tag
import org.springframework.data.repository.CrudRepository

interface TagRepository:CrudRepository<Tag, Int> {
    fun findByColor(color:String):List<Tag>
}