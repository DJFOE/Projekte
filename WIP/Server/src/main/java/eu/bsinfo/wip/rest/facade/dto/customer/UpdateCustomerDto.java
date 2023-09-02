package eu.bsinfo.wip.rest.facade.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UpdateCustomerDto {

    @NotNull(message = "ID is mandatory")
    private UUID id;

    @Schema(example = "Neuer")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Schema(example = "Hans")
    @NotBlank(message = "First name is mandatory")
    private String firstName;
}
