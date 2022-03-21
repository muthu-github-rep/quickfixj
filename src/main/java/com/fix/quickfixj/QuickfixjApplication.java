package com.fix.quickfixj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Import({TradeAppAcceptorConfig.class})
public class QuickfixjApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(QuickfixjApplication.class, args);
		QuickFixJServerConfiguration quickFixJServer=applicationContext.getBean(QuickFixJServerConfiguration.class);
		quickFixJServer.loadConfiguration();
		quickFixJServer.logon();
		quickFixJServer.sendMessage();



	}

}
