package org.fuc.functional;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

class Authentication {
    private String host;
    private RemoteWebDriver driver;

    public Authentication(String host, RemoteWebDriver driver) {
        this.host = host;
        this.driver = driver;
    }

    public void tryAuthenticate(String username, String password){
        driver.get(host + "/signin");

        driver.findElement(By.cssSelector("#inputEmail")).sendKeys(username);
        driver.findElement(By.cssSelector("#inputPassword")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type=submit]")).click();
    }
}
