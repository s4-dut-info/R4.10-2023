package edu.spring.td2.repositories

import edu.spring.td2.entities.Organization
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRepository:CrudRepository<Organization,Int> {
}