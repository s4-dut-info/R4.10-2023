package edu.spring.td2.services

import org.springframework.stereotype.Service

@Service
class UserService {
    fun generatePassword(size:Int):String{
        return List(size) { (0x21..0x7e).random().toChar() }.joinToString("")
    }
}