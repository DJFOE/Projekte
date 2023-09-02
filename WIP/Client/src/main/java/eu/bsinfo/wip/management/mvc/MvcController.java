package eu.bsinfo.wip.management.mvc;

public abstract class MvcController<V extends MvcView<?>> {

    protected V view;

    public abstract void init();

    public void setView(final V view) {
        this.view = view;
    }
}
