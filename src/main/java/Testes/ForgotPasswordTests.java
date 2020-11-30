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

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ForgotPasswordTests {

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
    public void testaASolicitaRecuperarSenhaCorreto(){
        message = new LoginPage(navegador,wait).redirecionaEsqueciMinhaSenha().solicitaRecuperarSenha("matheus.ataide","matheus.ataide123@gmail.com").captaMensagem(0);
        assertEquals("Password Message Sent",message);
    }

    @Test
    public void testaBSolicitaRecuperaSenhaIncorreto(){
        message = new LoginPage(navegador,wait).redirecionaEsqueciMinhaSenha().solicitaRecuperarSenha("matheus.ataide1231","matheus.ataide123213@gmail.com").captaMensagem(1);
        assertEquals("The provided information does not match any registered account!",message);
    }
    @After
    public void tearDown() throws InterruptedException {
        String caminhoPrint = "C:/Base2AutomationTests/"+ Generator.dataHora()+test.getMethodName()+".png";
        ScreenShot.print(navegador,caminhoPrint );
        navegador.quit();
    }
}
