package edu.spring.dogs.controllers

import edu.spring.dogs.entities.Master
import edu.spring.dogs.repositories.MasterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RestMasterController {
    @Autowired
    lateinit var masterRepository: MasterRepository
    @GetMapping("/masters")
    fun index()=masterRepository.findAll()

    @PostMapping("/masters")
    fun addMaster(@RequestBody master:Master):ResponseEntity<Master>{
        masterRepository.save(master)
        return ResponseEntity.ok(master)
    }

    @DeleteMapping("/masters/{id}")
    fun deleteMaster(@PathVariable id:Int):ResponseEntity<String>{
        if(masterRepository.existsById(id)) {
            masterRepository.deleteById(id)
            return ResponseEntity.ok("Suppresion ok")
        }
        return ResponseEntity.notFound().build()
    }
}