package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfilesPage extends BasePage{

    public ProfilesPage(WebDriver navegador, WebDriverWait wait) {
        super(navegador, wait);
    }

    public void inputPlatform(String plataforma){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[2]/td[2]/input"))).sendKeys(plataforma);
    }

    public void inputSO(String so){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[3]/td[2]/input"))).sendKeys(so);
    }

    public void inputOSVersion(String version){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[4]/td[2]/input"))).sendKeys(version);
    }

    public void inputDetalhesAdicionais(String detalhes){
        WebElement texto = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[5]/td[2]/textarea")));
        texto.sendKeys(Keys.CONTROL + "a");
        texto.sendKeys(Keys.DELETE);
        texto.sendKeys(detalhes);
    }

    public void clicaSubmitSalvarProfile(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td[2]/input"))).click();
    }

    public ProfilesPage criarProfile(String plataforma,String so, String version,String detalhes){
        inputPlatform(plataforma);
        inputSO(so);
        inputOSVersion(version);
        inputDetalhesAdicionais(detalhes);
        clicaSubmitSalvarProfile();
        return this;
    }

    public void inputSelectProfile(String profile){
        WebElement comboBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/form/table/tbody/tr[3]/td[2]/select")));
        new Select(comboBox).selectByVisibleText(profile);
    }

    public void clicaSubmitVisualizarProfile(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/form/table/tbody/tr[4]/td/input"))).click();
    }

    public ProfilesPage editarProfile(String profile,String detalhes){
        inputSelectProfile(profile);
        clicaSubmitVisualizarProfile();
        inputDetalhesAdicionais(detalhes);
        clicaSubmitUpdateProfile();
        return this;
    }

    public ProfilesPage deletarProfile(String profile){
        inputSelectProfile(profile);
        inputRadioButtonDelete();
        clicaSubmitVisualizarProfile();
        return this;
    }

    public void inputRadioButtonDelete(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/form/table/tbody/tr[2]/td/input[3]"))).click();
    }


    public void clicaSubmitUpdateProfile(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td/input"))).click();
    }

}
