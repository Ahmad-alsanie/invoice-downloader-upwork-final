package com.carls;

import com.carls.service.UpworkAutomationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UpworkAutomationRunner implements CommandLineRunner {

    @Autowired
    private UpworkAutomationService automationService;

    @Override
    public void run(String... args) throws Exception {
        try {
            automationService.loginToUpwork();
            automationService.navigateAndDownloadInvoices();
        } finally {
            automationService.quitDriver();
        }
    }
}
