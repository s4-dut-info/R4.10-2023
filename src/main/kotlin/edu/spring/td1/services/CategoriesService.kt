package edu.spring.td1.services

import edu.spring.td1.models.Category
import org.springframework.stereotype.Service

@Service
class CategoriesService {
    fun initCategories(): Set<Category> {
        val cats= LinkedHashSet<Category>()
        cats.add(Category.all)
        cats.add(Category.create("Fruits","Pomme","Banane","Orange","Kiwi","Fraise","Mangue","Poire","Pêche"))
        cats.add(Category.create("Légumes","Carotte","Tomate","Poivron","Courgette","Haricot","Aubergine","Chou","Oignon"))
        return cats
    }
}