package edu.spring.dogs.repositories

import edu.spring.dogs.entities.Toy
import org.springframework.data.repository.CrudRepository

interface ToyRepository:CrudRepository<Toy, Int> {
    fun findByType(type:String):List<Toy>
}