package eu.bsinfo.wip.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bsinfo.wip.rest.entity.Customer;
import eu.bsinfo.wip.rest.entity.Measurement;
import eu.bsinfo.wip.rest.exceptions.CustomerNotFoundException;
import eu.bsinfo.wip.rest.exceptions.MeasurementNotFoundException;
import eu.bsinfo.wip.rest.facade.MeasurementFacade;
import eu.bsinfo.wip.rest.facade.dto.measurement.CreateMeasurementDto;
import eu.bsinfo.wip.rest.facade.dto.measurement.MeasurementResponseDto;
import eu.bsinfo.wip.rest.facade.dto.measurement.UpdateMeasurementDto;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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

@WebMvcTest(MeasurementController.class)
public class MeasurementControllerTest {

    private static final String ENDPOINT_PATH = "/measurement";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeasurementFacade measurementFacade;

    private Customer mockCustomer = new Customer(UUID.randomUUID(), "Name", "Vorname");
    private Measurement mockMeasurement = new Measurement(UUID.randomUUID(), "H123498", LocalDate.now(), mockCustomer, "Test comment", false, 23849, Measurement.CounterType.WATER);
    private MeasurementResponseDto measurementResponseDto = new MeasurementResponseDto(mockMeasurement.getId(), mockMeasurement.getCounterId(), mockMeasurement.getDate(), mockCustomer.getId(), mockMeasurement.getComment(), mockMeasurement.isNewEntry(), mockMeasurement.getCounterValue(), mockMeasurement.getCounterType());

    @Test
    @DisplayName("Einzelne Measurement kann via GET vom Server empfangen werde")
    void t12_getSingleMeasurement() throws Exception {
        when(measurementFacade.getById(mockMeasurement.getId()))
                .thenReturn(measurementResponseDto);

        MvcResult result = mockMvc.perform(get(ENDPOINT_PATH + "/" + mockMeasurement.getId())
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MeasurementResponseDto response = fromJson(result.getResponse().getContentAsString(), MeasurementResponseDto.class);
        Assertions.assertEquals(measurementResponseDto, response);

        verify(measurementFacade, times(1)).getById(measurementResponseDto.getId());
        verifyNoMoreInteractions(measurementFacade);

    }

    @Test
    @DisplayName("Messungen können nach Kunden ID gesucht werden")
    void test_getByCustomerId() throws Exception {
        when(measurementFacade.getByCustomerId(mockCustomer.getId()))
                .thenReturn(List.of(measurementResponseDto));

        MvcResult result = mockMvc.perform(get(ENDPOINT_PATH + "/customer/" + mockCustomer.getId())
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MeasurementResponseDto[] response = fromJson(result.getResponse().getContentAsString(), MeasurementResponseDto[].class);
        Assertions.assertArrayEquals(new MeasurementResponseDto[]{measurementResponseDto}, response);

        verify(measurementFacade, times(1)).getByCustomerId(mockCustomer.getId());
        verifyNoMoreInteractions(measurementFacade);
    }


    @Test
    @DisplayName("Anfordern einer nicht existierenden Measurement via GET führt zu Not Found")
    void t13_getSingleMeasurementFailsForNonExistingMeasurement() throws Exception {
        when(measurementFacade.getById(mockMeasurement.getId()))
                .thenThrow(new MeasurementNotFoundException());

        MvcResult result = mockMvc.perform(get(ENDPOINT_PATH + "/" + mockMeasurement.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(measurementFacade, times(1)).getById(mockMeasurement.getId());
        verifyNoMoreInteractions(measurementFacade);
    }

    @Test
    @DisplayName("Liste aller Measurements vom Server enthält alle gesendeten Measurements")
    void t26_getEveryMeasurement() throws Exception {
        when(measurementFacade.getAll())
                .thenReturn(Collections.singletonList(measurementResponseDto));

        MvcResult result = mockMvc.perform(get(ENDPOINT_PATH)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MeasurementResponseDto[] response = fromJson(result.getResponse().getContentAsString(), MeasurementResponseDto[].class);
        Assertions.assertArrayEquals(new MeasurementResponseDto[]{measurementResponseDto}, response);

        verify(measurementFacade, times(1)).getAll();
        verifyNoMoreInteractions(measurementFacade);
    }

    @Test
    @DisplayName("Measurements können erfolgreich via POST an den Server gesendet werden")
    void t07_createMeasurementForCustomer() throws Exception {
        CreateMeasurementDto createMeasurementDto = new CreateMeasurementDto();
        createMeasurementDto.setCounterId("G5849");
        createMeasurementDto.setDate(LocalDate.now());
        createMeasurementDto.setCustomerId(mockCustomer.getId());
        createMeasurementDto.setComment("Test");
        createMeasurementDto.setNewEntry(true);
        createMeasurementDto.setCounterValue(398434);
        createMeasurementDto.setCounterType(mockMeasurement.getCounterType());


        when(measurementFacade.create(createMeasurementDto))
                .thenReturn(measurementResponseDto);

        MvcResult result = mockMvc.perform(post(ENDPOINT_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createMeasurementDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        MeasurementResponseDto response = fromJson(result.getResponse().getContentAsString(), MeasurementResponseDto.class);
        Assertions.assertEquals(measurementResponseDto, response);

        verify(measurementFacade, times(1)).create(createMeasurementDto);
        verifyNoMoreInteractions(measurementFacade);
    }

    @Test
    @DisplayName("Senden einer Measurement mit einem nicht existierenden Customer via POST führt zu Not Found")
    void t08_createMeasurementForNonExistingCustomerFails() throws Exception {
        CreateMeasurementDto createMeasurementDto = new CreateMeasurementDto();
        createMeasurementDto.setCounterId("G5849");
        createMeasurementDto.setDate(LocalDate.now());
        createMeasurementDto.setCustomerId(UUID.randomUUID());
        createMeasurementDto.setComment("Test");
        createMeasurementDto.setNewEntry(true);
        createMeasurementDto.setCounterValue(398434);
        createMeasurementDto.setCounterType(Measurement.CounterType.WATER);


        when(measurementFacade.create(createMeasurementDto))
                .thenThrow(new CustomerNotFoundException());

        MvcResult result = mockMvc.perform(post(ENDPOINT_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createMeasurementDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(measurementFacade, times(1)).create(createMeasurementDto);
        verifyNoMoreInteractions(measurementFacade);
    }

    @Test
    @DisplayName("Senden eines nicht gültigen Measurementobjekt führt zu Bad Request")
    void t09_createMeasurementFailsIfMeasurementIsNull() throws Exception {
        CreateMeasurementDto createMeasurementDto = null;
        when(measurementFacade.create(createMeasurementDto))
                .thenReturn(measurementResponseDto);

        MvcResult result = mockMvc.perform(post(ENDPOINT_PATH)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(toJson(createMeasurementDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(measurementFacade);
    }

    @Test
    @DisplayName("Measurement kann erfolgreich via PUT aktualisiert werden")
    void t10_updateExistingMeasurement() throws Exception {
        UpdateMeasurementDto updateMeasurementDto = new UpdateMeasurementDto();
        updateMeasurementDto.setCounterId(mockMeasurement.getCounterId());
        updateMeasurementDto.setDate(mockMeasurement.getDate());
        updateMeasurementDto.setCustomerId(mockMeasurement.getCustomer().getId());
        updateMeasurementDto.setComment(mockMeasurement.getComment());
        updateMeasurementDto.setNewEntry(mockMeasurement.isNewEntry());
        updateMeasurementDto.setCounterValue(3895);
        updateMeasurementDto.setCounterType(mockMeasurement.getCounterType());

        when(measurementFacade.update(updateMeasurementDto))
                .thenReturn(measurementResponseDto);

        MvcResult result = mockMvc.perform(put(ENDPOINT_PATH)
                        .accept(MediaType.ALL_VALUE)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(toJson(updateMeasurementDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Assertions.assertFalse(Strings.isBlank(response));

        verify(measurementFacade, times(1)).update(updateMeasurementDto);
        verifyNoMoreInteractions(measurementFacade);
    }

    @Test
    @DisplayName("Senden einer nicht existierenden Measurement via PUT führt zu Not Found")
    void t11_updateNonExistingMeasurementFails() throws Exception {
        UpdateMeasurementDto updateMeasurementDto = new UpdateMeasurementDto();
        updateMeasurementDto.setId(mockMeasurement.getId());
        updateMeasurementDto.setNewEntry(mockMeasurement.isNewEntry());
        updateMeasurementDto.setCounterId(mockMeasurement.getCounterId());
        updateMeasurementDto.setCounterValue(mockMeasurement.getCounterValue());
        updateMeasurementDto.setDate(mockMeasurement.getDate());
        updateMeasurementDto.setComment(mockMeasurement.getComment());
        updateMeasurementDto.setCounterType(mockMeasurement.getCounterType());

        when(measurementFacade.update(updateMeasurementDto))
                .thenThrow(new MeasurementNotFoundException());

        MvcResult result = mockMvc.perform(put(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateMeasurementDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(measurementFacade, times(1)).update(updateMeasurementDto);
        verifyNoMoreInteractions(measurementFacade);
    }

    @Test
    @DisplayName("Measurement kann erfolgreich gelöscht werden")
    void t14_deleteMeasurement() throws Exception {
        MvcResult result = mockMvc.perform(delete(ENDPOINT_PATH + "/" + mockMeasurement.getId())
                        .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(measurementFacade, times(1)).delete(mockMeasurement.getId());
        verifyNoMoreInteractions(measurementFacade);
    }

    @Test
    @DisplayName("Wird eine nicht existierendes Measurement versucht zu löschen, wird mit Not Found geantwortet")
    void t15_deleteMeasurementFailsForNonExistingMeasurement() throws Exception {
        doThrow(new MeasurementNotFoundException()).when(measurementFacade).delete(mockMeasurement.getId());

        MvcResult result = mockMvc.perform(delete(ENDPOINT_PATH + "/" + mockMeasurement.getId())
                        .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(measurementFacade, times(1)).delete(mockMeasurement.getId());
        verifyNoMoreInteractions(measurementFacade);
    }



    @Test
    @DisplayName("Alle entsprechenden Measurements für den Start des Clients können vom Server empfangen werden")
    void t16_getMeasurementsForClientStart() {

    }

    @Test
    @DisplayName("Measurement eines gelöschten Customers ist auf dem Server noch vorhanden")
    void t19_MeasurementFromDeletedCustomerStillOnServer(){

    }

    @Test
    @DisplayName("Anfordern von bestimmten Measurements mit einem üngültigen Datumsformat führt zu Bad Request")
    void t21_getMeasurementsFailsWithWrongDateFormat() {

    }

    @Test
    @DisplayName("Alle Measurements zwischen zwei Datumsvorgaben können erfolgreich vom Server empfangen werden")
    void t22_getEveryMeasurementInRangeForSpecificCustomer() {

    }

    @Test
    @DisplayName("Alle Measurements ab einem bestimmten Measurement-date können erfolgreich empfangen werden")
    void t23_getEveryMeasurementSinceSpecificDate() {

    }

    @Test
    @DisplayName("Alle Measurements bis zu einem bestimmten Measurement-date können erfolgreich vom Server empfangen werden")
    void t24_getEveryMeasurementUntilSpecificDate() {

    }



    private String toJson(Object value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    }

    private <T> T fromJson(String json, Class<T> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper.reader().readValue(json, type);
    }

}
