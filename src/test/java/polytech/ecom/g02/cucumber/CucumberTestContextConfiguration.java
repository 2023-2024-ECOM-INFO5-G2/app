package polytech.ecom.g02.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import polytech.ecom.g02.IntegrationTest;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
