package eu.bsinfo.wip.management.mvc.measurement.edit;

import eu.bsinfo.wip.management.resource.measurement.Measurement;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class EditMeasurementModel {

    private UUID id;
    private String customerId;
    private Measurement.CounterType type;
    private String counterId;
    private LocalDate date;
    private Boolean newEntry;
    private String counterValue;
    private String comment;
}
