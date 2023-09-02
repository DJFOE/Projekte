package eu.bsinfo.wip.management.mvc.measurement.list;

import eu.bsinfo.wip.management.resource.measurement.Measurement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListMeasurementModel {

    private List<Measurement> measurements;
    private List<Measurement> selectedMeasurements;


}
