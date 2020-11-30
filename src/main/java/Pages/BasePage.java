package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected WebDriver navegador;
    protected WebDriverWait wait;

    public BasePage(WebDriver navegador,WebDriverWait wait){
        this.navegador = navegador;
        this.wait = wait;
    }
}