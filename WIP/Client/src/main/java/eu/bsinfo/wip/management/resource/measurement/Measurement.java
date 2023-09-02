package eu.bsinfo.wip.management.resource.measurement;

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
public class Measurement {

    private UUID id;
    private UUID customerId;
    private CounterType counterType;
    private String counterId;
    private LocalDate date;
    private Boolean newEntry;
    private Long counterValue;
    private String comment;

    public enum CounterType {

        POWER("Strom"),
        GAS("Gas"),
        HEATING("Heizung"),
        WATER("Wasser");

        final String label;

        CounterType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
