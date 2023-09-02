package eu.bsinfo.wip.management;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class ClientConfiguration {

    private static final Logger LOG = LogManager.getLogger(ClientConfiguration.class);
    private static ClientConfiguration instance;
    private final Properties properties;

    private ClientConfiguration() {
        properties = new Properties();
        try {
            properties.load(ClientConfiguration.class.getClassLoader().getResourceAsStream("client.properties"));
        } catch (IOException e) {
            LOG.error("Cannot load properties file", e);
        }
    }

    public static ClientConfiguration getConfig() {
        if (instance == null)
            instance = new ClientConfiguration();
        return instance;
    }

    public String getString(String key) {
        return this.properties.getProperty(key);
    }
}
