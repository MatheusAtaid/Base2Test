package Suporte;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Web {

	public static final String USERNAME = "matheusataide1";
	public static final String AUTOMATE_KEY = "sZZQFPEkK52vfsXopfqQ";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

	public static WebDriver geraDriver() {
		WebDriver driver;
		File file = new File("C:/WebDriver/chromedriver.exe"); //Colocar diretorio de onde esta localizado o chromedriver na maquina
		System.setProperty("webdriver.chrome.driver",file.getAbsolutePath());
		ChromeOptions options = new ChromeOptions();
		//
		options.addArguments("--headless"); //Caso queira visualizar o teste em interface, desabilitar essa opcao
		//options.addArguments("window-size=1980,1080");
		options.addArguments("--allow-insecure-localhost");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("disable-infobars");
		options.addArguments("--allow-running-insecure-content");
		options.addArguments("--disable-gpu");
		options.addArguments("--user-agent");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		driver = new ChromeDriver(options);
		driver.manage().window().setSize(new Dimension(1920,1080));
		driver.get("http://mantis-prova.base2.com.br");
		return driver;
	}

	public static WebDriver creteBrowserStack() throws MalformedURLException { //Caso queiramos executar os testes em nuvem ao inves de em nossa maquina
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browser", "Chrome");
		caps.setCapability("browser_version", "79.0");
		caps.setCapability("os", "Windows");
		caps.setCapability("os_version", "10");
		caps.setCapability("resolution", "1280x1024");

		WebDriver driver = null;
		try{
			driver =  new RemoteWebDriver(new URL(URL), caps);
			driver.get("http://mantis-prova.base2.com.br");
		}
		catch (MalformedURLException e){
			System.out.println("Url com problema :"+e.getMessage());
		}

		return driver;
	}
}
