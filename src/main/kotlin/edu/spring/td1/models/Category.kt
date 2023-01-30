package edu.spring.td1.models

data class Category(var label:String) {
    private val items=HashSet<Item>()

        operator fun get (itemName:String):Item? {
            return items.find { it.nom==itemName }
        }

    fun addAll(vararg itemNames:String){
        itemNames.forEach { items.add(Item(it)) }
    }

    fun add(itemName:String):Boolean=items.add(Item(itemName))
}