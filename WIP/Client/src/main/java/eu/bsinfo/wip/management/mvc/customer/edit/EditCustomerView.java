package eu.bsinfo.wip.management.mvc.customer.edit;

import eu.bsinfo.wip.management.mvc.MvcWindowView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.UUID;

public class EditCustomerView extends MvcWindowView<EditCustomerModel> {

    private JTextField nameField, firstNameField;

    protected JButton saveButton;

    private UUID currentId;

    public EditCustomerView() {
        super("Kunden bearbeiten");
    }

    @Override
    protected void init() {
        super.init();

        frame.setTitle("Kunden bearbeitet");
        frame.setSize(325, 300);
        GridLayout formLayout = new GridLayout(7, 2);
        formLayout.setVgap(2);
        formLayout.setHgap(2);

        JPanel formPanel = new JPanel(formLayout);
        formPanel.setBorder(new EmptyBorder(8, 8, 0, 8));

        formPanel.add(new JLabel("Nachname"));
        formPanel.add(nameField = new JTextField());


        formPanel.add(new JLabel("Vorname"));
        formPanel.add(firstNameField = new JTextField());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(saveButton = new JButton("Speichern"));
        buttonPanel.setBorder(new EmptyBorder(0, 8, 8, 8));

        container.add(formPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void fillView(final EditCustomerModel model) {
        currentId = model.getId();
        nameField.setText(model.getName() == null ? "" : String.valueOf(model.getName()));
        firstNameField.setText(model.getFirstName());
    }

    @Override
    public EditCustomerModel getModel() {
        EditCustomerModel out = new EditCustomerModel();
        out.setId(currentId);
        out.setFirstName(this.firstNameField.getText());
        out.setName(this.nameField.getText());
        return out;
    }
}
