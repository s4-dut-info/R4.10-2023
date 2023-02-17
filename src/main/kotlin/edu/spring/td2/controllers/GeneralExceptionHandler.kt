package edu.spring.td2.controllers

import edu.spring.td2.exceptions.ElementNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
class GeneralExceptionHandler {
    @ExceptionHandler(value = [ElementNotFoundException::class])
    fun errorHandlerAction(ex:Exception): ModelAndView {
        val mv= ModelAndView("/main/error")
        mv.addObject("content",ex.message)
        mv.addObject("title","Elément non trouvé")
        return mv
    }

    @ExceptionHandler(value = [NoHandlerFoundException::class])
    fun notFoundAction(ex:NoHandlerFoundException): ModelAndView {
        val mv= ModelAndView("/main/error")
        mv.addObject("content","La page ${ex.requestURL} n'existe pas<br><a href=\"/orgas\">Retour à l'accueil</a>")
        mv.addObject("title","Oups!")
        return mv
    }
}