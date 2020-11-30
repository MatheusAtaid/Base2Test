package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage extends BasePage{

    public HomePage(WebDriver navegador, WebDriverWait wait) {
        super(navegador, wait);
    }

    public String captaMensagem(int opcao){
        String resposta = "";
        switch (opcao){
            case 0: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/table[1]/tbody/tr/td[1]"))).getText();
                    break;
            case 1: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/font"))).getText();
                    break;
            case 2: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/table[1]/tbody/tr[3]/td[1]/table/tbody/tr[2]/td[2]/span"))).getText();
                    break;
            case 3: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/table[1]/tbody/tr[3]/td[1]/table/tbody/tr/td"))).getText();
                    break;
        }
        return resposta;
    }

    public LoginPage fazerLogout(){
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout"))).click();
        return new LoginPage(navegador, wait);
    }

    public ReportPage direcionaReportPage(){
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Report Issue"))).click();
        return new ReportPage(navegador,wait);
    }

    public HomePage selecionarProjeto(String projeto){
        WebElement comboBox =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/table[1]/tbody/tr/td[3]/form/select")));
        new Select(comboBox).selectByVisibleText(projeto);
        return this;
    }

    public AccountPage direcionaMinhaConta(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("My Account"))).click();
        return new AccountPage(navegador,wait);
    }

    public IssuesPage direcionaIssuesPage(){
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("View Issues"))).click();
        return new IssuesPage(navegador,wait);
    }

    public DetailReportPage selecionaReport(String xpathReport){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathReport))).click();
        return new DetailReportPage(navegador, wait);
    }

    public HomePage scrollItem(String xpath) throws InterruptedException {
        Thread.sleep(3000);
        WebElement card = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        JavascriptExecutor jse = (JavascriptExecutor)navegador;
        jse.executeScript("arguments[0].scrollIntoView(true);",navegador.findElement(By.xpath(xpath)));
        return this;
    }



}
