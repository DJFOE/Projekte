package eu.bsinfo.wip.management.mvc.customer.list;

import eu.bsinfo.wip.management.mvc.MvcController;
import eu.bsinfo.wip.management.mvc.MvcView;
import eu.bsinfo.wip.management.mvc.Refreshable;
import eu.bsinfo.wip.management.mvc.Views;
import eu.bsinfo.wip.management.mvc.customer.edit.EditCustomerController;
import eu.bsinfo.wip.management.mvc.customer.edit.EditCustomerModel;
import eu.bsinfo.wip.management.mvc.customer.edit.EditCustomerModelConverter;
import eu.bsinfo.wip.management.mvc.customer.edit.EditCustomerView;
import eu.bsinfo.wip.management.mvc.measurement.list.ListMeasurementController;
import eu.bsinfo.wip.management.mvc.measurement.list.ListMeasurementView;
import eu.bsinfo.wip.management.resource.ResourceOperationException;
import eu.bsinfo.wip.management.resource.customer.CustomerResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;

public class ListCustomerController extends MvcController<ListCustomerView> implements Refreshable {

    private static final Logger LOG = LogManager.getLogger(ListCustomerController.class);

    private EditCustomerModelConverter modelConverter = new EditCustomerModelConverter();
    private CustomerResource customerResource = new CustomerResource();


    @Override
    public void init() {
        view.SearchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        view.addItem.addActionListener(e -> {
            EditCustomerView editView = new EditCustomerView();
            Views.startView(editView, new EditCustomerController());

            editView.show();

            editView.setWindowClosedOperation(this::refresh);
        });

        view.editItem.addActionListener(e -> {
            EditCustomerView editView = new EditCustomerView();
            Views.startView(editView, new EditCustomerController());

            EditCustomerModel model = modelConverter.entityToModel(view.getModel().getSelectedCustomers().get(0));

            editView.fillView(model);

            editView.show();

            editView.setWindowClosedOperation(this::refresh);
        });

        view.deleteItem.addActionListener(a -> {
            if (view.askUser(MvcView.OK_CANCEL_OPTION, "Aktion bestätigen: Kunde löschen", "Kunde endgültig löschen?") == MvcView.YES_OPTION) {
                view.getModel().getSelectedCustomers().forEach(m -> {
                    try {
                        customerResource.delete(m.getId());
                    } catch (ResourceOperationException e) {
                        LOG.error(e);
                        view.notifyUser(MvcView.ERROR, "Fehler", e.getMessage());
                    }
                });

                refresh();
            }
        });

        view.showMeasurementItem.addActionListener(e -> {
            var selectedCustomer = view.getModel().getSelectedCustomers().get(0);

            Views.openTab("Alle Messungen für Kunde " + selectedCustomer.getName(), new ListMeasurementView(), new ListMeasurementController(selectedCustomer.getId()));
        });

        refresh();
    }

    @Override
    public void refresh() {
        ListCustomerModel model = new ListCustomerModel();
        try {
            model.setCustomers(customerResource.getAll());
        } catch (ResourceOperationException e) {
            model.setCustomers(Collections.emptyList());
            LOG.error(e);
            view.notifyUser(MvcView.ERROR, "Fehler", "Kunden konnten nicht aktualisiert werden");
        }
        view.fillView(model);
    }
}
