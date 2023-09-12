package f4.woorimock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class WoorimockApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoorimockApplication.class, args);
	}

}
