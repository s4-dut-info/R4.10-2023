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
import jakarta.persistence.Table

@Entity
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id:Int?=null
    @Column(length = 30)
    open var firstname:String?=null
    @Column(length = 30)
    open var lastname:String?=null

    @Column(nullable = false, length = 255, unique = true)
    open lateinit var email:String

    open var password:String?=null

    open var suspended:Boolean=false

    @ManyToOne
    @JoinColumn(name="idOrganization", nullable = false)
    open lateinit var organization:Organization

    @ManyToMany()
    @JoinTable(name="user_groups")
    open val groups = mutableSetOf<Group>()
}