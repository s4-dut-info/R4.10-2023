package edu.spring.dogs.rest

import edu.spring.dogs.entities.Dog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(path = "dogs", collectionResourceRel = "dogs")
interface RestDogResource:JpaRepository<Dog, Int> {
}