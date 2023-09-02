package eu.bsinfo.wip.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bsinfo.wip.rest.entity.Customer;
import eu.bsinfo.wip.rest.exceptions.CustomerNotFoundException;
import eu.bsinfo.wip.rest.facade.CustomerFacade;
import eu.bsinfo.wip.rest.facade.dto.customer.CreateCustomerDto;
import eu.bsinfo.wip.rest.facade.dto.customer.CustomerResponseDto;
import eu.bsinfo.wip.rest.facade.dto.customer.UpdateCustomerDto;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private static final String ENDPOINT_PATH = "/customer";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerFacade customerFacade;

    private Customer mockCustomer = new Customer(UUID.randomUUID(), "Name", "Vorname");
    private CustomerResponseDto customerResponseDto = new CustomerResponseDto(mockCustomer.getId(), mockCustomer.getName(), mockCustomer.getFirstName());

    @Test
    @DisplayName("Einzelner Customer kann vom Server via GET empfangen werden")
    public void t05_getGetSingleCustomer() throws Exception {
        when(customerFacade.getById(mockCustomer.getId()))
                .thenReturn(customerResponseDto);

        MvcResult result = mockMvc.perform(get(ENDPOINT_PATH + "/" + mockCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CustomerResponseDto response = fromJson(result.getResponse().getContentAsString(), CustomerResponseDto.class);
        Assertions.assertEquals(customerResponseDto, response);

        verify(customerFacade, times(1)).getById(mockCustomer.getId());
        verifyNoMoreInteractions(customerFacade);
    }

    @Test
    @DisplayName("Wird ein nicht existierender Customer via GET angefordert, wird mit Not Found geantwortet")
    public void t06_getSingleCustomerFailsForNonExistingCustomer() throws Exception {
        when(customerFacade.getById(mockCustomer.getId()))
                .thenThrow(new CustomerNotFoundException());

        MvcResult result = mockMvc.perform(get(ENDPOINT_PATH + "/" + mockCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(customerFacade, times(1)).getById(mockCustomer.getId());
        verifyNoMoreInteractions(customerFacade);
    }

    @Test
    @DisplayName("Liste aller Customern vom Server enthält alle gesendeten Customern")
    void t20_getEveryCustomer() throws Exception {
        when(customerFacade.getAll())
                .thenReturn(Collections.singletonList(customerResponseDto));

        MvcResult result = mockMvc.perform(get(ENDPOINT_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CustomerResponseDto[] response = fromJson(result.getResponse().getContentAsString(), CustomerResponseDto[].class);
        Assertions.assertArrayEquals(new CustomerResponseDto[]{customerResponseDto}, response);

        verify(customerFacade, times(1)).getAll();
        verifyNoMoreInteractions(customerFacade);
    }

    @Test
    @DisplayName("Customer werden erfolgreich via POST an den Server gesendet")
    public void t01_createNewCustomer() throws Exception {
        CreateCustomerDto createCustomerDto = new CreateCustomerDto();
        createCustomerDto.setName("Name");
        createCustomerDto.setFirstName("Firstname");

        when(customerFacade.create(createCustomerDto))
                .thenReturn(customerResponseDto);

        MvcResult result = mockMvc.perform(post(ENDPOINT_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCustomerDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        CustomerResponseDto response = fromJson(result.getResponse().getContentAsString(), CustomerResponseDto.class);
        Assertions.assertEquals(customerResponseDto, response);

        verify(customerFacade, times(1)).create(createCustomerDto);
        verifyNoMoreInteractions(customerFacade);
    }

    @Test
    @DisplayName("Senden eines nicht gültigen Customern-Objekts via POST führt zu Bad-Request")
    void t02_createNewCustomerFailsIfCustomerIsInvalid() throws Exception {
        CreateCustomerDto createCustomerDto = new CreateCustomerDto();
        createCustomerDto.setName(" ");
        createCustomerDto.setFirstName("Firstname");

        when(customerFacade.create(createCustomerDto))
                .thenReturn(customerResponseDto);

        MvcResult result = mockMvc.perform(post(ENDPOINT_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createCustomerDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(customerFacade);
    }

    @Test
    @DisplayName("Customer werden erfolgreich via PUT an den Server gesendet")
    public void t03_updateExistingCustomer() throws Exception {
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto();
        updateCustomerDto.setId(mockCustomer.getId());
        updateCustomerDto.setName(mockCustomer.getName());
        updateCustomerDto.setFirstName(mockCustomer.getFirstName());

        when(customerFacade.update(updateCustomerDto))
                .thenReturn(customerResponseDto);

        MvcResult result = mockMvc.perform(put(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateCustomerDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Assertions.assertFalse(Strings.isBlank(response));

        verify(customerFacade, times(1)).update(updateCustomerDto);
        verifyNoMoreInteractions(customerFacade);
    }

    @Test
    @DisplayName("Senden eines nicht bestehenden Customern-Objekts via PUT führt zu Not-Found")
    public void t04_updateNonExistingCustomerFails() throws Exception {
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto();
        updateCustomerDto.setId(mockCustomer.getId());
        updateCustomerDto.setName(mockCustomer.getName());
        updateCustomerDto.setFirstName(mockCustomer.getFirstName());

        when(customerFacade.update(updateCustomerDto))
                .thenThrow(new CustomerNotFoundException());

        MvcResult result = mockMvc.perform(put(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateCustomerDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(customerFacade, times(1)).update(updateCustomerDto);
        verifyNoMoreInteractions(customerFacade);
    }

    @Test
    @DisplayName("Ein bestehender Customer kann erfolgreich gelöscht werden")
    void t17_deleteCustomer() throws Exception {
        MvcResult result = mockMvc.perform(delete(ENDPOINT_PATH + "/" + mockCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(customerFacade, times(1)).delete(mockCustomer.getId());
        verifyNoMoreInteractions(customerFacade);
    }

    @Test
    @DisplayName("Löschen eines nicht existierenden Customern führt zu Not-Found")
    void t18_deleteCustomerFailsForNonExistingCustomer() throws Exception {
        doThrow(new CustomerNotFoundException()).when(customerFacade).delete(mockCustomer.getId());

        MvcResult result = mockMvc.perform(delete(ENDPOINT_PATH + "/" + mockCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(customerFacade, times(1)).delete(mockCustomer.getId());
        verifyNoMoreInteractions(customerFacade);
    }

    private String toJson(Object value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    }

    private <T> T fromJson(String json, Class<T> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.reader().readValue(json, type);
    }
}
