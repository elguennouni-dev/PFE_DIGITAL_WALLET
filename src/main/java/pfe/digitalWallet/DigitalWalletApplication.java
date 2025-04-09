package pfe.digitalWallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DigitalWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalWalletApplication.class, args);
	}

}
