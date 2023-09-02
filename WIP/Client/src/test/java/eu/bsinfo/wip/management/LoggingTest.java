package eu.bsinfo.wip.management;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

class LoggingTest {

    private static final Logger LOG = LogManager.getLogger(LoggingTest.class);

    @Test
    void testLogMessageFormatting() {
        LOG.info("Info message");
        LOG.warn("Warn message");
        LOG.error("Error message");
        LOG.debug("Debug message");
        LOG.trace("Trace message");
        LOG.fatal("Fatal message", new Exception());
    }

}
