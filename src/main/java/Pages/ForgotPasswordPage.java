package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ForgotPasswordPage extends BasePage{

    public ForgotPasswordPage(WebDriver navegador, WebDriverWait wait) {
        super(navegador, wait);
    }

    public void inputUsername(String username){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[2]/td[2]/input"))).sendKeys(username);
    }

    public void inputEmail(String email){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[3]/td[2]/input"))).sendKeys(email);
    }

    public void clicaSubmit(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/table/tbody/tr[5]/td/input"))).click();
    }

    public ForgotPasswordPage solicitaRecuperarSenha(String username, String email){
        inputUsername(username);
        inputEmail(email);
        clicaSubmit();
        return this;
    }

    public String captaMensagem(int opcao){
        String resposta = "";
        switch (opcao){
            case 0: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/table/tbody/tr[1]/td/b"))).getText();
                    break;
            case 1: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/table/tbody/tr[2]/td/p"))).getText();
                    break;
        }
        return resposta;
    }

}
