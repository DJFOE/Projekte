package eu.bsinfo.wip.management.mvc.measurement.list;

import eu.bsinfo.wip.management.mvc.ModelConverter;
import eu.bsinfo.wip.management.resource.measurement.Measurement;

import java.util.List;

public class ListMeasurementModelConverter implements ModelConverter<List<Measurement>, ListMeasurementModel> {

    @Override
    public ListMeasurementModel entityToModel(final List<Measurement> entities) {
        ListMeasurementModel model = new ListMeasurementModel();

        model.setMeasurements(entities);

        return null;
    }

    @Override
    public List<Measurement> modelToEntity(final ListMeasurementModel model) {
        return null;
    }
}
