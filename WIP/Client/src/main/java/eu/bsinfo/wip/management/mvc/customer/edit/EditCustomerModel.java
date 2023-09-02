package eu.bsinfo.wip.management.mvc.customer.edit;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EditCustomerModel {

    private UUID id;
    private String name;
    private String firstName;

}
