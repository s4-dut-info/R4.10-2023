package edu.spring.td1.models

data class Item(var nom: String) {
    var evaluation:Int=0
        set(value) {
            if(value in 0..10) field=value
        }
}