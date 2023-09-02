package eu.bsinfo.wip.rest.facade;


import eu.bsinfo.wip.rest.entity.Customer;
import eu.bsinfo.wip.rest.entity.Measurement;
import eu.bsinfo.wip.rest.exceptions.CustomerNotFoundException;
import eu.bsinfo.wip.rest.exceptions.MeasurementNotFoundException;
import eu.bsinfo.wip.rest.facade.dto.measurement.CreateMeasurementDto;
import eu.bsinfo.wip.rest.facade.dto.measurement.MeasurementResponseDto;
import eu.bsinfo.wip.rest.facade.dto.measurement.UpdateMeasurementDto;
import eu.bsinfo.wip.rest.service.CustomerService;
import eu.bsinfo.wip.rest.service.MeasurementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeasurementFacadeTest {

    @InjectMocks
    MeasurementFacade measurementFacade;

    @Mock
    MeasurementService measurementService;
    @Mock
    CustomerService customerService;
    UUID uuid = UUID.randomUUID();
    Customer customer = new Customer(uuid, "Doe", "John");
    private Measurement mockMeasurement = new Measurement("123", LocalDate.of(2, 2, 2), customer, "fdshjl", true, 144, Measurement.CounterType.WATER);
    private Customer mockCustomer = new Customer(uuid, "Doe", "John");

    private MeasurementResponseDto measurementResponseDto = new MeasurementResponseDto(mockMeasurement.getId(), mockMeasurement.getCounterId(), mockMeasurement.getDate(), mockCustomer.getId(), mockMeasurement.getComment(), mockMeasurement.isNewEntry(), mockMeasurement.getCounterValue(), mockMeasurement.getCounterType());

    @Test
    @DisplayName("MeasurementFacade#getMeasurementById gibt measurement response zurück")
    void testGetById() {
        when(measurementService.getById(mockMeasurement.getId()))
                .thenReturn(Optional.of(mockMeasurement));

        MeasurementResponseDto response = measurementFacade.getById(mockMeasurement.getId());

        Assertions.assertEquals(measurementResponseDto, response);

        verify(measurementService, times(1)).getById(mockMeasurement.getId());
        verifyNoMoreInteractions(measurementService);
    }

    @Test
    @DisplayName("MeasurementFacade#getMeasurementById wirft MeasurementNotFoundException falls Measurement nicht gefunden wurde")
    void testGetByIdFailsForNonExistingMeasurement() {
        when(measurementService.getById(mockMeasurement.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(MeasurementNotFoundException.class, () -> measurementFacade.getById(mockMeasurement.getId()));

        verify(measurementService, times(1)).getById(mockMeasurement.getId());
        verifyNoMoreInteractions(measurementService);
    }

    @Test
    @DisplayName("MeasurementFacade#getAll gibt liste an measurement responses zurück")
    void testGetAll() {
        when(measurementService.getAll())
                .thenReturn(Arrays.asList(mockMeasurement));

        List<MeasurementResponseDto> response = measurementFacade.getAll();

        Assertions.assertEquals(Arrays.asList(measurementResponseDto), response);

        verify(measurementService, times(1)).getAll();
        verifyNoMoreInteractions(measurementService);
    }

    @Test
    @DisplayName("MeasurementFacade#create gibt measurement als response zurück")
    void testCreate() {

        CreateMeasurementDto createMeasurementDto = new CreateMeasurementDto();
        createMeasurementDto.setCounterId(mockMeasurement.getCounterId());
        createMeasurementDto.setDate(mockMeasurement.getDate());
        createMeasurementDto.setCustomerId(mockMeasurement.getCustomer().getId());
        createMeasurementDto.setComment(mockMeasurement.getComment());
        createMeasurementDto.setNewEntry(mockMeasurement.isNewEntry());
        createMeasurementDto.setCounterValue(mockMeasurement.getCounterValue());

        when(measurementService.create(any(Measurement.class)))
                .thenReturn(mockMeasurement);
        when(customerService.getById(mockCustomer.getId()))
                .thenReturn(Optional.of(mockCustomer));

        MeasurementResponseDto response = measurementFacade.create(createMeasurementDto);

        Assertions.assertEquals(measurementResponseDto, response);

        verify(measurementService, times(1)).create(any(Measurement.class));
        verifyNoMoreInteractions(measurementService);
    }

    @Test
    @DisplayName("MeasurementFacade#create wirft CustomerNotFoundException falls Customer nicht gefunden wurde")
    void testCreateWithNotExistingCustomer() {
        CreateMeasurementDto createMeasurementDto = new CreateMeasurementDto();
        createMeasurementDto.setCounterId(mockMeasurement.getCounterId());
        createMeasurementDto.setDate(mockMeasurement.getDate());
        createMeasurementDto.setCustomerId(UUID.randomUUID());
        createMeasurementDto.setComment(mockMeasurement.getComment());
        createMeasurementDto.setNewEntry(mockMeasurement.isNewEntry());
        createMeasurementDto.setCounterValue(mockMeasurement.getCounterValue());

        when(customerService.getById(createMeasurementDto.getCustomerId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(CustomerNotFoundException.class, () -> measurementFacade.create(createMeasurementDto));

        verify(customerService, times(1)).getById(createMeasurementDto.getCustomerId());
        verifyNoMoreInteractions(customerService);

    }

    @Test
    @DisplayName("MeasurementFacade#update gibt measurement als response zurück")
    void testUpdate() {
        UpdateMeasurementDto updateMeasurementDto = new UpdateMeasurementDto();
        updateMeasurementDto.setId(mockMeasurement.getId());
        updateMeasurementDto.setCustomerId(mockCustomer.getId());
        updateMeasurementDto.setNewEntry(true);
        updateMeasurementDto.setCounterId("144");
        updateMeasurementDto.setCounterValue(123);
        updateMeasurementDto.setDate(LocalDate.of(2,2,2));
        updateMeasurementDto.setComment("fsjdklfs");

        when(measurementService.getById(mockMeasurement.getId()))
                .thenReturn(Optional.of(mockMeasurement));
        when(customerService.getById(mockCustomer.getId()))
                .thenReturn(Optional.of(mockCustomer));
        when(measurementService.update(any(Measurement.class))).thenAnswer(i -> {
            Measurement arg = i.getArgument(0);
            Assertions.assertEquals(updateMeasurementDto.getId(), arg.getId());
            Assertions.assertEquals(updateMeasurementDto.getCustomerId(), arg.getCustomer().getId());
            Assertions.assertEquals(updateMeasurementDto.isNewEntry(), arg.isNewEntry(), "MeasurementFacade#update sollte Measurement.newEntry ändern");
            Assertions.assertEquals(updateMeasurementDto.getCounterId(), arg.getCounterId(), "MeasurementFacade#update sollte Measurement.counterId ändern");
            Assertions.assertEquals(updateMeasurementDto.getCounterValue(), arg.getCounterValue(),"MeasurementFacade#update sollte Measurement.counterValue ändern");
            Assertions.assertEquals(updateMeasurementDto.getDate(), arg.getDate(),"MeasurementFacade#update sollte Measurement.date ändern");
            Assertions.assertEquals(updateMeasurementDto.getComment(), arg.getComment(),"MeasurementFacade#update sollte Measurement.comment ändern");
            return arg;
        });

        MeasurementResponseDto response = measurementFacade.update(updateMeasurementDto);

        Assertions.assertEquals(updateMeasurementDto.getId(), response.getId());
        Assertions.assertEquals(updateMeasurementDto.isNewEntry(), response.isNewEntry());
        Assertions.assertEquals(updateMeasurementDto.getCounterId(), response.getCounterId());
        Assertions.assertEquals(updateMeasurementDto.getCounterValue(), response.getCounterValue());
        Assertions.assertEquals(updateMeasurementDto.getDate(), response.getDate());
        Assertions.assertEquals(updateMeasurementDto.getComment(), response.getComment());


        verify(measurementService, times(1)).getById(mockMeasurement.getId());
        verify(measurementService, times(1)).update(any(Measurement.class));
        verifyNoMoreInteractions(measurementService);
    }

    @Test
    @DisplayName("MeasurementFacade#update wirft MeasurementNotFoundException falls Measurement nicht gefunden wurde")
    void testUpdateFailsForNonExistingMeasurement() {
        UpdateMeasurementDto updateMeasurementDto = new UpdateMeasurementDto();
        updateMeasurementDto.setId(mockMeasurement.getId());
        updateMeasurementDto.setCustomerId(mockCustomer.getId());
        updateMeasurementDto.setNewEntry(true);
        updateMeasurementDto.setCounterId("567");
        updateMeasurementDto.setCounterValue(321);
        updateMeasurementDto.setDate(LocalDate.of(6,6,6));
        updateMeasurementDto.setComment("fsjdklfs");

        when(measurementService.getById(mockMeasurement.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(MeasurementNotFoundException.class, () -> measurementFacade.update(updateMeasurementDto));

        verify(measurementService, times(1)).getById(mockMeasurement.getId());
        verifyNoMoreInteractions(measurementService);
    }

    @Test
    @DisplayName("MeasurementFacade#delete löscht measurement")
    void testDelete() {
        when(measurementService.getById(mockMeasurement.getId()))
                .thenReturn(Optional.of(mockMeasurement));

        measurementFacade.delete(mockMeasurement.getId());

        verify(measurementService, times(1)).delete(mockMeasurement.getId());
    }

    @Test
    @DisplayName("MeasurementFacade#delete wirft MeasurementNotFoundException falls Measurement nicht gefunden wurde")
    void testDeleteFailsForNonExistingCustomer() {
        when(measurementService.getById(mockMeasurement.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(MeasurementNotFoundException.class, () -> measurementFacade.delete(mockMeasurement.getId()));

        verify(measurementService, times(1)).getById(mockMeasurement.getId());
        verifyNoMoreInteractions(measurementService);
    }
}
