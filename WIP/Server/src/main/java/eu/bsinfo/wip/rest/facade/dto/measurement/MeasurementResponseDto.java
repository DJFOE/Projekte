package eu.bsinfo.wip.rest.facade.dto.measurement;

import eu.bsinfo.wip.rest.entity.Measurement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MeasurementResponseDto {

    private UUID id;
    private String counterId;
    private LocalDate date;
    private UUID customerId;
    private String comment;
    private boolean newEntry;
    private int counterValue;
    private Measurement.CounterType counterType;
}
