package edu.spring.td2.entities

import jakarta.persistence.*

@Entity
open class Organization() {
    fun addUser(user: User) {
        if(users.add(user)){
            user.organization=this
        }
    }

    fun addGroup(group: Group) {
        if(groups.add(group)){
            group.organization=this
        }
    }

    constructor(name:String):this(){
        this.name=name
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id:Int?=null

    @Column(length = 60, nullable = false, unique = true)
    open lateinit var name:String
    @Column(length = 20)
    open var domain:String?=""
    @Column(length = 20)
    open var aliases:String?=null

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    open val users= mutableSetOf<User>()

    @OneToMany(mappedBy="organization", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open val groups= mutableSetOf<Group>()
}