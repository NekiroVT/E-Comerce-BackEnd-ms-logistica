package com.mslogistica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient // 🔁 Para que se registre en Eureka
@EnableFeignClients
public class MsLogisticaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsLogisticaApplication.class, args);
	}

}
