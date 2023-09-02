package eu.bsinfo.wip.management.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class JDataTable<DATA> extends JTable {
    //TODO row Filter, get Table sorter, expose table in view oder view exposen
    private JPopupMenu popupMenu;
    protected TableRowSorter<DataTableModel> tableSorter;
    private DataTableModel tableModel;

    public JDataTable() {
        tableModel = new DataTableModel();
        setModel(tableModel);

        tableSorter = new TableRowSorter<>(tableModel);
        setRowSorter(tableSorter);

        popupMenu = new JPopupMenu();
        setComponentPopupMenu(popupMenu);
        popupMenu.addPopupMenuListener(new DataTablePopupMenuListener());
    }

    @Override
    public DataTableModel getModel() {
        return tableModel;
    }

    @Override
    public int[] getSelectedRows() {
        return Arrays.stream(super.getSelectedRows()).map(this::convertRowIndexToModel).toArray();
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public class DataTableModel extends DefaultTableModel {
        private List<JDataTableColumn<?>> columns = new ArrayList<>();
        private Function<DATA, Object[]> toRowFunction;

        public <TYPE> void addColumn(final String columnName, Class<TYPE> type, final Function<DATA, TYPE> rowValueFunction) {
            try {
                columns.add(new JDataTableColumn<>(columnName, type, rowValueFunction));
                tableModel.addColumn(columnName);
                toRowFunction = data -> columns.stream().map(c -> c.rowValueFunction.apply(data)).toArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columns.get(columnIndex).type;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public void addRow(DATA data) {
            if (toRowFunction == null) {
                // panic
                throw new IllegalStateException("Cannot add row for " + data + "! Maybe forgot to add columns?");
            }
            super.addRow(toRowFunction.apply(data));
        }
    }

    private class DataTablePopupMenuListener implements PopupMenuListener {
        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
            SwingUtilities.invokeLater(() -> {
                // get row under cursor
                int rowAtPoint = rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), JDataTable.this));
                if (rowAtPoint > -1) {
                    setRowSelectionInterval(rowAtPoint, rowAtPoint);
                    grabFocus();
                }
            });
        }

        @Override
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
            // not needed
        }

        @Override
        public void popupMenuCanceled(final PopupMenuEvent e) {
            // not needed
        }
    }

    @Getter
    @AllArgsConstructor
    private class JDataTableColumn<TYPE> {
        String name;
        Class<TYPE> type;
        Function<DATA, TYPE> rowValueFunction;
    }


    public static JTextField createRowFilter(JTable table) {
        RowSorter<? extends TableModel> rs = table.getRowSorter();
        if (rs == null) {
            table.setAutoCreateRowSorter(true);
            rs = table.getRowSorter();
        }

        TableRowSorter<? extends TableModel> rowSorter =
                (rs instanceof TableRowSorter) ? (TableRowSorter<? extends TableModel>) rs : null;

        if (rowSorter == null) {
            throw new RuntimeException("Cannot find appropriate rowSorter: " + rs);
        }

        final JTextField tf = new JTextField(15);
        tf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update(e);
            }

            private void update(DocumentEvent e) {
                String text = tf.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        return tf;
    }
}