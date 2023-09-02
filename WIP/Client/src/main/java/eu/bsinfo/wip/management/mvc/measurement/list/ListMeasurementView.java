package eu.bsinfo.wip.management.mvc.measurement.list;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import eu.bsinfo.wip.management.mvc.MvcView;
import eu.bsinfo.wip.management.resource.measurement.Measurement;
import eu.bsinfo.wip.management.ui.JDataTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ListMeasurementView extends MvcView<ListMeasurementModel> {

    protected JTextField customerSearchBar;
    protected JMenuItem addItem, editItem, deleteItem;
    private JDataTable<Measurement> dataTable = new JDataTable<>();
    private List<Measurement> measurements;

    public ListMeasurementView() {
        super("Messungen");
    }

    @Override
    protected void init() {
        customerSearchBar = new JTextField();
        JTextField SearchBar = JDataTable.createRowFilter(dataTable);

        {
            SearchBar.setBorder(new EmptyBorder(0, 4, 4, 4));
            SearchBar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nach Ablesung suchen...");
            SearchBar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSearchIcon());
            SearchBar.setPreferredSize(new Dimension(200, 20));
        }

        dataTable.getModel().addColumn("Kundennummer", UUID.class, Measurement::getCustomerId);
        dataTable.getModel().addColumn("Datum", LocalDate.class, Measurement::getDate);
        dataTable.getModel().addColumn("Zählerart", Measurement.CounterType.class, Measurement::getCounterType);
        dataTable.getModel().addColumn("Zählernummer", String.class, Measurement::getCounterId);
        dataTable.getModel().addColumn("Zählerstand", Long.class, Measurement::getCounterValue);
        dataTable.getModel().addColumn("Kommentar", String.class, Measurement::getComment);

        dataTable.getPopupMenu().add(addItem = new JMenuItem("Neu"));
        dataTable.getPopupMenu().add(editItem = new JMenuItem("Editieren"));
        dataTable.getPopupMenu().add(deleteItem = new JMenuItem("Löschen"));


        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.add(SearchBar, BorderLayout.EAST);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        dataTable.setDefaultRenderer(Long.class, centerRenderer);
        dataTable.setDefaultRenderer(Object.class, centerRenderer);

        container.setLayout(new BorderLayout());

        container.add(searchBarPanel, BorderLayout.NORTH);
        container.add(new JScrollPane(dataTable), BorderLayout.CENTER);
    }

    @Override
    public void fillView(final ListMeasurementModel model) {
        measurements = model.getMeasurements();

        dataTable.getModel().setRowCount(0); // clear entries
        model.getMeasurements().forEach(m -> dataTable.getModel().addRow(m));
    }

    @Override
    public ListMeasurementModel getModel() {
        ListMeasurementModel model = new ListMeasurementModel();
        model.setMeasurements(measurements);
        List<Measurement> selected = Arrays.stream(dataTable.getSelectedRows())
                .mapToObj(i -> measurements.get(i))
                .toList();
        model.setSelectedMeasurements(selected);
        return model;
    }

    public String getSearchText() {
        return customerSearchBar.getText().trim();
    }
}
