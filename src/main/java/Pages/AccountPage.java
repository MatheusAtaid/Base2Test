package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountPage extends BasePage{

    public AccountPage(WebDriver navegador, WebDriverWait wait) {
        super(navegador, wait);
    }

    public void inputNome(String nome){
        WebElement campo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[6]/td[2]/input")));
        campo.sendKeys(Keys.CONTROL + "a");
        campo.sendKeys(Keys.DELETE);
        campo.sendKeys(nome);
    }

    public void clicaUpdateUser(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/table/tbody/tr[10]/td[2]/input"))).click();
    }

    public AccountPage editaNomeUser(String nome){
        inputNome(nome);
        clicaUpdateUser();
        return this;
    }

    public void inputSenha(String senha){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[3]/td[2]/input"))).sendKeys(senha);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/form/table/tbody/tr[4]/td[2]/input"))).sendKeys(senha);
    }

    public AccountPage redefinirSenha(String senha){
        inputSenha(senha);
        clicaUpdateUser();
        return this;
    }

    public ProfilesPage direcionaProfilePage(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/table/tbody/tr[1]/td[2]/span[4]/a"))).click();
        return new ProfilesPage(navegador,wait);
    }
    public String captaMensagem(int opcao){
        String resposta ="";
        switch (opcao){
            case 0: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]"))).getText();
                break;
        }
        return resposta;
    }

}
