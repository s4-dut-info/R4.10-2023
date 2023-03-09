package edu.spring.dogs.rest

import edu.spring.dogs.entities.Master
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(path = "masters", collectionResourceRel = "masters")
interface RestMasterResource:JpaRepository<Master, Int> {
}