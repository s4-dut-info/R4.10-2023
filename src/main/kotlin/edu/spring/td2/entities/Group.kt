package edu.spring.td2.entities

import jakarta.persistence.*

@Entity
class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id:Int?=null

    @Column(nullable = false, unique = true, length = 45)
    open lateinit var name:String

    @Column(nullable = false, unique = true)
    open lateinit var email:String

    @Column(length = 30)
    open var aliases:String?=null

    @ManyToOne
    @JoinColumn(name = "idOrganization", nullable = false)
    open lateinit var organization:Organization

    @ManyToMany(mappedBy = "groups")
    open val users= mutableSetOf<User>()
}