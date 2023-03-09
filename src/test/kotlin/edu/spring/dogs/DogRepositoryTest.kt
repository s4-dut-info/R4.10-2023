package edu.spring.dogs

import edu.spring.dogs.entities.Dog
import edu.spring.dogs.entities.Master
import edu.spring.dogs.repositories.DogRepository
import edu.spring.dogs.repositories.MasterRepository
import org.hibernate.TransientPropertyValueException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.InvalidDataAccessApiUsageException

@DataJpaTest
class DogRepositoryTest {
    @Autowired
    lateinit var dogRepository: DogRepository

    @Autowired
    lateinit var masterRepository: MasterRepository

    @Test
    fun emptyAtInitialization(){
        assert(dogRepository.count()==0L)
    }

    @Test
    fun addDog(){
        var dog=Dog("Rex")
        dog=dogRepository.save(dog)
        assert(dogRepository.count()==1L)
        assert(dog.name=="Rex")
    }

    @Test
    fun removeDog(){
        var dog=Dog("Rex")
        dog=dogRepository.save(dog)
        assert(dogRepository.count()==1L)
        assert(dogRepository.findByMasterIsNull().count()==1)
        dogRepository.delete(dog)
        assert(dogRepository.count()==0L)
        assert(dogRepository.findByMasterIsNull().isEmpty())
    }

    @Test
    fun cannotAddDogWithUnsavedMaster(){
        val master=Master("John","DOE")
        var dog=Dog("Rex")
        dog.master=master
        assertThrows<InvalidDataAccessApiUsageException> {
            dog=dogRepository.save(dog)
            assert(dogRepository.count()==0L)
            assert(dogRepository.findByMasterIsNull().isEmpty())
        }
    }

    @Test
    fun findDogByNameAndMasterIdTest(){
        var master=Master("John","DOE")
        master.addDog(Dog("Rex"))
        master.addDog(Dog("Sultan"))
        master=masterRepository.save(master)
        val dog=dogRepository.findByNameAndMasterId("Rex",master.id)
        assert(dog!=null)
        assert(dog!!.name=="Rex")
        assert(dog.master==master)
    }
}