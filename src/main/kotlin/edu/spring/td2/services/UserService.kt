package edu.spring.td2.services

import org.springframework.stereotype.Service

@Service
class UserService {
    fun generatePassword():String{
        return List(8) { (0x21..0x7e).random().toChar() }.joinToString("")
    }
}