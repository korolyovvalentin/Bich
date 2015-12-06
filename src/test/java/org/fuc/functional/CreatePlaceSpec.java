package org.fuc.functional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class CreatePlaceSpec extends BaseTest {

    @Test
    public void createPlaceTest() {
        new Authentication(host, driver).tryAuthenticate("Kingpin", "12345");

        driver.get(host + "/business/places");
        int rowsBefore = driver.findElementsByTagName("tr").size();

        driver.get(host + "/business/places/create");
        driver.findElementByCssSelector("[name=name]").sendKeys("NewPlace");
        driver.findElementByCssSelector("[name=description]").sendKeys("Description");
        driver.findElementByCssSelector("[type=submit]").click();

        int rowsAfter = driver.findElementsByTagName("tr").size();
        assertThat(rowsAfter, is(rowsBefore + 1));
    }
}
