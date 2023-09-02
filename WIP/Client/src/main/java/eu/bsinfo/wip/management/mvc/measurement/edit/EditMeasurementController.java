package eu.bsinfo.wip.management.mvc.measurement.edit;

import eu.bsinfo.wip.management.mvc.MvcController;
import eu.bsinfo.wip.management.mvc.MvcView;
import eu.bsinfo.wip.management.mvc.Views;
import eu.bsinfo.wip.management.resource.ResourceOperationException;
import eu.bsinfo.wip.management.resource.measurement.Measurement;
import eu.bsinfo.wip.management.resource.measurement.MeasurementResource;

import java.util.LinkedList;
import java.util.List;

public class EditMeasurementController extends MvcController<EditMeasurementView> {

    private EditMeasurementModelConverter validator = new EditMeasurementModelConverter();
    private MeasurementResource measurementResource = new MeasurementResource();

    @Override
    public void init() {
        view.saveButton.addActionListener(e -> this.save());
    }

    public void save() {
        Measurement measurement;
        try {
            measurement = validator.modelToEntity(view.getModel());
        } catch (Exception e) {
            // Validation error
            view.notifyUser(MvcView.ERROR, "Fehler", e.getMessage());
            return;
        }

        boolean isNew = measurement.getId() == null;
        try {
            if (isNew) {
                measurementResource.create(measurement);
                view.notifyUser(MvcView.SUCCESS, "Gespeichert!", "Eintrag erfolgreich erstellt!");
            } else {
                measurementResource.update(measurement);
                view.notifyUser(MvcView.SUCCESS, "Gespeichert!", "Eintrag erfolgreich gespeichert!");
            }
        } catch (ResourceOperationException e) {
            view.notifyUser(MvcView.ERROR, "Fehler", e.getMessage());

        }


        Views.refreshAll();

        view.hide();
    }

    private List<Measurement> searchDoubleEntries(Measurement measurement, List<Measurement> measurements) {
        List<Measurement> doublesFound = new LinkedList<>();

        for (Measurement cd2 : measurements) {
            if (isDouble(measurement, cd2)) {
                doublesFound.add(cd2);
            }
        }
        return doublesFound;
    }

    private boolean isDouble(Measurement cd1, Measurement cd2) {
        assert cd1.getCounterId() != null;
        return cd1.getCounterId().equals(cd2.getCounterId()) && cd1.getDate().equals(cd2.getDate());
    }
}
