package edu.spring.dogs

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
    private val COUNT_MASTERS=".count-masters"
    private val COUNT_DOGS=".count-dogs"
    private val COUNT_MASTER_DOGS="td .count-master-dogs"
    private val DELETE_MASTER=".delete-master.button"
    private val DELETE_DOG="button[value='remove'][name='dog-action']:first-child"
    private val ADD_MASTER="form[action='/master/add'] button"
    private val ADD_DOG="button[value='add']"
    private val GIVEUP_DOG="button[value='give-up']"

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

    @Test
    fun homePageLoadsWithMessages() {
        //Vérification de la présence des messages
        Assertions.assertTrue(driver.currentUrl?.contains("")!!);
        assertElementContainsText("body", "Il n'y a pas de maître dans la base de données.");
        assertElementContainsText("body", "Il n'y a pas de chien à l'adoption dans la base de données.");
        checkElementIsDisplayed("input[name=firstname]")
    }


    @Test
    fun addMasterAndDogs() {
        assertElementContainsText(COUNT_MASTERS, "0")

        //Ajout d'un maître
        fillElement("firstname", "John")
        fillElement("lastname", "DOE")
        btnClick(ADD_MASTER)
        assertElementContainsText("body", "John DOE")
        assertElementContainsText(COUNT_MASTERS, "1")

        //Ajout d'un chien
        checkElementIsDisplayed("input[name=name]")
        assertElementContainsText(COUNT_MASTER_DOGS, "0")
        fillElement("name", "Milou")
        btnClick(ADD_DOG)
        assertElementContainsText(COUNT_MASTER_DOGS, "1")

        //Ajout d'un chien
        fillElement("name", "Rex")
        btnClick(ADD_DOG)
        assertElementContainsText(COUNT_MASTER_DOGS, "2")

        //Abandon d'un chien qui n'existe pas
        fillElement("name", "notExistingDog")
        btnClick(GIVEUP_DOG)
        assertElementContainsText(COUNT_MASTER_DOGS, "2")
        assertElementContainsText(COUNT_DOGS, "0")

        //Abandon d'un chien
        fillElement("name", "Rex")
        btnClick(GIVEUP_DOG)
        assertElementContainsText(COUNT_MASTER_DOGS, "1")
        assertElementContainsText("body", "Rex")
        assertElementContainsText(COUNT_DOGS, "1")

        //Disparition du maître
        btnClick(DELETE_MASTER)
        assertElementContainsText(COUNT_MASTERS, "0")
        assertElementContainsText("body", "Il n'y a pas de maître dans la base de données.");
        assertElementContainsText("body", "Rex")
        assertElementContainsText("body", "Milou")
        assertElementContainsText("body","Pas d'adoptant")
        assertElementContainsText(COUNT_DOGS, "2")

        //Départ d'un chien
        btnClick(DELETE_DOG)
        assertElementContainsText(COUNT_DOGS, "1")

        //Départ d'un chien
        btnClick(DELETE_DOG)
        assertElementContainsText(COUNT_DOGS, "0")
        assertElementContainsText("body", "Il n'y a pas de chien à l'adoption dans la base de données.");

    }
}