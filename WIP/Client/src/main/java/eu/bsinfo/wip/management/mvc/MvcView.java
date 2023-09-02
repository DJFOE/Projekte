package eu.bsinfo.wip.management.mvc;

import javax.swing.*;
import java.awt.*;

/**
 * Base view for binding onto swing containers
 *
 * @param <M> model
 */
public abstract class MvcView<M> {

    public static final int SUCCESS = JOptionPane.INFORMATION_MESSAGE;
    public static final int ERROR = JOptionPane.ERROR_MESSAGE;

    public static final int YES_NO_OPTION = JOptionPane.YES_NO_OPTION;
    public static final int OK_CANCEL_OPTION = JOptionPane.OK_CANCEL_OPTION;
    public static final int YES_OPTION = JOptionPane.YES_OPTION;
    public static final int NO_OPTION = JOptionPane.NO_OPTION;

    protected Container container;

    private String name;

    public MvcView(String name) {
        this.name = name;
    }

    void bind(Container container) {
        this.container = container;
        init();
    }

    protected abstract void init();

    public abstract void fillView(M model);

    public abstract M getModel();

    public void notifyUser(int status, String title, String... messages) {
        JOptionPane.showMessageDialog(this.container, String.join("\n", messages), title, status);
    }

    public int askUser(int options, String title, String messages) {
        return JOptionPane.showConfirmDialog(this.container, String.join("\n", messages), title, options);
    }

    public String getName() {
        return name;
    }

    public Container getContainer() {
        return container;
    }
}
