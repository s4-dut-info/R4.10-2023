package edu.spring.td2.entities

import jakarta.persistence.*

@Entity
open class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id:Int?=null
    @Column(nullable = false, length = 60, unique = true)
    open lateinit var name:String
    @Column(length = 255)
    open var email:String?=null
    @Column(length = 45)
    open var aliases:String?=null

    @ManyToOne
    @JoinColumn(name="idOrganization", nullable = false)
    open lateinit var organization:Organization

    @ManyToMany(mappedBy = "groups")
    open val users=mutableSetOf<User>()
}