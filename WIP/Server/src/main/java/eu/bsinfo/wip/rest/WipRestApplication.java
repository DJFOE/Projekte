package eu.bsinfo.wip.rest;

import eu.bsinfo.wip.rest.server.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Management REST Application",
        description = "REST Service for management"))
public class WipRestApplication {
	public static void main(String[] args) {

		Server.start();
	}

}
