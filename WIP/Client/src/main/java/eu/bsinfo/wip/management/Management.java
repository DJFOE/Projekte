package eu.bsinfo.wip.management;

import com.formdev.flatlaf.FlatLightLaf;
import eu.bsinfo.wip.management.mvc.Views;
import eu.bsinfo.wip.management.mvc.main.MainController;
import eu.bsinfo.wip.management.resource.measurement.Measurement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class Management {

    private static final Logger LOG = LogManager.getLogger(Management.class);

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

    public List<Measurement> searchDoubleEntries(Measurement measurement, List<Measurement> measurements) {
        List<Measurement> doublesFound = new LinkedList<>();

        for (Measurement cd2 : measurements) {
            if (isDouble(measurement, cd2)) {
                doublesFound.add(cd2);
            }
        }
        return doublesFound;
    }

    private boolean isDouble(Measurement cd1, Measurement cd2) {
        assert cd1.getCounterId() != null;
        return cd1.getCounterId().equals(cd2.getCounterId()) && cd1.getDate().equals(cd2.getDate());
    }
}
