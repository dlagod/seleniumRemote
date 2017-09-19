package es.ofca.test.jimena4.common.selenium;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.common.base.Function;

import es.ofca.test.jimena4.common.constant.Constants;
import es.ofca.test.jimena4.common.constant.Constants.Browser;
import es.ofca.test.jimena4.common.utils.ConfigFile;
import es.ofca.test.jimena4.common.utils.PropertiesFile;

public class WebDriverSelenium {
	
	private static final Logger LOGGER = Logger.getLogger(WebDriverSelenium.class.getName());

	protected WebDriver driver;
	protected StringBuffer verificationErrors = new StringBuffer();
	
	protected static final String MESSAGE_lOGIN = "login.messageLogin";
	protected static final String BTN_CLOSE = "login.btnClose";
	
	
	/**
	 * Método que devuelve el Driver
	 * @return Devuelve el driver
	 */
    public WebDriver getDriver() {
        return driver;
    }
 
    /**
     * Método que establece el driver
     * @param driver Driver
     */
    public void setWebDriver(RemoteWebDriver driver) {
        this.driver = driver;
    }

	@BeforeMethod(alwaysRun = true)
	public void setUp() throws Exception {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.setUp");
		}
		
		// Inicializar el entorno
		ConfigFile.getInstance();

		try {
			// Se fuerza el perfil de Firefox para indicar la
			// Opción 0 - Sin proxy
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("network.proxy.type", 0);

			DesiredCapabilities cap = DesiredCapabilities.firefox();
			cap.setCapability(FirefoxDriver.PROFILE, profile);
			cap.setJavascriptEnabled(true);

			// Acceso Remoto
			driver = new RemoteWebDriver(new URL(ConfigFile.getValue(Constants.REMOTE_WEB_HOST)), cap);
		} catch (Exception e) {
			LOGGER.error("Error al instanciar el driver", e);
			throw e;
		}
		
		// Maximizar la ventana
		driver.manage().window().maximize();

		// Tiempo de timeo
		//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		// Se instancia el fichero de recursos.
		PropertiesFile.getInstance();
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown() throws Exception {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.tearDown");
		}

		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}
	
	
	/**
	 * Método que cambia el focus
	 * @throws Exception
	 *             Excepción producida
	 */
	public void focus() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.focus");
		}
		
		// Se cambia el foco del campo
		getDriver().findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "\t");

		// Espera de recarga de página
		getDriver().manage().timeouts().pageLoadTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS);
	}
	
	
	
	/**
	 * Función que espera hasta encontrar el elemento
	 * @param locator Identificador del elemento
	 * @return WebElement
	 * @throws Exception Excepción producida
	 */
	public WebElement untilFindElement(final By locator)
			throws Exception {
		
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	            .withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class);
	    
		return wait.until(new Function<WebDriver, WebElement>() {
		    public WebElement apply(WebDriver webDriver) {
				
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilFindElement");
				}
		        return getDriver().findElement(locator);
		    }
		});
	}
	
	
	/**
	 * Función que espera hasta encontrar los elementos
	 * @param locator Identificador de los elementos
	 * @return Lista de elementos
	 * @throws Exception Excepción producida
	 */
	public List<WebElement> untilFindElements(final By locator)
			throws Exception {
		
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	            .withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class);
	    
		return wait.until(new Function<WebDriver, List<WebElement>>() {
		    public List<WebElement> apply(WebDriver webDriver) {
				
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilFindElements");
				}
				List<WebElement> elements = getDriver().findElements(locator);
				if ((elements == null) || (elements.isEmpty())) {
					throw new NoSuchElementException("Elementos no encontrados");
		    	} 

		        return elements;
		    }
		});
	}
	
	/**
	 * Función que espera hasta limpiar el elemento
	 * @param locator Identificador del elemento
	 * @return Booleano que indica si se ha realizado la acción o no
	 * @throws Exception Excepción producida
	 */
	public boolean untilClearElement(final By locator)
			throws Exception {
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	            .withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class);
	    
		return wait.until(new Function<WebDriver, Boolean>() {
		    public Boolean apply(WebDriver webDriver) {
		    	
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilClearElement");
				}
				
		    	Boolean make = false;
		    	WebElement webElement = ((WebElement) getDriver().findElement(locator));
		    	if (webElement.isDisplayed()) {
		    		webElement.clear();
		    		make = true;
		    	}
		 
		    	return make;
		    }
		});
	}
	
	
	/**
	 * Función que espera hasta establecer el valor del elemento
	 * @param locator Identificador del elemento
	 * @param value Valor del elemento
	 * @return Booleano que indica si se ha realizado la acción o no
	 * @throws Exception Excepción producida
	 */
	public boolean untilSendKeysElement(final By locator, final String value)
			throws Exception {
		
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	            .withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class);
	    
	    return wait.until(new Function<WebDriver, Boolean>() {
		    public Boolean apply(WebDriver webDriver) {
		    	
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilSendKeysElement");
				}
		
		    	Boolean make = false;
		    	WebElement element = ((WebElement) getDriver().findElement(locator));
		    	element.sendKeys(value);
		    	if ((value != null) && (value.equalsIgnoreCase(element.getAttribute(Constants.VALUE)))) {
		    		make = true;
		    	} else {
		    		throw new NoSuchElementException("Valor no establecido");
		    	}
		    	
		    	return make;
		    }
		});
	}	
	
	/**
	 * Función que espera devolver el valor del atributo
	 * @param locator Identificador del elemento
	 * @param name Nombre del atributo
	 * @return Valor devuelto.
	 * @throws Exception Excepción producida
	 */
	public String untilGetAttributeElement(final By locator, final String name)
			throws Exception {
		
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	            .withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class);
	    
	    return wait.until(new Function<WebDriver, String>() {
		    public String apply(WebDriver webDriver) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilGetAttributeElement");
				}
		    	return ((WebElement) getDriver().findElement(locator)).getAttribute(name);
		    }
		});
	}
	
	/**
	 * Función que espera devolver el valor del atributo
	 * @param locator Identificador del elemento
	 * @param name Nombre del atributo
	 * @param value Valor del atributo 
	 * @return Valor devuelto.
	 * @throws Exception Excepción producida
	 */
	public String untilGetAttributeElement(final By locator, final String name, final String value)
			throws Exception {
		
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	            .withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class);
	    
	    return wait.until(new Function<WebDriver, String>() {
		    public String apply(WebDriver webDriver) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilGetAttributeElement");
				}
				WebElement element = ((WebElement) getDriver().findElement(locator));
				if ((value != null) && (!value.equalsIgnoreCase(element.getAttribute(name)))) {
					throw new NoSuchElementException("Valor no establecido");
		    	}
		    	return element.getAttribute(name);
		    }
		});
	}
	
	
	/**
	 * Función que espera devolver el valor del texto
	 * @param locator Identificador del elemento
	 * @return Valor devuelto.
	 * @throws Exception Excepción producida
	 */
	public String untilGetTextElement(final By locator)
			throws Exception {
		
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
				.withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS).pollingEvery(500, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);

		return wait.until(new Function<WebDriver, String>() {
			public String apply(WebDriver webDriver) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilGetTextElement");
				}
				return ((WebElement) getDriver().findElement(locator)).getText();
			}
		});
	}
	
	
	/**
	 * Función que espera devolver el valor del texto
	 * @param locator Identificador del elemento
	 * @param value Valor del atributo 
	 * @return Valor devuelto.
	 * @throws Exception Excepción producida
	 */
	public String untilGetTextElement(final By locator, final String value)
			throws Exception {
		
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
				.withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS).pollingEvery(500, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);

		return wait.until(new Function<WebDriver, String>() {
			public String apply(WebDriver webDriver) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilGetTextElement");
				}
				WebElement element = ((WebElement) getDriver().findElement(locator));
				if ((value != null) && (!value.equalsIgnoreCase(element.getText()))) {
					throw new NoSuchElementException("Valor no establecido");
		    	}
				return element.getText();
			}
		});
	}
	
	
	
	/**
	 * Función que espera hasta pulsar click en el elemento
	 * @param locator Identificador del elemento
	 * @return Booleano que indica si se ha realizado la acción o no
	 * @throws Exception Excepción producida
	 */
	public boolean untilClickElement(final By locator)
			throws Exception {
		
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	            .withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class)
	            .ignoring(ElementNotVisibleException.class);
	    
		return wait.until(new Function<WebDriver, Boolean>() {
		    public Boolean apply(WebDriver webDriver) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilClickElement");
				}
		    	Boolean make = false;
		    	((WebElement) getDriver().findElement(locator)).click();
		    	make = true;
		    	return make;
		    }
		});
	}
	
	
	/**
	 * Función que espera hasta pulsar seleccionar un el elemento de una lista
	 * @param locator Identificador del elemento
	 * @param value Valor del elemento en la lista
	 * @return Booleano que indica si se ha realizado la acción o no
	 * @throws Exception Excepción producida
	 */
	public boolean untilSelectElement(final By locator, final String value)
			throws Exception {
		
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	            .withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class)
	            .ignoring(ElementNotVisibleException.class);
	    
		return wait.until(new Function<WebDriver, Boolean>() {
		    public Boolean apply(WebDriver webDriver) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilSelectElement");
				}
			
		    	Boolean make = false;
				// Se comprueba los mensajes de campos obligatorios
				List<WebElement> listValuesSelect = getDriver().findElements(locator);
				
				if ((listValuesSelect != null) && (!listValuesSelect.isEmpty())) {
					for (WebElement select : listValuesSelect) {
						if(select.getText().trim().equals(value)){
							select.click();
							make = true;
				            break;
				        }
					}
				}
		    	
		    	return make;
		    }
		});
	}
	
	/**
	 * Función que espera hasta pulsar click en el elemento
	 * @param locator Identificador del elemento
	 * @return Booleano que indica si se ha realizado la acción o no
	 * @throws Exception Excepción producida
	 */
	public boolean untilSelectTextElement(final By locator, final String text)
			throws Exception {
		
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
	            .withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
	            .pollingEvery(500, TimeUnit.MILLISECONDS)
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class)
	            .ignoring(ElementNotVisibleException.class);
	    
		return wait.until(new Function<WebDriver, Boolean>() {
		    public Boolean apply(WebDriver webDriver) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("WebDriverSelenium.untilClickElement");
				}
		    	Boolean make = false;
		    	Select select = new Select(getDriver().findElement(locator));
		    	select.selectByVisibleText(text);
		    	make = true;
		    	return make;
		    }
		});
	}
	
	
	/**
	 * Función que espera hasta pulsar click en un Check
	 * @param locatorClick Identificador del elemento para realizar el click
	 * @param locatorCheck Identificador del elemento para verificar el check
	 * @return Booleano que indica si se ha realizado la acción o no
	 * @throws Exception Excepción producida
	 */
	public boolean untilClickCheck(final By locatorClick, final By locatorCheck)
			throws Exception {
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.untilClickCheck");
		}
		
		// Se comprueba el elemento
		Boolean oldChecked = Boolean.parseBoolean(((WebElement) untilFindElement(locatorCheck)).getAttribute(Constants.ARIA_CHECKED));
   	
    	// Se realiza el click
		clickWhenReady(locatorClick);
    	
    	//Se comprueba cada 500 millis
    	WebDriverWait wait = new WebDriverWait(getDriver(), Constants.TIME_OUT_SECONDS, 500);
    	Boolean updated = wait.until(textToBePresentInElementAttribute(locatorCheck, String.valueOf(!oldChecked), Constants.ARIA_CHECKED));
    	
    	//Se vuelve comprobar que se ha modificado el elemento
    	Boolean checked = oldChecked;
    	if (updated) {
    		checked = Boolean.parseBoolean(((WebElement) untilFindElement(locatorCheck)).getAttribute(Constants.ARIA_CHECKED));
    	}
    	
    	//Se espera al cambio del check
    	return checked;
	}

	
	
	
	/**
	 * Método que indica cuando un elemento se puede pinchar
	 * @param by Localizador del elemento
	 * @return True, si se puede pinchar en él.
	 */
	public boolean retryingFindClick(By by) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.retryingFindClick");
		}
		
		boolean result = false;
		int attempts = 0;
		while (attempts < Constants.RETRY) {
			try {
				getDriver().findElement(by).click();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				LOGGER.error("retryingFindClick: El elemento no se encuentra en la caché: " + by.toString());
			}
			attempts++;
		}
		return result;
	}
	
	
	/**
	 * Método que indica cuando un elemento se puede borrar
	 * @param by Localizador del elemento
	 * @return True, si se puede pinchar en él.
	 */
	public boolean retryingFindClear(By by) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.retryingFindClear");
		}
		boolean result = false;
		int attempts = 0;
		while (attempts < Constants.RETRY) {
			try {
				getDriver().findElement(by).clear();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				LOGGER.error("retryingFindClear: El elemento no se encuentra en la caché: " + by.toString());
			}
			attempts++;
		}
		return result;
	}
	
	/**
	 * Función que espera por la llamada de un AJax
	 * @throws InterruptedException Excepción interrunpida
	 */
	public void waitForAjax() throws InterruptedException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.waitForAjax");
		}
		while (true) {
			Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor) getDriver()).executeScript("return jQuery.active == 0");
			if (ajaxIsComplete) {
				break;
			}
			Thread.sleep(100);
		}
	}
	
	/**
	 * Método que espera a que la página este totalmente cargada
	 */
	public void waitUntilDocumentIsReady() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.waitUntilDocumentIsReady");
		}
		Wait<WebDriver> wait = new FluentWait<>(getDriver()).withTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS).ignoring(UnhandledAlertException.class);

		wait.until(new Function<WebDriver, Boolean>() {
			@Override
			public Boolean apply(WebDriver f) {
				return ((JavascriptExecutor) f).executeScript("return document.readyState").equals("complete");
			}
		});
	}
	
	/**
	 * Método que espera hasta que el elemento este visible
	 * @param locator Localizador
	 * @return Elemento
	 */
	public WebElement getWhenVisible(By locator) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.getWhenVisible");
		}
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(getDriver(), Constants.TIME_OUT_SECONDS);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		return element;
	}
	
	
	/**
	 * Se espera hasta que el elemento se pueda pinchar en él.
	 * @param locator Localizador del elemento
	 */
	public void clickWhenReady(By locator) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.clickWhenReady");
		}
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(getDriver(), Constants.TIME_OUT_SECONDS);
		element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();
	}
	  
	  
	  /**
	   * An expectation for checking if the given text is present in the specified
	   * elements value attribute.
	   *
	   * @param locator used to find the element
	   * @param text to be present in the value attribute of the element found by the locator
	   * @param attribute value attribute of the element found by the locator
	   * @return true once the value attribute of the first element located by locator contains
	   * the given text
	   */
	  public static ExpectedCondition<Boolean> textToBePresentInElementAttribute(
	      final By locator, final String text, final String attribute) {

	    return new ExpectedCondition<Boolean>() {
	      @Override
	      public Boolean apply(WebDriver driver) {
	        try {
	          String elementText = driver.findElement(locator).getAttribute(attribute);
	          if (elementText != null) {
	            return elementText.contains(text);
	          } else {
	            return false;
	          }
	        } catch (StaleElementReferenceException e) {
	          return null;
	        }
	      }

	      @Override
	      public String toString() {
	        return String.format("text ('%s') to be the attribute of element located by %s",
	            text, locator);
	      }
	    };
	  }
	  
	  /**
	   * Permite conocer si el navegador es Internet Explorer
	   * @return True si es, False si no es.
	   * @throws Exception Excepción producida
	   */
	  protected boolean isBrowserIE() throws Exception {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("WebDriverSelenium.isBrowserIE");
			}
			
			boolean isIE = false;
			
			Capabilities cap = ((RemoteWebDriver) getDriver()).getCapabilities();
		    String browserName = cap.getBrowserName().toLowerCase();
		    
		    if (Browser.IE.getValue().equals(browserName)) {
		    	isIE = true;
		    }
			
			return isIE;
	  }

	public void login(String userName, String password)
			throws Exception {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.login - JIMEIV-718");
		}

		loginPage(userName, password);

		AssertJUnit.assertEquals(PropertiesFile.getValue(MESSAGE_lOGIN),
				untilGetTextElement(By.cssSelector("div.lado_derecho > div.h2")));
	}
	
	protected void loginPage(String userName, String password) 
			throws Exception {
		loginFormPage(userName,password);
		getDriver().findElement(By.name("submit")).click();
	}
	
	protected void loginFormPage(String userName, String password) 
			throws Exception {
		getDriver().get(ConfigFile.getValue(Constants.BASE_URL));
		
		// Completamos el usuario
		By user = By.id("username");
		untilClearElement(user);
		untilSendKeysElement(user, userName);
		
		//Completamos la password
		By pass = By.id("password");
		untilClearElement(pass);
		untilSendKeysElement(pass, password);
	}
	
	
	
	/**
	 * JIMEIV-720: Cierre de sesión
	 */
	protected void cerrarSesion() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("WebDriverSelenium.cerrarSesion - JIMEIV-720");
		}
		//waitForLayout();
		
		By cerrar = By.cssSelector("img[title=\"" + PropertiesFile.getValue(BTN_CLOSE) + "\"]");
		clickWhenReady(cerrar);
		//untilClickElement(cerrar);
	}
	
	protected void waitForLayout() throws Exception {
		//Esperar hasta ocultar Capa Popup modal
		By capa = By.xpath("//*[local-name()='div'] [contains(@id,'_modal') and (not(contains(@id,'formModalPanels:'))) and (contains(@class,'ui-widget-overlay'))]");
		WebDriverWait wait = new WebDriverWait(getDriver(), Constants.TIME_OUT_SECONDS);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(capa));
	}
}
