import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class LgSoundbarScraper {
    public static void main(String[] args) {
        // Set up Chrome driver
       // System.setProperty("webdriver.chrome.driver", "C:\Users\User\Downloads");
       //System.setProperty("webdriver.chrome.driver", "C:/Users/User/Downloads/chromedriver.exe");
       System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Downloads\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            // Open Amazon.in
            driver.get("https://www.amazon.in");

            // Search for LG soundbar
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.sendKeys("lg soundbar");
            searchBox.submit();

            // Wait for search results to load
           // WebDriverWait wait = new WebDriverWait(driver, 10);
           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search")));

            // Get all product elements
            List<WebElement> products = driver.findElements(By.xpath("//div[@data-component-type='s-search-result']"));

            // Map to store product names and prices
            Map<String, Integer> productDict = new HashMap<>();

            for (WebElement product : products) {
                try {
                    // Extract product name
                    WebElement nameElement = product.findElement(By.xpath(".//h2//span"));
                    String name = nameElement.getText();

                    // Extract price
                    String priceText = "";
                    try {
                        priceText = product.findElement(By.xpath(".//span[@class='a-price-whole']")).getText();
                    } catch (Exception e) {
                        // Price element not found
                    }

                    // Handle price extraction
                    int price = 0;
                    if (!priceText.isEmpty()) {
                        price = Integer.parseInt(priceText.replace(",", ""));
                    }

                    // Add product name and price to map
                    productDict.put(name, price);
                } catch (Exception e) {
                    // Handle any exceptions that occur while processing each product
                    System.out.println("Error processing a product: " + e.getMessage());
                }
            }

            // Sort products by price
            productDict.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()))
                .forEach(e -> System.out.println(e.getValue() + " " + e.getKey()));

        } finally {
            // Close the browser
            driver.quit();
        }
    }
}

