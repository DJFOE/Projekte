package eu.bsinfo.wip.management.mvc.customer.list;

import eu.bsinfo.wip.management.mvc.ModelConverter;
import eu.bsinfo.wip.management.mvc.customer.list.ListCustomerModel;
import eu.bsinfo.wip.management.resource.customer.Customer;

import java.util.List;

public class ListCustomerModelConverter implements ModelConverter<List<Customer>, ListCustomerModel> {

    @Override
    public ListCustomerModel entityToModel(final List<Customer> entities) {
        ListCustomerModel model = new ListCustomerModel();

        model.setCustomers(entities);

        return null;
    }

    @Override
    public List<Customer> modelToEntity(final ListCustomerModel model) {
        return null;
    }
}
