package edu.spring.td2.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne

@Entity
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id:Int?=null

    @Column(length = 30)
    open var firstname:String?=null

    @Column(length = 30)
    open var lastname:String?=null

    @Column(nullable = false, unique = true)
    open lateinit var email:String

    @Column(nullable = false)
    open lateinit var password:String

    open var suspended:Boolean=false

    @ManyToOne
    @JoinColumn(name = "idOrganization", nullable = false)
    open lateinit var organization:Organization

    @ManyToMany
    @JoinTable(name = "user_groups")
    open val groups= mutableSetOf<Group>()
}