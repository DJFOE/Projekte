package eu.bsinfo.wip.management.resource.measurement;

import eu.bsinfo.wip.management.ClientConfiguration;
import eu.bsinfo.wip.management.resource.ResourceOperationException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class MeasurementResource {

    private static final Logger LOG = LogManager.getLogger(MeasurementResource.class);
    private static final String RESOURCE_NAME = "Messung";

    private static final String RESOURCE_URL = ClientConfiguration.getConfig().getString("HOST") + "/measurement";
    final Client client = ClientBuilder.newClient();

    public List<Measurement> getAll() throws ResourceOperationException {
        LOG.trace("Requesting all measurements");

        final var target = client.target(RESOURCE_URL).request(MediaType.MEDIA_TYPE_WILDCARD);
        try (final var response = target.get()) {
            switch (response.getStatus()) {
                case 200 -> {
                    return response.readEntity(new GenericType<List<Measurement>>() {
                    });
                }
                case 500 -> throw ResourceOperationException.reading(RESOURCE_NAME, "Server Fehler");
                default ->
                        throw ResourceOperationException.reading(RESOURCE_NAME, String.valueOf(response.getStatus()));
            }
        } catch (ProcessingException e) {
            LOG.error("Cannot get all measurements", e);
            throw ResourceOperationException.forServerCommunication();
        }
    }

    public List<Measurement> getAllForCustomer(UUID customerId) throws ResourceOperationException {
        LOG.trace("Requesting all measurements for customer {}", customerId);

        final var target = client.target(RESOURCE_URL).queryParam("kunde", customerId.toString()).request(MediaType.MEDIA_TYPE_WILDCARD);
        try (final var response = target.get()) {
            switch (response.getStatus()) {
                case 200 -> {
                    return response.readEntity(new GenericType<List<Measurement>>() {
                    });
                }
                case 500 -> throw ResourceOperationException.reading(RESOURCE_NAME, "Server Fehler");
                default ->
                        throw ResourceOperationException.reading(RESOURCE_NAME, String.valueOf(response.getStatus()));
            }
        } catch (ProcessingException e) {
            LOG.error("Cannot get all measurements for customer {}", customerId, e);
            throw ResourceOperationException.forServerCommunication();
        }
    }

    public Measurement create(Measurement measurement) throws ResourceOperationException {
        LOG.trace("Creating measurement {}", measurement);

        final var target = client.target(RESOURCE_URL).request(MediaType.MEDIA_TYPE_WILDCARD);
        try (final var response = target.post(Entity.entity(measurement, MediaType.APPLICATION_JSON))) {
            switch (response.getStatus()) {
                case 200 | 201 -> {
                    final var created = response.readEntity(Measurement.class);
                    LOG.info("Measurement created {}", created);
                    return created;
                }
                case 400 -> throw ResourceOperationException.creating(RESOURCE_NAME, "Fehlerhafte Daten");
                case 500 -> throw ResourceOperationException.creating(RESOURCE_NAME, "Server Fehler");
                default ->
                        throw ResourceOperationException.creating(RESOURCE_NAME, String.valueOf(response.getStatus()));
            }
        } catch (ProcessingException e) {
            LOG.error("Cannot create measurement {}", measurement, e);
            throw ResourceOperationException.forServerCommunication();
        }
    }


    public String update(Measurement measurement) throws ResourceOperationException {
        LOG.trace("Updating measurement {}", measurement);

        final var target = client.target(RESOURCE_URL).request(MediaType.MEDIA_TYPE_WILDCARD);
        try (final var response = target.put(Entity.entity(measurement, MediaType.APPLICATION_JSON))) {
            switch (response.getStatus()) {
                case 200 -> {
                    final var updated = response.readEntity(String.class);
                    LOG.info("Measurement updated: {}", updated);
                    return updated;
                }
                case 400 -> throw ResourceOperationException.updating(RESOURCE_NAME, "Fehlerhafte Daten");
                case 404 -> throw ResourceOperationException.updating(RESOURCE_NAME, "Nicht gefunden");
                case 500 -> throw ResourceOperationException.updating(RESOURCE_NAME, "Server Fehler");
                default ->
                        throw ResourceOperationException.updating(RESOURCE_NAME, String.valueOf(response.getStatus()));
            }

        } catch (ProcessingException e) {
            LOG.error("Cannot update measurement {}", measurement, e);
            throw ResourceOperationException.forServerCommunication();
        }
    }

    public void delete(UUID uuid) throws ResourceOperationException {
        LOG.trace("Deleting measurement by ID {}", uuid);

        final var target = client.target(RESOURCE_URL).path(uuid.toString()).request(MediaType.MEDIA_TYPE_WILDCARD);
        try (final var response = target.delete()) {
            LOG.info("Deleted measurement by id {}", uuid);
            switch (response.getStatus()) {
                case 200 -> LOG.info("Measurement {} deleted", uuid);
                case 404 -> throw ResourceOperationException.deleting(RESOURCE_NAME, "Nicht gefunden");
                case 500 -> throw ResourceOperationException.deleting(RESOURCE_NAME, "Server Fehler");
                default ->
                        throw ResourceOperationException.deleting(RESOURCE_NAME, String.valueOf(response.getStatus()));
            }
        } catch (ProcessingException e) {
            LOG.error("Cannot delete measurement {}", uuid, e);
            throw ResourceOperationException.forServerCommunication();
        }
    }
}

