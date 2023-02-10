package edu.spring.td2.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class IndexController {
    @RequestMapping(path = ["","index"])
    fun indexAction():String="index"
}