package eu.bsinfo.wip.management.mvc;

import eu.bsinfo.wip.management.mvc.main.MainView;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Views {

    private static final List<LoadedMvc<?>> loadedViews = new LinkedList<>();
    private static final MainView ROOT_VIEW = new MainView();

    private Views() {
    }

    public static <T extends MvcView<?>> void startView(T view, MvcController<T> controller, Container container) {
        var loadedView = new LoadedMvc<T>(view, controller);
        loadedViews.add(loadedView);
        view.bind(container);
        controller.setView(view);
        controller.init();
    }

    public static <T extends MvcWindowView<?>> void startView(T view, MvcController<T> controller) {
        startView(view, controller, null);
    }

    public static <T extends MvcView<?>> void openTab(String tabName, T view, MvcController<T> controller) {
        ROOT_VIEW.openTab(tabName, view, controller);
    }

    public static void refreshAll() {
        loadedViews.forEach(lv -> {
            if (lv.controller instanceof Refreshable refreshable)
                refreshable.refresh();
        });
    }

    public static MainView getRootView() {
        return ROOT_VIEW;
    }

    @AllArgsConstructor
    private static class LoadedMvc<T extends MvcView<?>> {
        private T view;
        private MvcController<? extends MvcView<?>> controller;
    }
}
