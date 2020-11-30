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
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTests {

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
    public void testaAFazerLoginCorreto(){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").captaMensagem(0);
        assertEquals("Logged in as: matheus.ataide (Matheus Ataíde - reporter)",message);
    }

    @Test
    public void testaBFazerLoginIncorreto(){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","32134234234").captaMensagem(1);
        assertEquals("Your account may be disabled or blocked or the username/password you entered is incorrect.",message);
    }

    @Test
    public void testaCFazerLogout(){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").fazerLogout().captaMensagem();
        assertTrue(message.contains("Login"));
    }


    @After
    public void tearDown() throws InterruptedException {
        String caminhoPrint = "C:/Base2AutomationTests/"+ Generator.dataHora()+test.getMethodName()+".png";
        ScreenShot.print(navegador,caminhoPrint );
        navegador.quit();
    }
}
