package eu.bsinfo.wip.rest.facade.dto.measurement;

import eu.bsinfo.wip.rest.entity.Measurement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UpdateMeasurementDto {

    @NotNull(message = "ID is mandatory")
    private UUID id;

    @NotBlank(message = "Counter ID is mandatory")
    private String counterId;

    @NotBlank(message = "Date is mandatory")
    private LocalDate date;

    @NotBlank(message = "Customer ID is mandatory")
    private UUID customerId;

    @NotBlank(message = "Comment is mandatory")
    private String comment;

    @NotBlank(message = "New entry is mandatory")
    private boolean newEntry;

    @NotBlank(message = "Counter value is mandatory")
    private int counterValue;

    @NotNull(message = "Counter type is mandatory")
    private Measurement.CounterType counterType;
}
