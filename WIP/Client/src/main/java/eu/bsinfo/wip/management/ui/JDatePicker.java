package eu.bsinfo.wip.management.ui;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class JDatePicker extends JDatePickerImpl {

    private static final Properties i18nProps;

    static {
        i18nProps = new Properties();
        i18nProps.put("text.today", "Heute");
        i18nProps.put("text.month", "Monat");
        i18nProps.put("text.year", "Jahr");
    }

    public JDatePicker() {
        super(new JDatePanelImpl(new UtilDateModel(), i18nProps), new DateLabelFormatter());
    }

    @Override
    public UtilDateModel getModel() {
        return (UtilDateModel) super.getModel();
    }

    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final String DATE_PATTERN = "dd-MM-yyyy";
        private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return DATE_FORMAT.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return DATE_FORMAT.format(cal.getTime());
            }
            return "";
        }

    }
}
