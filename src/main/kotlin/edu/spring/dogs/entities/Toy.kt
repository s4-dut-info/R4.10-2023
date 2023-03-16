package edu.spring.dogs.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
open class Toy() {
    constructor(type:String, label:String):this(){
        this.type=type
        this.label=label
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open val id = 0

    @Column(length = 30)
    open var type: String? = null

    @Column(length = 60, nullable = false, unique = true)
    open lateinit var label: String
}