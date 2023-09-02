package eu.bsinfo.wip.management.mvc.customer.edit;


import eu.bsinfo.wip.management.mvc.MvcController;
import eu.bsinfo.wip.management.mvc.MvcView;
import eu.bsinfo.wip.management.mvc.Views;
import eu.bsinfo.wip.management.resource.ResourceOperationException;
import eu.bsinfo.wip.management.resource.customer.Customer;
import eu.bsinfo.wip.management.resource.customer.CustomerResource;

public class EditCustomerController extends MvcController<EditCustomerView> {

    private EditCustomerModelConverter validator = new EditCustomerModelConverter();
    private CustomerResource customerResource = new CustomerResource();

    @Override
    public void init() {
        view.saveButton.addActionListener(e -> this.save());
    }

    public void save() {
        Customer customer;
        try {
            customer = validator.modelToEntity(view.getModel());
        } catch (Exception e) {
            // Validation error
            view.notifyUser(MvcView.ERROR, "Fehler", e.getMessage());
            return;
        }

        boolean isNew = customer.getId() == null;
        try {
            if (isNew) {
                customerResource.create(customer);
                view.notifyUser(MvcView.SUCCESS, "Gespeichert!", "Eintrag erfolgreich erstellt!");
            } else {
                customerResource.update(customer);
                view.notifyUser(MvcView.SUCCESS, "Gespeichert!", "Eintrag erfolgreich gespeichert!");
            }
            Views.refreshAll();

            view.hide();

        } catch (ResourceOperationException e) {
            view.notifyUser(MvcView.ERROR, "Fehler", e.getMessage());
        }
    }
}

