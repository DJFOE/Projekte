package eu.bsinfo.wip.management.mvc.customer.list;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import eu.bsinfo.wip.management.mvc.MvcView;
import eu.bsinfo.wip.management.resource.customer.Customer;
import eu.bsinfo.wip.management.ui.JDataTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListCustomerView extends MvcView<ListCustomerModel> {

    protected JTextField SearchBar;
    protected JMenuItem addItem, editItem, deleteItem, showMeasurementItem;
    private JDataTable<Customer> dataTable = new JDataTable<>();
    private List<Customer> customers;
    protected int[] selectedIndicies = new int[0];

    public ListCustomerView() {
        super("Kunden");
    }

    @Override
    protected void init() {
        SearchBar = new JTextField();
        JTextField SearchBar = JDataTable.createRowFilter(dataTable);

        {
            SearchBar.setBorder(new EmptyBorder(0, 4, 4, 4));
            SearchBar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nach Kunden suchen...");
            SearchBar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSearchIcon());
            SearchBar.setPreferredSize(new Dimension(200, 20));
        }

        dataTable.getModel().addColumn("Kundennummer", UUID.class, Customer::getId);
        dataTable.getModel().addColumn("Nachname", String.class, Customer::getName);
        dataTable.getModel().addColumn("Vorname", String.class, Customer::getFirstName);

        dataTable.getPopupMenu().add(addItem = new JMenuItem("Neu"));
        dataTable.getPopupMenu().add(editItem = new JMenuItem("Editieren"));
        dataTable.getPopupMenu().add(deleteItem = new JMenuItem("Löschen"));
        dataTable.getPopupMenu().add(showMeasurementItem = new JMenuItem("Alle Messungen für Kunde"));

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
    public void fillView(final ListCustomerModel model) {
        customers = model.getCustomers();

        dataTable.getModel().setRowCount(0); // clear entries
        model.getCustomers().forEach(m -> dataTable.getModel().addRow(m));
    }

    @Override
    public ListCustomerModel getModel() {
        ListCustomerModel model = new ListCustomerModel();
        model.setCustomers(customers);
        List<Customer> selected = Arrays.stream(dataTable.getSelectedRows())
                .mapToObj(i -> customers.get(i))
                .collect(Collectors.toList());
        model.setSelectedCustomers(selected);
        return model;
    }

    public String getSearchText() {
        return SearchBar.getText().trim();
    }
}
