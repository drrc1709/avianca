import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AviancaTest {
	private WebDriver driver;
	JavascriptExecutor js;

	public static final String XPATH = "xpath";
	public static final String CSS = "css";
	public static final String NOMBRE = "nombre";
	public static final String ID = "id";

	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void avianca() throws InterruptedException {

		String destino = "//div[2]/div/form/div/div[2]/div/div/div/fieldset/div/div[3]/div[2]/label/div/input";
		String ida = "//input[contains(@class,'form-control solo-ida')]";
		String pasajeros = "(//input[@name='pbPasajeros'])[3]";

		try {
			WebDriverWait wait = new WebDriverWait (driver, 30);
			BufferedReader br = new BufferedReader(new FileReader("src\\test\\resources\\data.csv"));
			String line = "";
			String cvsSplitBy = ";";
			int count = 0;
			
			
			while ((line = br.readLine()) != null) {
				
				String[] datos = line.split(cvsSplitBy);
				
				if (count > 0) {
					driver.get("https://www.avianca.com/co/es/");
					driver.manage().window().maximize();
					driver.findElement(By.linkText("Solo ida")).click();
					Thread.sleep(3000);
					driver.findElement(By.xpath(destino)).click();
					System.out.println(datos[0]+"\n"+datos[1]+"\n"+datos[2]);
					driver.findElement(By.xpath(destino)).sendKeys(datos[0]);
					driver.findElement(By.xpath(destino)).sendKeys(Keys.DOWN);
					driver.findElement(By.xpath(destino)).sendKeys(Keys.ENTER);
					driver.findElement(By.xpath(ida)).click();
					driver.findElement(By.xpath(ida)).sendKeys(datos[1]);
					driver.findElement(By.xpath(pasajeros)).click();
					driver.findElement(By.xpath(pasajeros)).sendKeys(datos[2]);
					driver.findElement(By.xpath(pasajeros)).sendKeys(Keys.ENTER);
					Thread.sleep(2000);
					Actions actions = new Actions(driver);
					WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@title='Buscar vuelos'])[4]")));
					actions.moveToElement(element).perform();
					Thread.sleep(1000);
					element.click();
					js.executeScript("window.alert('Detalles del vuelo:\\n\\tDestino: "+datos[0]+"\\n\\tFecha de Viaje: "+datos[1]+"\\n\\tPasajeros: "+datos[2]+"')");
					Thread.sleep(2000);
					driver.switchTo().alert().accept();
				}
				count++;
				driver.navigate().refresh();
			}
			
		} catch (IOException e) {
			Logger.getLogger(AviancaTest.class.getName()).warning(e.toString());
		}
	}
}
