package edu.spring.dogs.entities

import jakarta.persistence.*


@Entity
open class Master() {
    constructor(firstname:String, lastname:String):this(){
        this.firstname=firstname
        this.lastname=lastname
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id = 0

    @Column(length = 30)
    open var firstname: String? = null

    @Column(length = 30)
    open var lastname: String? = null

    @OneToMany(mappedBy = "master", cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH])
    open val dogs= mutableSetOf<Dog>()

    fun addDog(dog:Dog):Boolean {
        if(dogs.add(dog)){
            dog.master=this
            return true
        }
        return false
    }

    fun giveUpDog(dog:Dog):Boolean {
        if(dogs.remove(dog)){
            dog.master=null
            return true
        }
        return false
    }

    @PreRemove
    open fun preRemove() {
        for (dog in dogs){
            dog.master=null
        }
    }
}