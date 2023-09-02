package eu.bsinfo.wip.rest.server;

import eu.bsinfo.wip.rest.WipRestApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;


public class Server {
    private static ConfigurableApplicationContext ctx;
    public static void start(){
        ctx = SpringApplication.run(WipRestApplication.class);
    }
    public static void shutDown(){
        int exitCode = SpringApplication.exit(ctx, () -> 0);
        System.exit(exitCode);
    }
}
