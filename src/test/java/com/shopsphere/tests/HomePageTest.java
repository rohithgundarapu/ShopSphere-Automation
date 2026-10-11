package com.shopsphere.tests;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HomePageTest {
    
private void pause() {
    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Test execution was interrupted", e);
    }
}
    private WebDriver driver;

    @BeforeAll
void setUp() {
    String browser = System.getProperty("browser", "chrome");

    if (browser.equalsIgnoreCase("firefox")) {
        driver = new FirefoxDriver();
    } else {
        driver = new ChromeDriver();
    }

    driver.manage().window().maximize();
}

    @Test
    @Order(1)
    void verifyHomePageLoads() {

        driver.get("http://localhost:8080");

        String title = driver.getTitle();

        System.out.println("Browser: "
                + System.getProperty("browser", "chrome"));

        System.out.println("Page title: " + title);

        assertTrue(
                title.contains("ShopSphere"),
                "Homepage title should contain ShopSphere"
        );
        pause();
    }
   @Test
   @Order(2)
void verifyProductsAreDisplayed() throws InterruptedException {
    driver.get("http://localhost:8080");

    WebDriverWait wait = new WebDriverWait(
        driver,
        Duration.ofSeconds(15)
    );

    // Wait for products to appear
    wait.until(
        ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".product-card")
        )
    );

    // Find all product cards
    List<WebElement> products =
        driver.findElements(By.cssSelector(".product-card"));

    // Scroll down to the first product
    ((org.openqa.selenium.JavascriptExecutor) driver)
        .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
            products.get(0));

    // Pause so you can see the scrolling
    pause();

    System.out.println("Products displayed: " + products.size());

    assertTrue(
        products.size() > 0,
        "At least one product should be displayed"
    );
}
@Test
@Order(3)
void verifyProductSearch() throws InterruptedException {

    // 1. Open ShopSphere
    driver.get("http://localhost:8080");

    WebDriverWait wait = new WebDriverWait(
            driver, Duration.ofSeconds(15));

    // 2. Enter the search term
    WebElement searchInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.id("search-input")));
                    pause();

    searchInput.clear();
    searchInput.sendKeys("Everyday Backpack");

    // 3. Click the Search button
    WebElement searchButton = driver.findElement(
            By.cssSelector("button[type='submit']"));

    searchButton.click();

    // 4. Wait for the product results
    wait.until(
            ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".product-card")));

    // 5. Find the search results
    List<WebElement> products =
            driver.findElements(By.cssSelector(".product-card"));

    // 6. Scroll down to the first search result
    if (!products.isEmpty()) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior:'smooth', block:'center'});",
                products.get(0));
    }

    // 7. Pause so you can see the results
    pause();

    // 8. Verify that the expected product appears
    boolean productFound = false;

    for (WebElement product : driver.findElements(
            By.cssSelector(".product-name"))) {

        if (product.isDisplayed()
                && product.getText().trim()
                        .equalsIgnoreCase("Everyday Backpack")) {
            productFound = true;
            break;
        }
    }

    System.out.println("Search term: Everyday Backpack");
    System.out.println("Matching product found: " + productFound);

    assertTrue(
            productFound,
            "Search results should contain Everyday Backpack");
}
   @AfterAll
void closeBrowser() {
    if (driver != null) {
        driver.quit();
    }
}
}