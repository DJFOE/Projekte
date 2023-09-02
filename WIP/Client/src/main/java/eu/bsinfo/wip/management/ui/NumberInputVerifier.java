package eu.bsinfo.wip.management.ui;

import com.formdev.flatlaf.util.StringUtils;

import javax.swing.*;

/**
 * InputVerifier for JTextFields that checks for number inputs (long)
 */
public class NumberInputVerifier extends InputVerifier {

    @Override
    public boolean verify(final JComponent input) {
        JTextField textField = (JTextField) input;

        if (StringUtils.isEmpty(textField.getText())) {
            input.putClientProperty("JComponent.outline", "warning");
            input.setToolTipText("Füllen Sie bitte dieses Feld aus");
            return false;
        }

        try {
            Long.parseLong(textField.getText());
            input.putClientProperty("JComponent.outline", "");
            input.setToolTipText("");
            return true;
        } catch (Exception e) {
            input.putClientProperty("JComponent.outline", "error");
            input.setToolTipText("Geben Sie bitte eine Zahl ein");
            return false;
        }
    }

    @Override
    public boolean shouldYieldFocus(final JComponent source, final JComponent target) {
            /*if (verify(source))
                return true;
            return false;*/
        return verify(source);
        //return true;
    }
}
