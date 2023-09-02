package eu.bsinfo.wip.rest.facade.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class CustomerResponseDto {

    private UUID id;
    @Schema(example = "Müller")
    private String name;
    @Schema(example = "Hans")
    private String firstName;
}
