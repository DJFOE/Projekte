package eu.bsinfo.wip.management.mvc.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.StringUtils;
import eu.bsinfo.wip.management.mvc.MvcController;
import eu.bsinfo.wip.management.mvc.MvcView;
import eu.bsinfo.wip.management.mvc.MvcWindowView;
import eu.bsinfo.wip.management.mvc.Views;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class MainView extends MvcWindowView<Object> {

    private JTabbedPane tabbedPane;

    protected JMenuItem newMeasurement, newCustomer, showMeasurement, showCustomer;

    public MainView() {
        super("Hausverwaltung");
    }

    @Override
    protected void init() {
        super.init();

        frame.setSize(600, 400);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        {
            JMenu newEntry = new JMenu("Neu");
            newMeasurement = new JMenuItem("Neue Messung");
            newCustomer = new JMenuItem("Neuer Kunde");
            newEntry.add(newMeasurement);
            newEntry.add(newCustomer);

            menuBar.add(newEntry);

            JMenu show = new JMenu("Anzeige");
            showMeasurement = new JMenuItem("Messungen öffnen");
            showCustomer = new JMenuItem("Kunden öffnen");
            show.add(showMeasurement);
            show.add(showCustomer);
            menuBar.add(show);
        }
        frame.setJMenuBar(menuBar);

        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, true);
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT, "Close");
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK,
                (BiConsumer<JTabbedPane, Integer>) (tabPane, tabIndex) -> {
                    AWTEvent e = EventQueue.getCurrentEvent();
                    tabPane.removeTabAt(tabIndex);
                });
        container.add(tabbedPane);
    }

    public <T extends MvcView<?>> void openTab(String tabName, T view, MvcController<T> controller) {
        for (int tabIndex = 0; tabIndex < tabbedPane.getTabCount(); tabIndex++) {
            if (tabbedPane.getTitleAt(tabIndex).equals(tabName)) {
                tabbedPane.setSelectedIndex(tabIndex);
                return;
            }
        }

        JPanel panel = new JPanel();
        Views.startView(view, controller, panel);
        tabbedPane.addTab(StringUtils.isEmpty(tabName) ? view.getName() : tabName, panel);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    @Override
    public void fillView(final Object model) {
        // not needed
    }

    @Override
    public Object getModel() {
        return null;
    }
}
