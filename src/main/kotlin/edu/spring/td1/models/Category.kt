package edu.spring.td1.models

import java.io.Serializable

data class Category(var label:String):Serializable {
    private val items=HashSet<Item>()
    private var insertable:Boolean=true

    val clearable:Boolean
        get() = items.isNotEmpty()
    val count:Int
        get() = items.size
    private constructor(label:String,insertable:Boolean):this(label){
        this.insertable=insertable
    }

    operator fun get (itemName:String):Item? {
        return items.find { it.nom==itemName }
    }

    fun addAll(vararg itemNames:String){
        itemNames.forEach { add(it) }
    }

    fun add(itemName:String):Boolean{
        val item=Item(itemName)
        if(addItem(item)){
            if(insertable) {
                return all.addItem(item)
            }
            return true
        }
        return false
    }
    private fun addItem(item:Item):Boolean{
        if(items.add(item)){
            return true
        }
        return false
    }

    fun clear() {
        if(this!=all) {
            all.items.removeAll(items)
            items.clear()
        }
    }

    companion object{
        fun create(label:String,vararg itemNames:String):Category{
            val cat=Category(label)
            cat.addAll(*itemNames)
            return cat
        }
        val all=Category("Toutes",false)
    }
}