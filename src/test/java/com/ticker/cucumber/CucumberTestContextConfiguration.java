package com.ticker.cucumber;

import com.ticker.TickerApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = TickerApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
