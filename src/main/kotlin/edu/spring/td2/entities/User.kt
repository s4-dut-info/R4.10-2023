package edu.spring.td2.entities

import jakarta.persistence.*

@Entity
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id:Int?=null

    @Column(length = 30)
    open var firstname:String?=null
    @Column(length = 30)
    open var lastname:String?=null

    @Column(length = 255, nullable = false, unique = true)
    open lateinit var email:String

    open var suspended=false

    @ManyToOne
    @JoinColumn(name="idOrganization", nullable = false)
    lateinit open var organization: Organization


}