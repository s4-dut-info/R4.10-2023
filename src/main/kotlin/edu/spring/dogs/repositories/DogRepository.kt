package edu.spring.dogs.repositories

import edu.spring.dogs.entities.Dog
import org.springframework.data.repository.CrudRepository

interface DogRepository:CrudRepository<Dog, Int> {
    fun findByMasterIsNull():List<Dog>
    fun findByNameAndMasterId(name:String,masterId:Int):Dog?
}