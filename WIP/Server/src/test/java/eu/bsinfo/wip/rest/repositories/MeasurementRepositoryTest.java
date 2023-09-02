package eu.bsinfo.wip.rest.repositories;

import eu.bsinfo.wip.rest.entity.Customer;
import eu.bsinfo.wip.rest.entity.Measurement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
class MeasurementRepositoryTest {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindAllByDateAfter() {
        Customer customer = new Customer("Name", "Firstname");
        customer = customerRepository.save(customer);

        Measurement measurement1 = new Measurement("counterId1", LocalDate.of(2021, 2, 2), customer, "Comment 1", true, 1337, Measurement.CounterType.WATER);
        Measurement measurement2 = new Measurement("counterId2", LocalDate.of(2022, 2, 2), customer, "Comment 2", true, 1337, Measurement.CounterType.WATER);
        Measurement measurement3 = new Measurement("counterId3", LocalDate.of(2018, 2, 2), customer, "Comment 3", true, 1337, Measurement.CounterType.WATER);
        Measurement measurement4 = new Measurement("counterId4", LocalDate.of(2023, 1, 2), customer, "Comment 4", true, 1337, Measurement.CounterType.WATER);
        Measurement measurement5 = new Measurement("counterId5", LocalDate.of(2022, 1, 1), customer, "Comment 5", true, 1337, Measurement.CounterType.WATER);

        measurementRepository.save(measurement1);
        measurementRepository.save(measurement2);
        measurementRepository.save(measurement3);
        measurementRepository.save(measurement4);
        measurementRepository.save(measurement5);

        List<Measurement> measurementsAfter2022 = measurementRepository.findAllByDateGreaterThanEqual(LocalDate.of(2022, 1, 1));
        Assertions.assertEquals(Arrays.asList(measurement2, measurement4, measurement5), measurementsAfter2022);

    }

    @Test
    public void testInsert() {
        // lmao dont do this
        measurementRepository.save(new Measurement());
    }
}
