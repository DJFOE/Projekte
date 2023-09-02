package eu.bsinfo.wip.management.resource.customer;

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

public class CustomerResource {

    private static final Logger LOG = LogManager.getLogger(CustomerResource.class);
    private static final String RESOURCE_NAME = "Kunde";
    private static final String RESOURCE_URL = ClientConfiguration.getConfig().getString("HOST") + "/customer";
    final Client client = ClientBuilder.newClient();

    public List<Customer> getAll() throws ResourceOperationException {
        LOG.trace("Requesting all customers");

        final var target = client.target(RESOURCE_URL).request(MediaType.APPLICATION_JSON);
        try (final var response = target.get()) {
            switch (response.getStatus()) {
                case 200 -> {
                    return response.readEntity(new GenericType<List<Customer>>() {
                    });
                }
                case 500 -> throw ResourceOperationException.reading(RESOURCE_NAME, "Server Fehler");
                default ->
                        throw ResourceOperationException.reading(RESOURCE_NAME, String.valueOf(response.getStatus()));

            }
        } catch (ProcessingException e) {
            LOG.error("Cannot get all customers", e);
            throw ResourceOperationException.forServerCommunication();
        }
    }

    public Customer create(Customer customer) throws ResourceOperationException {
        LOG.trace("Creating customer {}", customer);

        final var target = client.target(RESOURCE_URL).request(MediaType.APPLICATION_JSON);
        try (final var response = target.post(Entity.entity(customer, MediaType.APPLICATION_JSON))) {
            switch (response.getStatus()) {
                case 200 | 201 -> {
                    final var created = response.readEntity(Customer.class);
                    ;
                    LOG.info("Customer created {}", created);
                    return created;
                }
                case 400 -> throw ResourceOperationException.creating(RESOURCE_NAME, "Fehlerhafte Daten");
                case 500 -> throw ResourceOperationException.creating(RESOURCE_NAME, "Server Fehler");
                default ->
                        throw ResourceOperationException.creating(RESOURCE_NAME, String.valueOf(response.getStatus()));
            }
        } catch (ProcessingException e) {
            LOG.error("Cannot create customer {}", customer, e);
            throw ResourceOperationException.forServerCommunication();
        }
    }

    public String update(Customer customer) throws ResourceOperationException {
        LOG.trace("Updating customer {}", customer);

        final var target = client.target(RESOURCE_URL).request(MediaType.WILDCARD);
        try (final var response = target.put(Entity.entity(customer, MediaType.APPLICATION_JSON))) {
            switch (response.getStatus()) {
                case 200 -> {
                    final var updated = response.readEntity(String.class);
                    LOG.info("Customer updated {}", updated);
                    return updated;
                }
                case 400 -> throw ResourceOperationException.updating(RESOURCE_NAME, "Fehlerhafte Daten");
                case 404 -> throw ResourceOperationException.updating(RESOURCE_NAME, "Nicht gefunden");
                case 500 -> throw ResourceOperationException.updating(RESOURCE_NAME, "Server Fehler");
                default ->
                        throw ResourceOperationException.updating(RESOURCE_NAME, String.valueOf(response.getStatus()));
            }
        } catch (ProcessingException e) {
            LOG.error("Cannot update customer {}", customer, e);
            throw ResourceOperationException.forServerCommunication();
        }
    }

    public void delete(UUID uuid) throws ResourceOperationException {
        LOG.trace("Deleting customer by ID {}", uuid);

        final var target = client.target(RESOURCE_URL).path(uuid.toString()).request(MediaType.MEDIA_TYPE_WILDCARD);
        try (final var response = target.delete()) {
            switch (response.getStatus()) {
                case 200 -> LOG.info("Customer {} deleted", uuid);
                case 404 -> throw ResourceOperationException.deleting(RESOURCE_NAME, "Nicht gefunden");
                case 500 -> throw ResourceOperationException.deleting(RESOURCE_NAME, "Server Fehler");
                default ->
                        throw ResourceOperationException.deleting(RESOURCE_NAME, String.valueOf(response.getStatus()));
            }
        } catch (ProcessingException e) {
            LOG.error("Cannot delete customer {}", uuid, e);
            throw ResourceOperationException.forServerCommunication();
        }
    }
}
