package eu.bsinfo.wip.management.mvc.measurement.edit;

import eu.bsinfo.wip.management.mvc.MvcWindowView;
import eu.bsinfo.wip.management.resource.measurement.Measurement;
import eu.bsinfo.wip.management.ui.JDatePicker;
import eu.bsinfo.wip.management.ui.NumberInputVerifier;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class EditMeasurementView extends MvcWindowView<EditMeasurementModel> {

    private JTextField customerIdField, counterIdField, counterValueField, commentField;
    private JComboBox<Measurement.CounterType> counterTypeField;
    private JDatePicker dateField;
    private JCheckBox newEntryField;
    protected JButton saveButton;

    private UUID currentId;

    public EditMeasurementView() {
        super("Messung bearbeiten");
    }

    @Override
    protected void init() {
        super.init();

        frame.setTitle("Messung bearbeitet");
        frame.setSize(325, 300);
        GridLayout formLayout = new GridLayout(7, 2);
        formLayout.setVgap(2);
        formLayout.setHgap(2);

        JPanel formPanel = new JPanel(formLayout);
        formPanel.setBorder(new EmptyBorder(8, 8, 0, 8));

        formPanel.add(new JLabel("Kundennummer"));
        formPanel.add(customerIdField = new JTextField());

        formPanel.add(new JLabel("Zählerart"));
        formPanel.add(counterTypeField = new JComboBox<>(Measurement.CounterType.values()));

        formPanel.add(new JLabel("Zählernummer"));
        formPanel.add(counterIdField = new JTextField());

        formPanel.add(new JLabel("Datum"));
        formPanel.add(dateField = new JDatePicker());

        formPanel.add(new JLabel("Zählerstand"));
        formPanel.add(counterValueField = new JTextField());
        counterValueField.setInputVerifier(new NumberInputVerifier());

        formPanel.add(new JLabel("Kommentar"));
        formPanel.add(commentField = new JTextField());

        formPanel.add(new JLabel("Neu eingebaut"));
        formPanel.add(newEntryField = new JCheckBox("Neu eingebaut"));

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(saveButton = new JButton("Speichern"));
        buttonPanel.setBorder(new EmptyBorder(0, 8, 8, 8));

        container.add(formPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void fillView(final EditMeasurementModel model) {
        currentId = model.getId();
        customerIdField.setText(model.getCustomerId() == null ? "" : String.valueOf(model.getCustomerId()));
        counterTypeField.setSelectedItem(model.getType());
        counterIdField.setText(model.getCounterId());
        dateField.getModel().setSelected(true); // To show the date in the preview field
        dateField.getModel().setValue(Date.from(model.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        newEntryField.setSelected(model.getNewEntry() != null && model.getNewEntry());
        counterValueField.setText(model.getCounterValue() == null ? "" : String.valueOf(model.getCounterValue()));
        commentField.setText(model.getComment());
    }

    @Override
    public EditMeasurementModel getModel() {
        EditMeasurementModel out = new EditMeasurementModel();
        out.setId(currentId);
        out.setCounterId(this.counterIdField.getText());
        out.setNewEntry(this.newEntryField.isSelected());
        out.setComment(this.commentField.getText());
        out.setType((Measurement.CounterType) this.counterTypeField.getSelectedItem());
        var date = LocalDate.ofInstant(this.dateField.getModel().getValue().toInstant(), ZoneId.systemDefault());
        out.setDate(date);
        out.setCustomerId(this.customerIdField.getText());
        out.setCounterValue(this.counterValueField.getText());
        return out;
    }
}
