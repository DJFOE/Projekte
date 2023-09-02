package eu.bsinfo.wip.management.mvc.main;

import eu.bsinfo.wip.management.mvc.MvcController;
import eu.bsinfo.wip.management.mvc.Views;
import eu.bsinfo.wip.management.mvc.customer.edit.EditCustomerController;
import eu.bsinfo.wip.management.mvc.customer.edit.EditCustomerModel;
import eu.bsinfo.wip.management.mvc.customer.edit.EditCustomerView;
import eu.bsinfo.wip.management.mvc.customer.list.ListCustomerController;
import eu.bsinfo.wip.management.mvc.customer.list.ListCustomerView;
import eu.bsinfo.wip.management.mvc.measurement.edit.EditMeasurementController;
import eu.bsinfo.wip.management.mvc.measurement.edit.EditMeasurementModel;
import eu.bsinfo.wip.management.mvc.measurement.edit.EditMeasurementView;
import eu.bsinfo.wip.management.mvc.measurement.list.ListMeasurementController;
import eu.bsinfo.wip.management.mvc.measurement.list.ListMeasurementView;

import java.time.LocalDate;

public class MainController extends MvcController<MainView> {

    @Override
    public void init() {
        view.newMeasurement.addActionListener(e -> {
            EditMeasurementView editMeasurementView = new EditMeasurementView();
            Views.startView(editMeasurementView, new EditMeasurementController());

            EditMeasurementModel model = new EditMeasurementModel();
            model.setDate(LocalDate.now());

            editMeasurementView.fillView(model);

            editMeasurementView.show();
        });
        view.showMeasurement.addActionListener(e ->
                view.openTab("Alle Messungen", new ListMeasurementView(), new ListMeasurementController())
        );

        view.newCustomer.addActionListener(e -> {
            EditCustomerView editCustomerView = new EditCustomerView();
            Views.startView(editCustomerView, new EditCustomerController());

            EditCustomerModel model = new EditCustomerModel();

            editCustomerView.fillView(model);

            editCustomerView.show();
        });
        view.showCustomer.addActionListener(e ->
                view.openTab("Alle Kunden", new ListCustomerView(), new ListCustomerController())
        );
    }
}
