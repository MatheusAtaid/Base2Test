package Testes;

import Pages.LoginPage;
import Suporte.Generator;
import Suporte.ScreenShot;
import Suporte.Web;
import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "src/main/resources/massaDeDados.csv")
public class ReportsTests {

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
    public void testaACriarReport(@Param(name = "categoria")String categoria, @Param(name = "frequencia")String frequencia, @Param(name = "gravidade")String gravidade,@Param(name = "prioridade")String prioridade,
                                  @Param(name = "profile")String profile,@Param(name = "sumario")String sumario,
                                  @Param(name = "descricao")String descricao,@Param(name = "passos")String passos,@Param(name = "observacoes")String observacoes){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").direcionaReportPage().criaReport(categoria,frequencia,gravidade,prioridade,profile,sumario,descricao,passos,observacoes).captaMensagem(0);
        assertTrue(message.contains("Operation successful."));
    }

    @Test
    public void testaBAdicionaTagEmReport(){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").selecionaReport("//a[@title='[new] Teste numero 6']").criarTag("testeDoMatheusAtaide").captaMensagem(0);
        assertEquals("testeDoMatheusAtaide",message);
    }

    @Test
    public void testaCRemoveTagReport(){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").selecionaReport("//a[@title='[new] Teste numero 6']").removeTag("testeDoMatheusAtaide").captaMensagem(2);
        assertEquals("No tags attached.",message);
    }

    @Test
    public void testaDAdicionaNotaEmReport(){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").selecionaReport("//a[@title='[new] Teste numero 6']").criarNota("Teste das notas no report do Matheus").captaMensagem(1);
        assertEquals("Teste das notas no report do Matheus",message);
    }

    @Test
    public void testaEEditaNotaEmReport(){
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").selecionaReport("//a[@title='[new] Teste numero 6']").editaNota("Teste das notas no report do Matheus depois de editar nota").captaMensagem(1);
        assertEquals("Teste das notas no report do Matheus depois de editar nota",message);
    }

    @Test
    public void testaFDeletaNotaEmReport() throws InterruptedException {
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").selecionaReport("//a[@title='[new] Teste numero 6']").deletaNota().captaMensagem(3);
        assertTrue(message.contains("There are no notes attached to this issue."));
    }

    @Test
    public void testaGVisualizarIssueHistory() throws InterruptedException {
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").selecionaReport("//a[@title='[new] Teste numero 6']").scrollItem("//*[@id=\"history_open\"]/table/tbody/tr[1]/td").captaMensagem(4);
        System.out.println(message);
        assertEquals(" Issue History",message);
    }

    @Test
    public void testaHMonitorarReport() throws InterruptedException {
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").selecionaReport("//a[@title='[new] Teste numero 6']").scrollItem("/html/body/table[3]/tbody/tr[19]/td/table/tbody/tr/td[4]/form/input[3]").monitorarReport("/html/body/table[3]/tbody/tr[19]/td/table/tbody/tr/td[4]/form/input[3]").direcionaMyView().scrollItem("/html/body/div[3]/table[1]/tbody/tr[3]/td[1]/table/tbody/tr[1]/td/a").captaMensagem(2);
        assertTrue(message.contains("Teste numero 6"));
    }

    @Test
    public void testaIDesmonitorarReport() throws InterruptedException {
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").selecionaReport("//a[@title='[new] Teste numero 6']").scrollItem("/html/body/table[3]/tbody/tr[19]/td/table/tbody/tr/td[4]/form/input[3]").desmonitorarReport("/html/body/table[3]/tbody/tr[19]/td/table/tbody/tr/td[4]/form/input[3]").direcionaMyView().scrollItem("/html/body/div[3]/table[1]/tbody/tr[3]/td[1]/table/tbody/tr[1]/td/a").captaMensagem(3);
        assertEquals("Monitored by Me [ ^ ] (0 - 0 / 0)",message);
    }

    @Test
    public void testaJVisualizarIssuesGerais() throws InterruptedException {
        message = new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").selecionarProjeto("Matheus Ataide´s project ").direcionaIssuesPage().scrollItem("//*[@id=\"buglist\"]/tbody/tr[1]/td/span[1]").captaMensagem();
        assertTrue(message.contains("Viewing Issues (1 - 15 / 15)"));
    }


    @Test
    public void testaKCriarProfile(){
        new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").direcionaMinhaConta().direcionaProfilePage().criarProfile("Plataforma do teste do Matheus Ataide","Windows 10","10.1","Detalhes do teste do matheus no profile!");
    }

    @Test
    public void testaLConsultarProfile(){
        new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").direcionaMinhaConta().direcionaProfilePage().editarProfile("Plataforma do teste do Matheus A Windows 10 10.1","Detalhes do teste do matheus no profile editado!");
    }

    @Test
    public void testaMDeletarProfile(){
        new LoginPage(navegador,wait).fazerLoginCorreto("matheus.ataide","12345678").direcionaMinhaConta().direcionaProfilePage().deletarProfile("Plataforma do teste do Matheus A Windows 10 10.1");
    }

    @After
    public void tearDown() throws InterruptedException {
        String caminhoPrint = "C:/Base2AutomationTests/"+ Generator.dataHora()+test.getMethodName()+".png";
        ScreenShot.print(navegador,caminhoPrint );
        navegador.quit();
    }

}
