package eu.bsinfo.wip.management.mvc;

import javax.swing.*;

/**
 * MvcView for opening new windows
 * MvcWindowView will init the container by itself,
 * just don't forget to call super.init()
 * <b>before</b> you overwrite it
 *
 * @param <M> model
 */
public abstract class MvcWindowView<M> extends MvcView<M> {

    protected JFrame frame;
    private Runnable windowClosedOperation;

    public MvcWindowView(final String name) {
        super(name);
    }

    @Override
    protected void init() {
        frame = new JFrame();
        container = frame.getContentPane();
        frame.setTitle(this.getName());
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        if (windowClosedOperation != null)
            windowClosedOperation.run();
        frame.setVisible(false);
    }

    public void setWindowClosedOperation(final Runnable windowClosedOperation) {
        this.windowClosedOperation = windowClosedOperation;
    }
}
