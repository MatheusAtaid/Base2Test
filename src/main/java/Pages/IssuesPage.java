package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IssuesPage extends BasePage{

    public IssuesPage(WebDriver navegador, WebDriverWait wait) {
        super(navegador, wait);
    }

    public String captaMensagem(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"buglist\"]/tbody/tr[1]/td/span[1]"))).getText();
    }


    public IssuesPage scrollItem(String xpath) throws InterruptedException {
        Thread.sleep(3000);
        WebElement card = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        JavascriptExecutor jse = (JavascriptExecutor)navegador;
        jse.executeScript("arguments[0].scrollIntoView(true);",navegador.findElement(By.xpath(xpath)));
        return this;
    }
}
