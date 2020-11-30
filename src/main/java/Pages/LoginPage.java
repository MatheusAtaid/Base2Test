package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage{

    public LoginPage(WebDriver navegador, WebDriverWait wait) {
        super(navegador, wait);
    }

    public void inputUsername(String username){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/form/table/tbody/tr[2]/td[2]/input"))).sendKeys(username);
    }

    public void inputSenha(String senha){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/form/table/tbody/tr[3]/td[2]/input"))).sendKeys(senha);
    }

    public void clicaSubmitLogin(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/form/table/tbody/tr[6]/td/input"))).click();
    }

    public void clicaInputRememberUser(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/form/table/tbody/tr[4]/td[2]/input"))).click();
    }

    public HomePage fazerLoginCorreto(String username, String senha){
        inputUsername(username);
        inputSenha(senha);
        clicaInputRememberUser();
        clicaSubmitLogin();
        return new HomePage(navegador,wait);
    }

    public ForgotPasswordPage redirecionaEsqueciMinhaSenha(){
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Lost your password?"))).click();
        return new ForgotPasswordPage(navegador,wait);
    }

    public String captaMensagem(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/form/table/tbody/tr[1]/td[1]"))).getText();
    }
}
