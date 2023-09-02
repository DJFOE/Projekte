package eu.bsinfo.wip.management;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClientConfigurationTest {

    @Test
    void test() {
        Assertions.assertEquals("hello,world", ClientConfiguration.getConfig().getString("test"));
    }
}
