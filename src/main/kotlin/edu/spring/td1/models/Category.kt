package edu.spring.td1.models

data class Category(var label:String) {
    private val items=HashSet<Item>()
    var insertable:Boolean=true

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
    companion object{
        fun create(label:String,vararg itemNames:String):Category{
            val cat=Category(label)
            cat.addAll(*itemNames)
            return cat
        }
        val all=Category("Toutes",false)
    }
}