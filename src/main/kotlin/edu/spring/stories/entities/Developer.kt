package edu.spring.stories.entities

import jakarta.persistence.*


@Entity
open class Developer() {
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

    @OneToMany(mappedBy = "developer", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    open val stories= mutableSetOf<Story>()

    fun addStory(story:Story):Boolean {
        if(stories.add(story)){
            story.developer=this
            return true
        }
        return false
    }

    fun giveUpStory(story:Story):Boolean {
        if(stories.remove(story)){
            story.developer=null
            return true
        }
        return false
    }

    @PreRemove
    open fun preRemove() {
        for (story in stories){
            story.developer=null
        }
    }
}