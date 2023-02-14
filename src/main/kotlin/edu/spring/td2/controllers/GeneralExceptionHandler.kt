package edu.spring.td2.controllers

import edu.spring.td2.exceptions.ElementNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
class GeneralExceptionHandler {
    @ExceptionHandler(value = [
        ElementNotFoundException::class,
        MethodArgumentTypeMismatchException::class
    ])
    fun errorAction(ex:Exception): ModelAndView {
        return message("Erreur",ex.message?:"Erreur inconnue")
    }

    @ExceptionHandler(value = [
        NoHandlerFoundException::class,
    ])
    fun notFoundAction(ex:NoHandlerFoundException): ModelAndView {
        return message("Page introuvable","La page ${ex.requestURL} n'existe pas")
    }

    private fun message(title:String,message:String): ModelAndView {
        val mv= ModelAndView("/main/error")
        mv.addObject("title",title)
        mv.addObject("message",message)
        return mv
    }
}