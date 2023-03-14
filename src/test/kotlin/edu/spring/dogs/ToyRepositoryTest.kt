package edu.spring.dogs

import edu.spring.dogs.entities.Dog
import edu.spring.dogs.entities.Toy
import edu.spring.dogs.repositories.DogRepository
import edu.spring.dogs.repositories.ToyRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ToyRepositoryTest {
    @Autowired
    lateinit var toyRepository: ToyRepository

    @Autowired
    lateinit var dogRepository: DogRepository

    @Test
    fun emptyAtInitialization(){
        assert(toyRepository.count()==0L)
    }

    @Test
    fun addToy(){
        var toy=Toy("Ball","Red")
        toy=toyRepository.save(toy)
        assert(toyRepository.count()==1L)
        Assertions.assertThat(toy).hasFieldOrPropertyWithValue("label", "Red")
        Assertions.assertThat(toy).hasFieldOrPropertyWithValue("type", "Ball")
    }

    @Test
    fun removeToy(){
        var toy=Toy("Ball","Red")
        toy=toyRepository.save(toy)
        assert(toyRepository.count()==1L)
        toyRepository.delete(toy)
        assert(toyRepository.count()==0L)
    }

    @Test
    fun findByType(){
        var toy=Toy("Ball","Red")
        toy=toyRepository.save(toy)
        toy=Toy("Ball","Blue")
        toy=toyRepository.save(toy)
        toy=Toy("Bone","White")
        toy=toyRepository.save(toy)
        assert(toyRepository.count()==3L)
        var toys=toyRepository.findByType("Ball")
        assert(toys.count()==2)
        toys=toyRepository.findByType("Bone")
        assert(toys.count()==1)
    }

    @Test
    fun canAddToyToDog(){
        var dog=dogRepository.save(Dog("Rex"))
        var toy=toyRepository.save(Toy("Ball","Red"))
        dog.toys.add(toy)
        dog=dogRepository.save(dog)
        assert(dog.toys.count()==1)
        assert(dog.toys.first().label=="Red")
        assert(dog.toys.first().type=="Ball")
    }

    @Test
    fun canRemoveToyFromDog(){
        var dog=dogRepository.save(Dog("Rex"))
        var toy=toyRepository.save(Toy("Ball","Red"))
        dog.toys.add(toy)
        dog=dogRepository.save(dog)
        assert(dog.toys.count()==1)
        dog.toys.remove(toy)
        dog=dogRepository.save(dog)
        assert(dog.toys.isEmpty())
    }

    @Test
    fun cannotRemoveToyFromDog(){
        var dog=dogRepository.save(Dog("Rex"))
        var toy=toyRepository.save(Toy("Ball","Red"))
        dog.toys.add(toy)
        dog=dogRepository.save(dog)
        assert(dog.toys.count()==1)
        toyRepository.delete(toy)
        dog=dogRepository.save(dog)
        assert(dog.toys.count()==1)
    }
}