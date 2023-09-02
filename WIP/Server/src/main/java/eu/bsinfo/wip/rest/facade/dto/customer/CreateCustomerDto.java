package eu.bsinfo.wip.rest.facade.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateCustomerDto {

    @Schema(example = "Müller")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Schema(example = "Hans")
    @NotBlank(message = "First name is mandatory")
    private String firstName;
}
