package eu.bsinfo.wip.rest.service;

import eu.bsinfo.wip.rest.entity.Measurement;
import eu.bsinfo.wip.rest.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MeasurementService {

    @Autowired
    public MeasurementRepository repository;

    public List<Measurement> getAll() {
        return repository.findAll();
    }

    public Optional<Measurement> getById(UUID id) {
        if (id == null)
            return Optional.empty();
        return repository.findById(id);
    }

    public List<Measurement> getByCustomerId(UUID id) {
        return getAll().stream().filter(m -> m.getCustomer() != null && m.getCustomer().getId().equals(id)).toList();
    }


    public List<Measurement> getByDateAfter(LocalDate date) {
        return repository.findAllByDateGreaterThanEqual(date);
    }

    public Measurement create(Measurement measurement) {
        // we could replace this with measurement.setId(null)
        Assert.isNull(measurement.getId(), "Creating measurement id must be null");
        return repository.save(measurement);
    }

    public Measurement update(Measurement measurement) {
        Assert.notNull(measurement.getId(), "Updating measurement id may not be null");
        return repository.save(measurement);
    }

    public void delete(UUID id) {
        Assert.notNull(id, "ID to delete may not be null");
        repository.deleteById(id);
    }
}
