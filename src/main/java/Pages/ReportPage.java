package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReportPage extends BasePage{

    public ReportPage(WebDriver navegador, WebDriverWait wait) {
        super(navegador, wait);
    }

    public void inputSelectBox(String tabIndex,String termo){
        WebElement comboBox =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@tabindex='"+tabIndex+"']")));
        new Select(comboBox).selectByVisibleText(termo);
    }

    public void inputSumary(String sumario){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/form/table/tbody/tr[8]/td[2]/input"))).sendKeys(sumario);
    }

    public void inputDescricao(String descricao){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/form/table/tbody/tr[9]/td[2]/textarea"))).sendKeys(descricao);
    }

    public void inputSteps(String passos){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/form/table/tbody/tr[10]/td[2]/textarea"))).sendKeys(passos);
    }

    public void inputObservacoes(String observacao){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/form/table/tbody/tr[11]/td[2]/textarea"))).sendKeys(observacao);
    }

    public void submitReport(){
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/form/table/tbody/tr[15]/td[2]/input"))).click();
    }

    public void selecionaCategoria(String categoria){
        inputSelectBox("1",categoria);
    }

    public void selecionaReproducionalidade(String frequenciaReproducao){
        inputSelectBox("2",frequenciaReproducao);
    }

    public void selecionaGravidade(String gravidade){
        inputSelectBox("3",gravidade);
    }

    public void selecionaPrioridade(String prioridade){
        inputSelectBox("4",prioridade);
    }

    public void selecionaProfile(String perfil){
        inputSelectBox("5",perfil);
    }

    public ReportPage criaReport(String categoria, String frequencia, String gravidade, String prioridade, String profile, String sumario, String descricao, String passos, String observacoes){
        selecionaCategoria(categoria);
        selecionaReproducionalidade(frequencia);
        selecionaGravidade(gravidade);
        selecionaPrioridade(prioridade);
        selecionaProfile(profile);
        inputSumary(sumario);
        inputDescricao(descricao);
        inputSteps(passos);
        inputObservacoes(observacoes);
        inputFoto("C:\\Users\\mathe\\Documents\\Base2Test\\arquivoTeste.txt"); //Arquivo esta junto ao diretorio do codigo fonte do projeto porem talvez possa ser necessario alterar no ambiente de execução
        submitReport();
        return this;
    }

    public void inputFoto(String arquivo){
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"ufile[]\"]")));
        navegador.findElement(By.xpath("//*[@id=\"ufile[]\"]")).sendKeys(arquivo);
    }

    public String captaMensagem(int opcao){
        String resposta = "";
        switch (opcao){
            case 0: resposta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]"))).getText();
                    break;
        }
        return resposta;
    }
}
