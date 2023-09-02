package eu.bsinfo.wip.rest.facade.integration;


import eu.bsinfo.wip.rest.entity.Customer;
import eu.bsinfo.wip.rest.entity.Measurement;
import eu.bsinfo.wip.rest.exceptions.MeasurementNotFoundException;
import eu.bsinfo.wip.rest.facade.CustomerFacade;
import eu.bsinfo.wip.rest.facade.MeasurementFacade;
import eu.bsinfo.wip.rest.facade.dto.customer.CreateCustomerDto;
import eu.bsinfo.wip.rest.facade.dto.customer.CustomerResponseDto;
import eu.bsinfo.wip.rest.facade.dto.measurement.CreateMeasurementDto;
import eu.bsinfo.wip.rest.facade.dto.measurement.MeasurementResponseDto;
import eu.bsinfo.wip.rest.facade.dto.measurement.UpdateMeasurementDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeasurementFacadeIntegrationTest {

    @Autowired
    CustomerFacade customerFacade;
    @Autowired
    MeasurementFacade measurementFacade;

    Customer customer = new Customer( "test", "a");
    List<Measurement> testMeasurements = Arrays.asList(
            new Measurement(UUID.randomUUID(),  "Test", LocalDate.of(2, 2, 2), customer, "test", true, 394735, Measurement.CounterType.POWER),
            new Measurement(UUID.randomUUID(),  "A2390587", LocalDate.of(4, 2, 1), customer, "comment", false, 3459453, Measurement.CounterType.GAS));

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
    @DisplayName("MeasurementFacade#create gibt measurement als response zurück")
    void testCreate(){
        CustomerResponseDto customerResponseDto = customerFacade.create(new CreateCustomerDto(customer.getName(), customer.getFirstName()));
        this.customer.setId(customerResponseDto.getId());
        testMeasurements.forEach(c -> {
            MeasurementResponseDto expectedResponseDto = new MeasurementResponseDto(null, c.getCounterId(), c.getDate(), c.getCustomer().getId(), c.getComment(), c.isNewEntry(), c.getCounterValue(), c.getCounterType());
            MeasurementResponseDto responseDto = measurementFacade.create(new CreateMeasurementDto(c.getCounterId(), c.getDate(), c.getCustomer().getId(), c.getComment(), c.isNewEntry(), c.getCounterValue(), c.getCounterType()));
            expectedResponseDto.setId(responseDto.getId());

            Assertions.assertNotNull(responseDto);
            Assertions.assertEquals(expectedResponseDto, responseDto);
        });
    }

    @Test
    @Order(3)
    @DisplayName("MeasurementFacade#getAll gibt liste an measurement responses zurück")
    void testGetAll(){
        List<MeasurementResponseDto> measurementResponseDtos = measurementFacade.getAll();

        Assertions.assertNotNull(measurementResponseDtos);
        Assertions.assertEquals(testMeasurements.size(), measurementResponseDtos.size());
    }

    @Test
    @Order(4)
    @DisplayName("MeasurementFacade#getMeasurementById gibt measurement response zurück")
    void testGetById(){
        MeasurementResponseDto expectedResponse = measurementFacade.getAll().get(0);
        MeasurementResponseDto responseDto = measurementFacade.getById(expectedResponse.getId());

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(expectedResponse, responseDto);
    }

    @Test
    @Order(5)
    @DisplayName("MeasurementFacade#getMeasurementByCustomerId gibt liste an measurement responses zurück")
    void testGetByCustomerId(){
        List<MeasurementResponseDto> expectedResponses = measurementFacade.getAll();
        List<MeasurementResponseDto> responseDtoList = measurementFacade.getByCustomerId(measurementFacade.getAll().get(0).getCustomerId());

        Assertions.assertNotNull(responseDtoList);
        Assertions.assertEquals(expectedResponses.size(), responseDtoList.size());
        Assertions.assertEquals(expectedResponses, responseDtoList);
    }

    @Test
    @Order(6)
    @DisplayName("MeasurementFacade#update gibt measurement als response zurück")
    void testUpdate(){
        CustomerResponseDto customer = customerFacade.getAll().get(0);
        MeasurementResponseDto previousMeasurement = measurementFacade.getAll().get(0);
        String updatedCounterId = "NewID02";
        LocalDate updatedDate = LocalDate.of(35, 2, 15);
        String updatedComment = "new Comment";
        //"A2390587", LocalDate.of(4, 2, 1), customer, "comment", false, 3459453, Measurement.CounterType.GAS));
        UpdateMeasurementDto updateMeasurementDto = new UpdateMeasurementDto(previousMeasurement.getId(), updatedCounterId, updatedDate, customer.getId(), updatedComment, previousMeasurement.isNewEntry(), previousMeasurement.getCounterValue(), previousMeasurement.getCounterType());
        MeasurementResponseDto expectedResponseDto = new MeasurementResponseDto(previousMeasurement.getId(), updatedCounterId, updatedDate, customer.getId(), updatedComment, previousMeasurement.isNewEntry(), previousMeasurement.getCounterValue(), previousMeasurement.getCounterType());
        MeasurementResponseDto responseDto = measurementFacade.update(updateMeasurementDto);

        Assertions.assertNotNull(responseDto);
        Assertions.assertNotEquals(previousMeasurement, responseDto);
        Assertions.assertEquals(expectedResponseDto, responseDto);
    }

    @Test
    @Order(7)
    @DisplayName("MeasurementFacade#delete löscht measurement")
    void testDelete(){
        MeasurementResponseDto testResponse = measurementFacade.getAll().get(0);
        measurementFacade.delete(testResponse.getId());

        Assertions.assertThrows(MeasurementNotFoundException.class, () -> measurementFacade.getById(testResponse.getId()));
    }

}
