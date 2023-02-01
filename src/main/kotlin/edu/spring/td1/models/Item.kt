package edu.spring.td1.models

import java.io.Serializable

data class Item(var nom: String):Serializable {
    var evaluation:Int=0
        set(value) {
            if(value in 0..10) field=value
        }
    val notZero:Boolean
        get() = evaluation>0
}