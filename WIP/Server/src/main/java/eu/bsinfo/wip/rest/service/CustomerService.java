package eu.bsinfo.wip.rest.service;

import eu.bsinfo.wip.rest.entity.Customer;
import eu.bsinfo.wip.rest.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    @Autowired
    public CustomerRepository repository;

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public Optional<Customer> getById(UUID id) {
        if (id == null)
            return Optional.empty();
        return repository.findById(id);
    }

    public Customer create(Customer customer) {
        // we could replace this with customer.setId(null)
        Assert.isNull(customer.getId(), "Creating customer id must be null");
        return repository.save(customer);
    }

    public Customer update(Customer customer) {
        Assert.notNull(customer.getId(), "Updating customer id may not be null");
        return repository.save(customer);
    }

    public void delete(UUID id) {
        Assert.notNull(id, "ID to delete may not be null");
        repository.deleteById(id);
    }

}
