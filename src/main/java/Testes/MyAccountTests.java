package Testes;

import Pages.LoginPage;
import Suporte.Generator;
import Suporte.ScreenShot;
import Suporte.Web;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyAccountTests {

    WebDriver navegador ;
    WebDriverWait wait;
    String message;

    @Rule
    public TestName test = new TestName();

    @Before
    public void setUp() throws MalformedURLException {
        navegador = Web.geraDriver();
        wait = new WebDriverWait(navegador,20);
    }

    @Test
    public void testaAAtualizarNomePerfil(){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").direcionaMinhaConta().editaNomeUser("Matheus Ataíde editado").captaMensagem(0);
        assertEquals("Real name successfully updated\n" +
                "Operation successful.\n" +
                "[ Proceed ]",message);
        new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").direcionaMinhaConta().editaNomeUser("Matheus Ataíde"); //Volta nome de usuario ao normal apos edição
    }

    @Test
    public void testaBRedefinirSenha(){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").direcionaMinhaConta().redefinirSenha("123456789").captaMensagem(0);
        assertEquals("Password successfully updated\n" +
                "Operation successful.\n" +
                "[ Proceed ]",message);
        new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","123456789").direcionaMinhaConta().redefinirSenha("12345678");
    }

    @After
    public void tearDown() throws InterruptedException {
        String caminhoPrint = "C:/Base2AutomationTests/"+ Generator.dataHora()+test.getMethodName()+".png";
        ScreenShot.print(navegador,caminhoPrint );
        navegador.quit();
    }
}
