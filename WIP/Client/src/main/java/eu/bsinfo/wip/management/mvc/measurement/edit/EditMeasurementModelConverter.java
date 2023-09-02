package eu.bsinfo.wip.management.mvc.measurement.edit;

import eu.bsinfo.wip.management.mvc.ModelConverter;
import eu.bsinfo.wip.management.resource.measurement.Measurement;

import java.util.UUID;

public class EditMeasurementModelConverter implements ModelConverter<Measurement, EditMeasurementModel> {

    @Override
    public EditMeasurementModel entityToModel(final Measurement entity) {
        EditMeasurementModel model = new EditMeasurementModel();

        model.setId(entity.getId());
        model.setCounterId(entity.getCounterId());
        model.setNewEntry(entity.getNewEntry());
        model.setComment(entity.getComment());
        model.setType(entity.getCounterType());
        model.setDate(entity.getDate());
        model.setCustomerId(String.valueOf(entity.getCustomerId()));
        model.setCounterValue(String.valueOf(entity.getCounterValue()));

        return model;
    }

    @Override
    public Measurement modelToEntity(final EditMeasurementModel model) {
        Measurement measurement = new Measurement();

        measurement.setId(model.getId());
        measurement.setCounterId(model.getCounterId());
        measurement.setNewEntry(model.getNewEntry());
        measurement.setComment(model.getComment());
        measurement.setCounterType(model.getType());
        measurement.setDate(model.getDate());
        measurement.setCustomerId(UUID.fromString(model.getCustomerId()));
        measurement.setCounterValue(Long.parseLong(model.getCounterValue()));

        return measurement;
    }
}
