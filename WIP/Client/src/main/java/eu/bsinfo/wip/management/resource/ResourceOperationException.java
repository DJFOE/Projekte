package eu.bsinfo.wip.management.resource;

public class ResourceOperationException extends Exception {

    public ResourceOperationException() {
    }

    public ResourceOperationException(final String message) {
        super(message);
    }

    public ResourceOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static ResourceOperationException forServerCommunication() {
        return new ResourceOperationException("Fehler bei der Kommunikation mit dem Server");
    }

    public static ResourceOperationException creating(String resourceName) {
        return new ResourceOperationException(resourceName + " konnte nicht erstellt werden");
    }

    public static ResourceOperationException creating(String resourceName, String reason) {
        return new ResourceOperationException(resourceName + " konnte nicht erstellt werden: " + reason);
    }

    public static ResourceOperationException updating(String resourceName) {
        return new ResourceOperationException(resourceName + " konnte nicht aktualisiert werden");
    }

    public static ResourceOperationException updating(String resourceName, String reason) {
        return new ResourceOperationException(resourceName + " konnte nicht aktualisiert werden: " + reason);
    }

    public static ResourceOperationException reading(String resourceName) {
        return new ResourceOperationException(resourceName + " konnte nicht gelesen werden");
    }

    public static ResourceOperationException reading(String resourceName, String reason) {
        return new ResourceOperationException(resourceName + " konnte nicht gelesen werden: " + reason);
    }

    public static ResourceOperationException deleting(String resourceName) {
        return new ResourceOperationException(resourceName + " konnte nicht gelöscht werden");
    }

    public static ResourceOperationException deleting(String resourceName, String reason) {
        return new ResourceOperationException(resourceName + " konnte nicht gelöscht werden: " + reason);
    }
}
