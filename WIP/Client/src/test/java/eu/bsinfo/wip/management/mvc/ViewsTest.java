package eu.bsinfo.wip.management.mvc;

import com.formdev.flatlaf.FlatLightLaf;
import eu.bsinfo.wip.management.mvc.main.MainController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

class ViewsTest {

    private static final Logger LOG = LogManager.getLogger(ViewsTest.class);
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            // I recommend to use this theme, but we need to change JDatePicker
            // UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            LOG.error("Cannot load theme", ex);
        }

        Views.startView(Views.getRootView(), new MainController());
        Views.getRootView().show();
        LOG.info("Main view started");
    }

}
