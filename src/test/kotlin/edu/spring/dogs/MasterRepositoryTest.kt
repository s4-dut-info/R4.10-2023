package edu.spring.dogs

import edu.spring.dogs.entities.Dog
import edu.spring.dogs.entities.Master
import edu.spring.dogs.repositories.DogRepository
import edu.spring.dogs.repositories.MasterRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.junit.jupiter.api.Assertions.*
@DataJpaTest
class MasterRepositoryTest {
    @Autowired
    lateinit var masterRepository: MasterRepository

    @Autowired
    lateinit var dogRepository: DogRepository

    private fun createMasterWithDogs():Master{
        var master=Master("John","DOE")
        master.id=1000
        master.addDog(Dog("Rex"))
        master.addDog(Dog("Sultan"))
        master=masterRepository.save(master)
        return master
    }

    @Test
    fun emptyAtInitialization(){
        assert(masterRepository.count()==0L)
    }

    @Test
    fun addMaster(){
        var master=Master("John","DOE")
        master=masterRepository.save(master)
        assert(masterRepository.count()==1L)
        Assertions.assertThat(master).hasFieldOrPropertyWithValue("firstname", "John")
        Assertions.assertThat(master).hasFieldOrPropertyWithValue("lastname", "DOE")
    }

    @Test
    fun addMasterAndDogs(){
        var master=createMasterWithDogs()
        assert(masterRepository.count()==1L)
        assertTrue(master.dogs.elementAt(0).name=="Rex")
        assertTrue(master.dogs.elementAt(1).name=="Sultan")
        assertEquals(master.dogs.elementAt(0).master,master)
        assertEquals(master.dogs.elementAt(1).master,master)
        assertTrue(dogRepository.count()==2L)
        assertTrue(dogRepository.findByMasterIsNull().isEmpty())
    }

    @Test
    fun giveUpDogs(){
        var master=createMasterWithDogs()
        master.giveUpDog(dogRepository.findByNameAndMasterId("Rex",master.id)!!)
        master.giveUpDog(dogRepository.findByNameAndMasterId("Sultan", master.id)!!)
        masterRepository.save(master)
        assertTrue(dogRepository.findByMasterIsNull().count()==2)
    }

    @Test
    fun deleteMaster(){
        var master=createMasterWithDogs()
        masterRepository.deleteById(master.id)
        assertTrue(dogRepository.findByMasterIsNull().count()==2)
    }

    @Test
    fun findMasterByDogsName(){
        var master=createMasterWithDogs()
        var masters=masterRepository.findByDogsName("Rex")
        assertTrue(masters.count()==1)
        assertEquals(masters.elementAt(0),master)
    }
}