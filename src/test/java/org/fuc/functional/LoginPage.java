package org.fuc.functional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class LoginPage extends BaseTest {
    @Test
    public void validUserCredential() {
        new Authentication(host, driver).tryAuthenticate("Keroic", "12345");
        assertThat(driver.getCurrentUrl(), isIn(new String[]{host, host + "/"}));
    }

    @Test
    public void invalidUserCredential() {
        new Authentication(host, driver).tryAuthenticate("Keroic", "123456");
        assertThat(driver.getCurrentUrl(), is(host + "/signin?error=1"));
    }
}
