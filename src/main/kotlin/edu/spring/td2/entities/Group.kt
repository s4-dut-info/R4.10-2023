package edu.spring.td2.entities

import jakarta.persistence.*

@Entity
@Table(name = "Groupe")
open class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id:Int?=null

    @Column(length = 60, nullable = false, unique = true)
    open lateinit var name:String

    @Column(length = 255)
    open var email:String?=null
    @Column(length = 20)
    open var aliases:String?=null
}