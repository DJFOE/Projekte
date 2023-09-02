package eu.bsinfo.wip.rest.facade.integration;

import eu.bsinfo.wip.rest.entity.Customer;
import eu.bsinfo.wip.rest.exceptions.CustomerNotFoundException;
import eu.bsinfo.wip.rest.facade.CustomerFacade;
import eu.bsinfo.wip.rest.facade.MeasurementFacade;
import eu.bsinfo.wip.rest.facade.dto.customer.CreateCustomerDto;
import eu.bsinfo.wip.rest.facade.dto.customer.CustomerResponseDto;
import eu.bsinfo.wip.rest.facade.dto.customer.UpdateCustomerDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerFacadeIntegrationTest {

    @Autowired
    CustomerFacade customerFacade;
    @Autowired
    MeasurementFacade measurementFacade;

    List<Customer> testCustomers = Arrays.asList(new Customer(UUID.randomUUID(), "a", "b"), new Customer(UUID.randomUUID(),"testUser", "testFirstName"));

    @Test
    @Order(1)
    void testDeleteAll(){
        measurementFacade.getAll().forEach(m -> measurementFacade.delete(m.getId()));
        customerFacade.getAll().forEach(c -> customerFacade.delete(c.getId()));

        Assertions.assertEquals(0, customerFacade.getAll().size());
        Assertions.assertEquals(0, measurementFacade.getAll().size());
    }

    @Test
    @Order(2)
    @DisplayName("CustomerFacade#create gibt customer als response zurück")
    void testCreate(){
        testCustomers.forEach(c -> {
            CustomerResponseDto responseDto = customerFacade.create(new CreateCustomerDto(c.getName(), c.getFirstName()));
            Assertions.assertNotNull(responseDto);
        });
    }

    @Test
    @Order(3)
    @DisplayName("CustomerFacade#getAll gibt liste an customer responses zurück")
    void testGetAll(){
        List<CustomerResponseDto> customerResponseDtos = customerFacade.getAll();

        Assertions.assertNotNull(customerResponseDtos);
        Assertions.assertEquals(testCustomers.size(), customerResponseDtos.size());
    }

    @Test
    @Order(4)
    @DisplayName("CustomerFacade#getCustomerById gibt customer response zurück")
    void testGetById(){
        CustomerResponseDto expectedResponse = customerFacade.getAll().get(0);
        CustomerResponseDto responseDto = customerFacade.getById(expectedResponse.getId());

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(expectedResponse, responseDto);
    }

    @Test
    @Order(5)
    @DisplayName("CustomerFacade#update gibt customer als response zurück")
    void testUpdate(){
        CustomerResponseDto previousCustomer = customerFacade.getAll().get(0);
        String updatedName = "New";
        String updatedFirstName = "Name";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto(previousCustomer.getId(), updatedName, updatedFirstName);
        CustomerResponseDto expectedResponseDto = new CustomerResponseDto(previousCustomer.getId(), updatedName, updatedFirstName);
        CustomerResponseDto responseDto = customerFacade.update(updateCustomerDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertNotEquals(previousCustomer, responseDto);
        Assertions.assertEquals(expectedResponseDto, responseDto);
    }

    @Test
    @Order(6)
    @DisplayName("CustomerFacade#delete löscht customer")
    void testDelete(){
        CustomerResponseDto testResponse = customerFacade.getAll().get(0);
        customerFacade.delete(testResponse.getId());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerFacade.getById(testResponse.getId()));
    }

}
