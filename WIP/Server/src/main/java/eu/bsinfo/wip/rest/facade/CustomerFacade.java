package eu.bsinfo.wip.rest.facade;

import eu.bsinfo.wip.rest.entity.Customer;
import eu.bsinfo.wip.rest.exceptions.CustomerNotFoundException;
import eu.bsinfo.wip.rest.facade.dto.customer.CreateCustomerDto;
import eu.bsinfo.wip.rest.facade.dto.customer.CustomerResponseDto;
import eu.bsinfo.wip.rest.facade.dto.customer.UpdateCustomerDto;
import eu.bsinfo.wip.rest.service.CustomerService;
import eu.bsinfo.wip.rest.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Transactional
public class CustomerFacade {

    @Autowired
    public CustomerService customerService;

    @Autowired
    public MeasurementService measurementService;

    public CustomerResponseDto getById(UUID id) {
        return toDto(customerService.getById(id)
                .orElseThrow(CustomerNotFoundException::new));
    }

    public List<CustomerResponseDto> getAll() {
        return customerService.getAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public CustomerResponseDto create(CreateCustomerDto createDto) {
        Customer newCustomer = new Customer();
        newCustomer.setName(createDto.getName());
        newCustomer.setFirstName(createDto.getFirstName());
        return toDto(customerService.create(newCustomer));
    }

    public CustomerResponseDto update(UpdateCustomerDto updateDto) {
        Customer customer = customerService.getById(updateDto.getId())
                .orElseThrow(CustomerNotFoundException::new);

        customer.setName(updateDto.getName());
        customer.setFirstName(updateDto.getFirstName());

        return toDto(customerService.update(customer));
    }

    public void delete(UUID id) {
        measurementService.getByCustomerId(id).forEach(m -> {
            m.setCustomer(null);
            measurementService.update(m);
        });

        customerService.getById(id)
                .orElseThrow(CustomerNotFoundException::new);
        customerService.delete(id);
    }

    private CustomerResponseDto toDto(Customer customer) {
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setFirstName(customer.getFirstName());
        return dto;
    }
}
