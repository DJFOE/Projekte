package eu.bsinfo.wip.rest.controller;

import eu.bsinfo.wip.rest.facade.MeasurementFacade;
import eu.bsinfo.wip.rest.facade.dto.measurement.CreateMeasurementDto;
import eu.bsinfo.wip.rest.facade.dto.measurement.MeasurementResponseDto;
import eu.bsinfo.wip.rest.facade.dto.measurement.UpdateMeasurementDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping({"measurement", "ablesungen"})
public class MeasurementController {

    @Autowired
    public MeasurementFacade measurementFacade;

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all measurements", responses = {
            @ApiResponse(responseCode = "200", description = "List of measurements")
    })
    public List<MeasurementResponseDto> getAll(@RequestParam(value = "kunde") Optional<UUID> customerId,
                                               @RequestParam(value = "beginn") Optional<LocalDate> after,
                                               @RequestParam(value = "ende") Optional<LocalDate> before) {
        return measurementFacade.getAll().stream()
                .filter(m -> {
                    boolean customerOk = customerId.isEmpty() || Objects.equals(m.getCustomerId(), customerId.get());
                    boolean afterOk = after.isEmpty() || m.getDate().isEqual(after.get()) || m.getDate().isAfter(after.get());
                    boolean beforeOk = before.isEmpty() || m.getDate().isEqual(before.get()) || m.getDate().isBefore(before.get());
                    return customerOk && afterOk && beforeOk;
                })
                .toList();
    }

    @RequestMapping(method = GET, path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get one measurement", responses = {
            @ApiResponse(responseCode = "200", description = "Measurement"),
            @ApiResponse(responseCode = "400", description = "Invalid uuid format", content = @Content(examples = @ExampleObject("string"))),
            @ApiResponse(responseCode = "404", description = "Measurement not found", content = @Content(examples = @ExampleObject("string")))
    })
    public ResponseEntity<MeasurementResponseDto> getById(@PathVariable UUID id) {
        MeasurementResponseDto measurement = measurementFacade.getById(id);
        return ResponseEntity.ok(measurement);
    }


    @RequestMapping(method = GET, path = {"/customer/{customerId}", "/kunde/{customerId}"}, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get measurements by customer id", responses = {
            @ApiResponse(responseCode = "200", description = "List of measurements"),
            @ApiResponse(responseCode = "400", description = "Invalid uuid format", content = @Content(examples = @ExampleObject("string"))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(examples = @ExampleObject("string")))
    })
    public ResponseEntity<List<MeasurementResponseDto>> getMeasurementsByCustomerId(@PathVariable UUID customerId) {
        return ResponseEntity.ok(measurementFacade.getByCustomerId(customerId));
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates measurement", responses = {
            @ApiResponse(responseCode = "200", description = "Measurement created"),
            @ApiResponse(responseCode = "400", description = "Invalid measurement data", content = @Content(examples = @ExampleObject("string"))),
            @ApiResponse(responseCode = "404", description = "Measurement not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<MeasurementResponseDto> create(@Valid @RequestBody CreateMeasurementDto createDto) {
        MeasurementResponseDto saved = measurementFacade.create(createDto);
        // For 201 CREATED you would typically add the URI of the created entity to the
        // response via the 'Location' Header instead of returning the created object
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
    @Operation(summary = "Updates measurement", responses = {
            @ApiResponse(responseCode = "200", description = "Measurement updated"),
            @ApiResponse(responseCode = "400", description = "Invalid measurement data"),
            @ApiResponse(responseCode = "404", description = "Measurement not found")
    })
    public ResponseEntity<String> update(@RequestBody UpdateMeasurementDto updateDto) {
        measurementFacade.update(updateDto);
        return ResponseEntity.ok("Measurement updated");
    }

    @RequestMapping(method = DELETE, path = "/{id}", produces = TEXT_PLAIN_VALUE)
    @Operation(summary = "Deletes measurement", responses = {
            @ApiResponse(responseCode = "200", description = "Measurement deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid uuid format"),
            @ApiResponse(responseCode = "404", description = "Measurement not found")
    })
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        measurementFacade.delete(id);
        return ResponseEntity.ok("Measurement deleted");
    }
}
