package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DetailReportPage extends BasePage{

    public DetailReportPage(WebDriver navegador, WebDriverWait wait) {
        super(navegador, wait);
    }

    public void inputTags(String tag){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tag_string"))).sendKeys(tag);
    }

    public void clicaSubmit(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/table[3]/tbody/tr[16]/td[2]/form/input[5]"))).click();
    }

    public DetailReportPage criarTag(String tag){
        inputTags(tag);
        clicaSubmit();
        return this;
    }

    public DetailReportPage removeTag(String tag){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='"+tag+"']//following-sibling::a"))).click();
        return this;
    }

    public String captaMensagem(int opcao){
        String resposta = "";
        switch (opcao){
            case 0: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/table[3]/tbody/tr[15]/td[2]/a[1]"))).getText();
                    break;
            case 1: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("bugnote-note-public"))).getText();
                    break;
            case 2: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/table[3]/tbody/tr[15]/td[2]"))).getText();
                    break;
            case 3: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"bugnotes_open\"]/table/tbody/tr[2]/td"))).getText();
                    break;
            case 4: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"history_open\"]/table/tbody/tr[1]/td"))).getText();
                    break;
            case 5: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/table[3]/tbody/tr[19]/td/table/tbody/tr/td[4]/form/input[3]"))).getText();
                    break;
        }
        return resposta;

    }
    public void inputNota(String nota){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"bugnote_add_open\"]/form/table/tbody/tr[2]/td[2]/textarea"))).sendKeys(nota);
    }
    public DetailReportPage criarNota(String nota){
        inputNota(nota);
        submitCriarNota();
        return this;
    }

    public DetailReportPage editaNota(String nota){
        clicaEditarNota();
        inputEditarNota(nota);
        clicaSubmitEditarNota();
        return this;
    }

    public DetailReportPage deletaNota() throws InterruptedException {
        clicaDeletarNota();
        return this;
    }


    public void clicaDeletarNota() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[1]/div/form[2]/input[2]"))).click();
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/input[4]"))).click();
    }



    public void clicaSubmitEditarNota(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/table/tbody/tr[3]/td/input"))).click();
    }

    public void clicaEditarNota(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[1]/div/form[1]/input[2]"))).click();
    }

    public void inputEditarNota(String nota){
        WebElement campo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[2]/td/textarea")));
        campo.sendKeys(Keys.CONTROL + "a");
        campo.sendKeys(Keys.DELETE);
        campo.sendKeys(nota);
    }

    public void submitCriarNota(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"bugnote_add_open\"]/form/table/tbody/tr[3]/td/input"))).click();
    }


    public DetailReportPage scrollItem(String xpath) throws InterruptedException {
        Thread.sleep(3000);
        WebElement card = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        JavascriptExecutor jse = (JavascriptExecutor)navegador;
        jse.executeScript("arguments[0].scrollIntoView(true);",navegador.findElement(By.xpath(xpath)));
        return this;
    }

    public void clicaMonitorarReport(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/table[3]/tbody/tr[19]/td/table/tbody/tr/td[4]/form/input[3]"))).click();
    }

    public HomePage direcionaMyView(){
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("My View"))).click();
        return new HomePage(navegador,wait);
    }

    public DetailReportPage monitorarReport(String xpath) throws InterruptedException {
        scrollItem(xpath);
        clicaMonitorarReport();
        return this;
    }

    public DetailReportPage desmonitorarReport(String xpath) throws InterruptedException {
        scrollItem(xpath);
        clicaMonitorarReport();
        return this;
    }

}

