package eu.bsinfo.wip.management.mvc.measurement.list;

import com.sun.istack.Nullable;
import eu.bsinfo.wip.management.mvc.MvcController;
import eu.bsinfo.wip.management.mvc.MvcView;
import eu.bsinfo.wip.management.mvc.Refreshable;
import eu.bsinfo.wip.management.mvc.Views;
import eu.bsinfo.wip.management.mvc.measurement.edit.EditMeasurementController;
import eu.bsinfo.wip.management.mvc.measurement.edit.EditMeasurementModel;
import eu.bsinfo.wip.management.mvc.measurement.edit.EditMeasurementModelConverter;
import eu.bsinfo.wip.management.mvc.measurement.edit.EditMeasurementView;
import eu.bsinfo.wip.management.resource.ResourceOperationException;
import eu.bsinfo.wip.management.resource.measurement.Measurement;
import eu.bsinfo.wip.management.resource.measurement.MeasurementResource;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ListMeasurementController extends MvcController<ListMeasurementView> implements Refreshable {

    private EditMeasurementModelConverter modelConverter = new EditMeasurementModelConverter();
    private MeasurementResource measurementResource = new MeasurementResource();

    @Nullable
    private UUID forCustomerId;

    public ListMeasurementController() {
        forCustomerId = null;
    }

    public ListMeasurementController(UUID customerId) {
        forCustomerId = customerId;
    }

    @Override
    public void init() {
        view.customerSearchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                //view.tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + view.getSearchText()));
            }
        });

        view.addItem.addActionListener(e -> {
            EditMeasurementView editView = new EditMeasurementView();
            Views.startView(editView, new EditMeasurementController());

            EditMeasurementModel model = new EditMeasurementModel();
            model.setDate(LocalDate.now());

            editView.fillView(model);

            editView.show();

            editView.setWindowClosedOperation(this::refresh);
        });

        view.editItem.addActionListener(e -> {
            EditMeasurementView editView = new EditMeasurementView();
            Views.startView(editView, new EditMeasurementController());

            EditMeasurementModel model = modelConverter.entityToModel(view.getModel().getSelectedMeasurements().get(0));

            editView.fillView(model);

            editView.show();

            editView.setWindowClosedOperation(this::refresh);
        });

        view.deleteItem.addActionListener(a -> {
            if (view.askUser(MvcView.OK_CANCEL_OPTION, "Aktion bestätigen: Messung löschen", "Messung endgültig löschen?") == MvcView.YES_OPTION) {
                view.getModel().getSelectedMeasurements().forEach(m -> {
                    try {
                        measurementResource.delete(m.getId());
                    } catch (ResourceOperationException e) {
                        view.notifyUser(MvcView.ERROR, "Fehler", e.getMessage());
                    }
                });
                refresh();
            }
        });

        refresh();
    }

    @Override
    public void refresh() {
        ListMeasurementModel model = new ListMeasurementModel();
        try {
            List<Measurement> measurements;

            if (forCustomerId != null) {
                measurements = measurementResource.getAllForCustomer(forCustomerId);
            } else {
                measurements = measurementResource.getAll();
            }
            model.setMeasurements(measurements);
        } catch (ResourceOperationException e) {
            view.notifyUser(MvcView.ERROR, "Fehler", "Ablesungen konnten nicht aktualisiert werden.", e.getMessage());
        }

        view.fillView(model);
    }
}
