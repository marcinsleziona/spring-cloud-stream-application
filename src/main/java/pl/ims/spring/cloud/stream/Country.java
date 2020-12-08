package pl.ims.spring.cloud.stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/*
 * Created on 2020-12-01 08:30
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Country {
    @JsonProperty("Country")
    private String country;
    @JsonProperty("Slug")
    private String slug;
    @JsonProperty("ISO2")
    private String iso2;
}
