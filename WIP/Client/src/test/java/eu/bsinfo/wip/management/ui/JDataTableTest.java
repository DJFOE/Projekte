package eu.bsinfo.wip.management.ui;

import eu.bsinfo.wip.management.resource.measurement.Measurement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class JDataTableTest {

    public static void main(String[] args) {
        var frame = new JFrame();
        var container = frame.getContentPane();
        var dataTable = new JDataTable<Measurement>();

        var measurements = Arrays.asList(
                new Measurement(UUID.randomUUID(), UUID.randomUUID(), Measurement.CounterType.GAS, "COUNTER#1", LocalDate.now(), false, 12333L, "Comment on counter 1"),
                new Measurement(UUID.randomUUID(), UUID.randomUUID(), Measurement.CounterType.WATER, "COUNTER#2", LocalDate.now(), false, 1233L, "Comment on counter 2"),
                new Measurement(UUID.randomUUID(), UUID.randomUUID(), Measurement.CounterType.POWER, "COUNTER#3", LocalDate.now(), true, 121133L, "Comment on counter 3")
        );

        dataTable.getModel().addColumn("Kundennummer", UUID.class, Measurement::getCustomerId);
        dataTable.getModel().addColumn("Datum", LocalDate.class, Measurement::getDate);
        dataTable.getModel().addColumn("Zählerart", Measurement.CounterType.class, Measurement::getCounterType);
        dataTable.getModel().addColumn("Zählernummer", String.class, Measurement::getCounterId);
        dataTable.getModel().addColumn("Zählerwert", Long.class, Measurement::getCounterValue);
        dataTable.getModel().addColumn("Kommentar", String.class, Measurement::getComment);

        measurements.forEach(m -> dataTable.getModel().addRow(m));

        dataTable.getPopupMenu().add(new JMenuItem("Edit"));
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(a -> {
            System.out.println(Arrays.toString(dataTable.getSelectedRows()));
            Arrays.stream(dataTable.getSelectedRows()).mapToObj(measurements::get).forEach(System.out::println);
        });
        dataTable.getPopupMenu().add(deleteItem);
        frame.setTitle("Test Frame");
        frame.setSize(500, 400);
        container.add(new JScrollPane(dataTable), BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

}
