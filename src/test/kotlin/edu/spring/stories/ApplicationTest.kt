package edu.spring.stories

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.By.ByCssSelector
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Duration
import java.util.concurrent.TimeUnit


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ApplicationTest {

    // CSS selectors
    private val COUNT_DEVELOPERS=".count-developers"
    private val COUNT_STORIES=".count-stories"
    private val COUNT_DEVELOPER_STORIES="td .dev-story"
    private val DELETE_DEVELOPER=".delete-developer.button"
    private val DELETE_STORY="button[value='remove'][name='story-action']:first-child"
    private val ADD_DEVELOPER="form[action='/developer/add'] button"
    private val ADD_STORY="button[value='add']"
    private val GIVEUP_STORY=".giveup-story:first-child"

    lateinit var driver: WebDriver

    @LocalServerPort
    var randomServerPort = 0

    var baseUrl: String? = null

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        WebDriverManager.chromedriver().setup()
        val options = ChromeOptions()
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-dev-shm-usage")
        options.addArguments("--headless")
        driver = ChromeDriver(options)
        baseUrl = "http://127.0.0.1:$randomServerPort"
        navigateTo("")
        (driver as ChromeDriver).manage().window().maximize()
        (driver as ChromeDriver).manage().timeouts().implicitlyWait(120, TimeUnit.MILLISECONDS)
    }

    @AfterEach
    @Throws(Exception::class)
    fun tearDown() {
        driver.quit()
    }

    private fun navigateTo(relativeURL: String) {
        driver.navigate().to(baseUrl + relativeURL)
    }

    private fun fillElement(name: String, content: String) {
        val elm: WebElement? = driver.findElement(By.name(name))
        elm?.sendKeys(content)
    }

    private fun btnClick(cssSelector: String) {
        driver.findElement(ByCssSelector.cssSelector(cssSelector))?.click()
    }

    private fun assertElementContainsText(cssSelector: String, text: String) {
        val elmText=driver.findElement(ByCssSelector.cssSelector(cssSelector))?.text
        Assertions.assertTrue(elmText?.contains(text)!!)
    }

    private fun assertElementAttributeContainsText(
        cssSelector: String, attribute: String,
        text: String
    ) {
        Assertions.assertTrue(
            (driver.findElement(ByCssSelector.cssSelector(cssSelector))?.getAttribute(attribute)?.contains(text)!!)
        )
    }

    fun waitForTextToAppear(textToAppear: String?, element: WebElement?, timeout: Long) {
        val wait = WebDriverWait(driver, Duration.ofMillis(timeout))
        wait.until(ExpectedConditions.textToBePresentInElement(element, textToAppear))
    }

    fun waitForTextToAppear(textToAppear: String?, element: WebElement?) {
        waitForTextToAppear(textToAppear, element, 3000)
    }

    private fun checkElementIsDisplayed(cssSelector: String) {
        Assertions.assertTrue(driver.findElement(ByCssSelector.cssSelector(cssSelector))?.isDisplayed!!)
    }

    private fun countElements(cssSelector: String): Int {
        return driver.findElements(ByCssSelector.cssSelector(cssSelector)).size
    }

    @Test
    fun homePageLoadsWithMessages() {
        //Vérification de la présence des messages
        Assertions.assertTrue(driver.currentUrl?.contains("")!!);
        assertElementContainsText("body", "Il n'y a pas de développeurs dans la base de données.");
        assertElementContainsText("body", "Il n'y a pas d'US à attribuer dans la base de données.");
        checkElementIsDisplayed("input[name=firstname]")
    }


    @Test
    fun addDevAndStories() {
        assertElementContainsText(COUNT_DEVELOPERS, "0")

        //Ajout d'un développeur
        fillElement("firstname", "John")
        fillElement("lastname", "DOE")
        btnClick(ADD_DEVELOPER)
        assertElementContainsText("body", "John DOE")
        assertElementContainsText(COUNT_DEVELOPERS, "1")

        //Ajout d'une story
        checkElementIsDisplayed("input[name=name]")
        Assertions.assertEquals(countElements(COUNT_DEVELOPER_STORIES), 0)
        fillElement("name", "Imprimer")
        btnClick(ADD_STORY)
        Assertions.assertEquals(countElements(COUNT_DEVELOPER_STORIES), 1)

        //Ajout d'une story
        fillElement("name", "Se connecter")
        btnClick(ADD_STORY)
        Assertions.assertEquals(countElements(COUNT_DEVELOPER_STORIES), 2)

        //Abandon d'une story
        btnClick(GIVEUP_STORY)
        Assertions.assertEquals(countElements(COUNT_DEVELOPER_STORIES), 1)
        assertElementContainsText("body", "Imprimer")
        assertElementContainsText(COUNT_STORIES, "1")

        //Départ d'un développeur
        btnClick(DELETE_DEVELOPER)
        assertElementContainsText(COUNT_DEVELOPERS, "0")
        assertElementContainsText("body", "Il n'y a pas de développeurs dans la base de données.");
        assertElementContainsText("body", "Imprimer")
        assertElementContainsText("body", "Se connecter")
        assertElementContainsText("body","Pas de devs")
        assertElementContainsText(COUNT_STORIES, "2")

        //Suppression d'une story
        btnClick(DELETE_STORY)
        assertElementContainsText(COUNT_STORIES, "1")

        //Suppression d'une story
        btnClick(DELETE_STORY)
        assertElementContainsText(COUNT_STORIES, "0")
        assertElementContainsText("body", "Il n'y a pas d'US à attribuer dans la base de données.");

    }
}