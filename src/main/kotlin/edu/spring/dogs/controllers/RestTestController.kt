package edu.spring.dogs.controllers

import edu.spring.dogs.entities.Master
import edu.spring.dogs.repositories.DogRepository
import edu.spring.dogs.repositories.MasterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest")
class RestTestController {
    @Autowired
    lateinit var masterRepository: MasterRepository

    @GetMapping("/masters")
    fun getMasters() = masterRepository.findAll()

    @PostMapping("/masters", consumes = ["application/json"])
    fun addMaster(@RequestBody master:Master) :ResponseEntity<Master>{
      if(masterRepository.save(master)!=null) {
          return ResponseEntity.ok(master)
      }
       return ResponseEntity.badRequest().build()
    }

    @DeleteMapping("/masters/{id}")
    fun deleteMaster(@PathVariable("id") id:Int) :ResponseEntity<Master>{
      val master=masterRepository.findById(id)
      if(master.isPresent) {
          masterRepository.delete(master.get())
          return ResponseEntity.ok(master.get())
      }
       return ResponseEntity.badRequest().build()
    }

}