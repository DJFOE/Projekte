package eu.bsinfo.wip.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Measurement was not found")
public class MeasurementNotFoundException extends RuntimeException {
}
