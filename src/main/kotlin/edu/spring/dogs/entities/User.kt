package edu.spring.dogs.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
open class User() {

    constructor(username:String):this(){
        this.username=username
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id:Int=0

    @Column(length = 65, nullable = false)
    open lateinit var username:String

    @Column(length = 256)
    open var password:String?=null

    @Column(unique = true, length = 115)
    open var email:String?=null

    @Column(nullable = false)
    open lateinit var role:String
}