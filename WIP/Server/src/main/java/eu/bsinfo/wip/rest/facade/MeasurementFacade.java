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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Transactional
public class MeasurementFacade {

    @Autowired
    public MeasurementService measurementService;

    @Autowired
    public CustomerService customerService;

    public MeasurementResponseDto getById(UUID id) {
        return toDto(measurementService.getById(id)
                .orElseThrow(MeasurementNotFoundException::new));
    }

    public List<MeasurementResponseDto> getByCustomerId(UUID id) {
        return measurementService.getByCustomerId(id).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public List<MeasurementResponseDto> getAll() {
        return measurementService.getAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public MeasurementResponseDto create(CreateMeasurementDto createDto) {
        Measurement newMeasurement = new Measurement();

        Customer customer = customerService.getById(createDto.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);

        newMeasurement.setCustomer(customer);
        newMeasurement.setNewEntry(createDto.isNewEntry());
        newMeasurement.setCounterId(createDto.getCounterId());
        newMeasurement.setCounterValue(createDto.getCounterValue());
        newMeasurement.setDate(createDto.getDate());
        newMeasurement.setComment(createDto.getComment());
        newMeasurement.setCounterType(createDto.getCounterType());

        return toDto(measurementService.create(newMeasurement));
    }

    public MeasurementResponseDto update(UpdateMeasurementDto updateDto) {
        Measurement measurement = measurementService.getById(updateDto.getId())
                .orElseThrow(MeasurementNotFoundException::new);

        Customer customer = customerService.getById(updateDto.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);

        measurement.setCustomer(customer);
        measurement.setNewEntry(updateDto.isNewEntry());
        measurement.setCounterId(updateDto.getCounterId());
        measurement.setCounterValue(updateDto.getCounterValue());
        measurement.setDate(updateDto.getDate());
        measurement.setComment(updateDto.getComment());
        measurement.setCounterType(updateDto.getCounterType());

        return toDto(measurementService.update(measurement));
    }

    public void delete(UUID id) {
        measurementService.getById(id)
                .orElseThrow(MeasurementNotFoundException::new);
        measurementService.delete(id);
    }

    private MeasurementResponseDto toDto(Measurement measurement) {
        MeasurementResponseDto dto = new MeasurementResponseDto();
        dto.setId(measurement.getId());
        dto.setCustomerId(measurement.getCustomer() != null ? measurement.getCustomer().getId() : null);
        dto.setNewEntry(measurement.isNewEntry());
        dto.setCounterId(measurement.getCounterId());
        dto.setCounterValue(measurement.getCounterValue());
        dto.setDate(measurement.getDate());
        dto.setComment(measurement.getComment());
        dto.setCounterType(measurement.getCounterType());
        return dto;
    }
}
