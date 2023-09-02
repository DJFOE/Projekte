package eu.bsinfo.wip.rest.controller;

import eu.bsinfo.wip.rest.facade.CustomerFacade;
import eu.bsinfo.wip.rest.facade.dto.customer.CreateCustomerDto;
import eu.bsinfo.wip.rest.facade.dto.customer.CustomerResponseDto;
import eu.bsinfo.wip.rest.facade.dto.customer.UpdateCustomerDto;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping({"customer", "kunden"})
public class CustomerController {

    @Autowired
    public CustomerFacade customerFacade;

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all customers", responses = {
            @ApiResponse(responseCode = "200", description = "List of customers")
    })
    public ResponseEntity<List<CustomerResponseDto>> getAll() {
        return ResponseEntity.ok(customerFacade.getAll());
    }

    @RequestMapping(method = GET, path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get one customer", responses = {
            @ApiResponse(responseCode = "200", description = "Customer"),
            @ApiResponse(responseCode = "400", description = "Invalid uuid format", content = @Content(examples = @ExampleObject("string"))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(examples = @ExampleObject("string")))
    })
    public ResponseEntity<CustomerResponseDto> getById(@PathVariable UUID id) {
        CustomerResponseDto found = customerFacade.getById(id);
        return ResponseEntity.ok(found);
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates customer", responses = {
            @ApiResponse(responseCode = "200", description = "Customer created"),
            @ApiResponse(responseCode = "400", description = "Invalid customer data", content = @Content(examples = @ExampleObject("string"))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<CustomerResponseDto> create(@Valid @RequestBody CreateCustomerDto createDto) {
        CustomerResponseDto saved = customerFacade.create(createDto);
        // For 201 CREATED you would typically add the URI of the created entity to the
        // response via the 'Location' header instead of returning the created object
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
    @Operation(summary = "Updates customer", responses = {
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "400", description = "Invalid customer data"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<String> update(@Valid @RequestBody UpdateCustomerDto updateDto) {
        customerFacade.update(updateDto);
        return ResponseEntity.ok("Customer updated");
    }

    @RequestMapping(method = DELETE, path = "/{id}", produces = TEXT_PLAIN_VALUE)
    @Operation(summary = "Deletes customer", responses = {
            @ApiResponse(responseCode = "200", description = "Customer deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid uuid format"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        customerFacade.delete(id);
        return ResponseEntity.ok("Customer deleted");
    }
}
