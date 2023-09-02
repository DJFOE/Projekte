package eu.bsinfo.wip.management.mvc.customer.edit;

import eu.bsinfo.wip.management.mvc.ModelConverter;
import eu.bsinfo.wip.management.resource.customer.Customer;

public class EditCustomerModelConverter implements ModelConverter<Customer, EditCustomerModel> {

    @Override
    public EditCustomerModel entityToModel(final Customer entity) {
        EditCustomerModel model = new EditCustomerModel();

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setFirstName(entity.getFirstName());

        return model;
    }

    @Override
    public Customer modelToEntity(final EditCustomerModel model) {
        Customer customer = new Customer();

        customer.setId(model.getId());
        customer.setName(model.getName());
        customer.setFirstName(model.getFirstName());


        return customer;
    }
}
