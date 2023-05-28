package org.mberhe.management;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Phone Borrowing System", version = "1.0", description = "Documentation APIs v1.0"))
public class PhoneManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhoneManagementApplication.class, args);
	}

}
