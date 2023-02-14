package edu.spring.td2.entities

import jakarta.persistence.*

@Entity
open class Organization() {
    constructor(name:String) : this(){
        this.name=name
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id:Int?=null
    @Column(nullable = false, length = 60, unique = true)
    open lateinit var name:String
    @Column(length = 45)
    open var domain:String?=""
    @Column(length = 45)
    open var aliases:String?=""

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    open val users= mutableSetOf<User>()

    @OneToMany(mappedBy = "organization")
    open val groups = mutableSetOf<Group>()

    fun addUser(user:User){
        if(users.add(user)){
            user.organization=this
        }
    }
}