package Suporte;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;


public class ScreenShot {
	public static void print(WebDriver navegador, String arquivo) {
		File screenShot = ((TakesScreenshot)navegador).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenShot, new File(arquivo));
		}
		catch(Exception e) {
			System.out.println("Houve algum erro ao colocar tal arquivo na pasta");
			System.out.println("Mensagem de erro: "+e.getMessage());
		}
		finally {
			//Ignorar
		}
	}
}
