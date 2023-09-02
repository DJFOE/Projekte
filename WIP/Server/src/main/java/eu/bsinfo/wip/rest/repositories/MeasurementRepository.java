package eu.bsinfo.wip.rest.repositories;

import eu.bsinfo.wip.rest.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {

    List<Measurement> findAllByDateGreaterThanEqual(LocalDate date);
}
