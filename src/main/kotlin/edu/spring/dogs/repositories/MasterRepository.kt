package edu.spring.dogs.repositories

import edu.spring.dogs.entities.Master
import org.springframework.data.repository.CrudRepository

interface MasterRepository:CrudRepository<Master, Int> {
    fun findByFirstnameAndLastname(firstname:String, lastname:String):Master?
    fun findByDogsName(name:String):List<Master>
}