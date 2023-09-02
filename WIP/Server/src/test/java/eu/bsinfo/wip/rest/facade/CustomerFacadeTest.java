package eu.bsinfo.wip.rest.facade;

import eu.bsinfo.wip.rest.entity.Customer;
import eu.bsinfo.wip.rest.entity.Measurement;
import eu.bsinfo.wip.rest.exceptions.CustomerNotFoundException;
import eu.bsinfo.wip.rest.facade.dto.customer.CreateCustomerDto;
import eu.bsinfo.wip.rest.facade.dto.customer.CustomerResponseDto;
import eu.bsinfo.wip.rest.facade.dto.customer.UpdateCustomerDto;
import eu.bsinfo.wip.rest.service.CustomerService;
import eu.bsinfo.wip.rest.service.MeasurementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerFacadeTest {

    @InjectMocks
    CustomerFacade customerFacade;

    @Mock
    CustomerService customerService;

    @Mock
    MeasurementService measurementService;

    private Customer mockCustomer = new Customer(UUID.randomUUID(), "Name", "Vorname");

    private CustomerResponseDto customerResponseDto = new CustomerResponseDto(mockCustomer.getId(), mockCustomer.getName(), mockCustomer.getFirstName());

    @Test
    @DisplayName("CustomerFacade#getCustomerById gibt customer response zurück")
    void testGetById() {
        when(customerService.getById(mockCustomer.getId()))
                .thenReturn(Optional.of(mockCustomer));

        CustomerResponseDto response = customerFacade.getById(mockCustomer.getId());

        Assertions.assertEquals(customerResponseDto, response);

        verify(customerService, times(1)).getById(mockCustomer.getId());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("CustomerFacade#getCustomerById wirft CustomerNotFoundException falls Customer nicht gefunden wurde")
    void testGetByIdFailsForNonExistingCustomer() {
        when(customerService.getById(mockCustomer.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerFacade.getById(mockCustomer.getId()));

        verify(customerService, times(1)).getById(mockCustomer.getId());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("CustomerFacade#getAll gibt liste an customer responses zurück")
    void testGetAll() {
        when(customerService.getAll())
                .thenReturn(Arrays.asList(mockCustomer));

        List<CustomerResponseDto> response = customerFacade.getAll();

        Assertions.assertEquals(Arrays.asList(customerResponseDto), response);

        verify(customerService, times(1)).getAll();
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("CustomerFacade#create gibt customer als response zurück")
    void testCreate() {
        CreateCustomerDto createCustomerDto = new CreateCustomerDto();
        createCustomerDto.setName(mockCustomer.getName());
        createCustomerDto.setFirstName(mockCustomer.getName());

        when(customerService.create(any(Customer.class)))
                .thenReturn(mockCustomer);

        CustomerResponseDto response = customerFacade.create(createCustomerDto);

        Assertions.assertEquals(customerResponseDto, response);

        verify(customerService, times(1)).create(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("CustomerFacade#update gibt customer als response zurück")
    void testUpdate() {
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto();
        updateCustomerDto.setId(mockCustomer.getId());
        updateCustomerDto.setName("New name");
        updateCustomerDto.setFirstName("New firstname");

        when(customerService.getById(mockCustomer.getId()))
                .thenReturn(Optional.of(mockCustomer));
        when(customerService.update(any(Customer.class))).thenAnswer(i -> {
            Customer arg = i.getArgument(0);
            Assertions.assertEquals(updateCustomerDto.getId(), arg.getId());
            Assertions.assertEquals(updateCustomerDto.getName(), arg.getName(), "CustomerFacade#update sollte Customer.name ändern");
            Assertions.assertEquals(updateCustomerDto.getFirstName(), arg.getFirstName(), "CustomerFacade#update sollte Customer.firstName ändern");
            return arg;
        });

        CustomerResponseDto response = customerFacade.update(updateCustomerDto);

        Assertions.assertEquals(updateCustomerDto.getId(), response.getId());
        Assertions.assertEquals(updateCustomerDto.getName(), response.getName());
        Assertions.assertEquals(updateCustomerDto.getId(), response.getId());

        verify(customerService, times(1)).getById(mockCustomer.getId());
        verify(customerService, times(1)).update(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("CustomerFacade#update wirft CustomerNotFoundException falls Customer nicht gefunden wurde")
    void testUpdateFailsForNonExistingCustomer() {
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto();
        updateCustomerDto.setId(mockCustomer.getId());
        updateCustomerDto.setName("New name");
        updateCustomerDto.setFirstName("New firstname");

        when(customerService.getById(mockCustomer.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerFacade.update(updateCustomerDto));

        verify(customerService, times(1)).getById(mockCustomer.getId());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("CustomerFacade#delete löscht customer und setzt dessen measurements auf null")
    void testDelete() {
        when(customerService.getById(mockCustomer.getId()))
                .thenReturn(Optional.of(mockCustomer));

        var mockMeasurment = mock(Measurement.class);
        when(measurementService.getByCustomerId(mockCustomer.getId()))
                .thenReturn(Collections.singletonList(mockMeasurment));

        customerFacade.delete(mockCustomer.getId());

        verify(mockMeasurment, times(1)).setCustomer(null);
        verify(measurementService, times(1)).update(mockMeasurment);
        verify(customerService, times(1)).delete(mockCustomer.getId());
    }

    @Test
    @DisplayName("CustomerFacade#delete wirft CustomerNotFoundException falls Customer nicht gefunden wurde")
    void testDeleteFailsForNonExistingCustomer() {
        when(customerService.getById(mockCustomer.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerFacade.delete(mockCustomer.getId()));

        verify(customerService, times(1)).getById(mockCustomer.getId());
        verifyNoMoreInteractions(customerService);
    }
}
