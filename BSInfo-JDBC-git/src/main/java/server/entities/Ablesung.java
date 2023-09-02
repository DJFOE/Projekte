package server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
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
@Entity
@ToString
public class Ablesung {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String counterId;
    private LocalDate date;
    @ManyToOne
    private Customer customer;
    private String comment;
    private boolean newEntry;
    private int counterValue;
    @Enumerated
    private CounterType counterType;

    public Ablesung(String counterId, LocalDate date, Customer customer, String comment, boolean newEntry, int counterValue, CounterType counterType) {
        this.counterId = counterId;
        this.date = date;
        this.customer = customer;
        this.comment = comment;
        this.newEntry = newEntry;
        this.counterValue = counterValue;
        this.counterType = counterType;
    }

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