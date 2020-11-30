package Main;

import Testes.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Main {

    public static void main(String[]args) throws Exception {


        System.out.println("Iniciando testes automatizados da Base 2: ");

        //String opcao = args[2];

        int sum=0;
        int cont = 0;
        JUnitCore jUnitCore = new JUnitCore();
        Result result ;


        System.out.println("Testes automatizados da tela de login sendo executados...");
        result = jUnitCore.run(LoginTests.class);

        System.out.println("Total de testes da tela de login realizados: "+result.getRunCount()+" testes");
        verificaResultadoTestes(result,cont);



        System.out.println("Testes automatizados da tela de esqueci minha senha sendo executados...");
        result = jUnitCore.run(ForgotPasswordTests.class);

        System.out.println("Total de testes da tela de esqueci minha senha realizados: "+result.getRunCount()+" testes");
        verificaResultadoTestes(result,cont);


        System.out.println("Testes automatizados da tela de home sendo executados...");
        result = jUnitCore.run(ReportsTests.class);

        System.out.println("Total de testes da tela de home realizados: "+result.getRunCount()+" testes");
        verificaResultadoTestes(result,cont);


        System.out.println("Testes automatizados da tela de minha conta sendo executados...");
        result = jUnitCore.run(MyAccountTests.class);

        System.out.println("Total de testes da tela de minha conta realizados: "+result.getRunCount()+" testes");
        verificaResultadoTestes(result,cont);



    }


    public static void verificaResultadoTestes(Result result,int cont) throws Exception {

        if(result.wasSuccessful()) System.out.println("Todos os testes passaram com sucesso!");
        else System.out.println("Testes Falhados:");
        for (Failure failure : result.getFailures()) {
            cont++;
            System.out.println("\n"+failure.toString());
        }

        if(!result.wasSuccessful()){
            System.out.println("\n"+cont+" testes falharam");
            throw new Exception("TestsFailed");
        }
    }
}
