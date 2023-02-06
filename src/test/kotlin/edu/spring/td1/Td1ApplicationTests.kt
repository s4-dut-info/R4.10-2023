package edu.spring.td1

import edu.spring.td1.models.Category
import edu.spring.td1.models.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post


@SpringBootTest
@AutoConfigureMockMvc
class Td1ApplicationTests {

	@Autowired
	private val mvc: MockMvc? = null

	@Test
	fun contextLoads() {
	}

	@Test
	@Throws(Exception::class)
	fun loadAllItems() {
		val result=mvc!!.perform(get("/")).andReturn()
		val content=result.response.contentAsString
		val session = result.request.session
		val cats = session?.getAttribute("categories") as Set<Category>
		for(cat in cats){
			Assertions.assertTrue(content.contains(cat.label))
			for (item in cat.items) {
				Assertions.assertTrue(content.contains(item.nom))
			}
		}
	}
	@Test
	fun addCategory() {
		val result=mvc!!.perform(post("/addNewCategory").param("label","Foo")).andReturn()
		val session = result.request.session
		val cats = session?.getAttribute("categories") as Set<Category>
		Assertions.assertTrue(cats.any { it.label=="Foo" })
		val content=mvc!!.perform(get("/").session(session as MockHttpSession)).andReturn().response.contentAsString
		Assertions.assertTrue(content.contains("Foo"))
	}

	@Test
	fun addItem() {
		val result=mvc!!.perform(post("/addNew/Fruits").param("nom","Tomato")).andReturn()
		val session = result.request.session
		val cats = session?.getAttribute("categories") as Set<Category>
		Assertions.assertTrue(cats.find { it.label=="Fruits" }?.get("Tomato")==Item("Tomato"))
		val content=mvc!!.perform(get("/").session(session as MockHttpSession)).andReturn().response.contentAsString
		Assertions.assertTrue(content.contains("Tomato"))
	}
}
