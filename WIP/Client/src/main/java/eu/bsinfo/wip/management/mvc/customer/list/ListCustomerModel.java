package eu.bsinfo.wip.management.mvc.customer.list;

import eu.bsinfo.wip.management.resource.customer.Customer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListCustomerModel {

    private List<Customer> customers;
    private List<Customer> selectedCustomers;


}
