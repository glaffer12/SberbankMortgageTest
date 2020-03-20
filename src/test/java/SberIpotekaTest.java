import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.SourceType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.Thread;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class SberIpotekaTest
{
    WebDriver driver;
    Actions actions;
    JavascriptExecutor jse;

    @Before
    public void BeforeTest() {
        System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
        driver = new ChromeDriver();
        actions = new Actions(driver);
        jse = (JavascriptExecutor)driver;
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

    }

    @Test
    public void BodyTest() throws InterruptedException {

        driver.get("https://www.sberbank.ru");

        WebElement Mortgage = driver.findElement(By.xpath("//button[contains(@aria-label,\"Ипотека\")]"));
        actions.moveToElement(Mortgage).build().perform();
        WebElement MortgageCompleteHouse = driver.findElement(By.xpath("//a[@href=\"/ru/person/credits/home/buying_complete_house\" and contains(text(),\"Ипотека на готовое жильё\") and @class = \"lg-menu__sub-link\"]"));
        actions.moveToElement(MortgageCompleteHouse).click().build().perform();

        switchToFrame(By.xpath("//iframe[@id=\"iFrameResizer0\"]"));

        WebElement sum = driver.findElement(By.xpath("//*[@id=\"estateCost\"]"));
        WebElement firstSum = driver.findElement(By.xpath("//input[@id=\"initialFee\"]"));
        WebElement creditTerm = driver.findElement(By.xpath("//input[@id=\"creditTerm\"]"));

        WebElement amountOfCredit = driver.findElement(By.xpath("//span[@data-test-id=\"amountOfCredit\"]"));
        WebElement monthlyPayment = driver.findElement(By.xpath("//span[@data-test-id=\"monthlyPayment\"]"));
        WebElement requiredIncome = driver.findElement(By.xpath("//span[@data-test-id=\"requiredIncome\"]"));
        WebElement rate = driver.findElement(By.xpath("//span[@data-test-id=\"rate\"]"));

        fillField(sum,"5180000");
        fillField(firstSum,"3058000");
        fillField(creditTerm,"30");

        scrollToElement(creditTerm);

        WebElement SalaryCardExistence = driver.findElement(By.xpath("//label[./input[@data-test-id=\"paidToCard\"]]"));
        SalaryCardExistence.click();
        waitWhileValueChanges(amountOfCredit);

        WebElement CanConfirm = driver.findElement(By.xpath("//label[./input[@data-test-id=\"canConfirmIncome\"]]"));
        CanConfirm.click();
        waitWhileValueChanges(amountOfCredit);

        WebElement youngFamily = driver.findElement(By.xpath("//label[./input[@data-test-id=\"youngFamilyDiscount\"]]"));
        youngFamily.click();
        waitWhileValueChanges(amountOfCredit);

        WebElement Pointer = driver.findElement(By.xpath("//div[text()=\"Цель кредита\"]"));

        scrollToElement(Pointer);

        Assert.assertEquals("2 122 000 ₽", amountOfCredit.getText());
        Assert.assertEquals("17 998 ₽", monthlyPayment.getText());
        Assert.assertEquals("29 997 ₽", requiredIncome.getText());
        Assert.assertEquals("9,6 %", rate.getText());

        scrollToElement(SalaryCardExistence);

        CanConfirm.click();
        waitWhileValueChanges(amountOfCredit);

        scrollToElement(Pointer);

        Assert.assertEquals("2 122 000 ₽", amountOfCredit.getText());
        Assert.assertEquals("17 535 ₽", monthlyPayment.getText());
        Assert.assertEquals("29 224 ₽", requiredIncome.getText());
        Assert.assertEquals("9,4 %", rate.getText());

    }

    @After
    public void AfterTest() {
        driver.quit();
    }

    public void switchToFrame(By by) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
    }

    public void scrollToElement(WebElement element) {
        jse.executeScript("arguments[0].scrollIntoView();", element);
    }


    public void fillField(WebElement element, String value) throws InterruptedException {
        element.clear();
        Thread.sleep(500);
        element.sendKeys(value);
        waitWhileValueInput(element, value);
        Thread.sleep(500);
    }

    public void waitWhileValueChanges(WebElement element) throws InterruptedException {
        String CurrentValue;
        while (true) {
            CurrentValue=element.getText();
            Thread.sleep(500);
            if (CurrentValue.equals(element.getText())) break;
        }
    }

    public void waitWhileValueInput(WebElement element, String value) throws InterruptedException {
        while(true) {
            if (element.getAttribute("value").replaceAll("[^0-9]","").equals(value)) {
                break;
            } else {
                Thread.sleep(500);
            }
        }
    }

}
