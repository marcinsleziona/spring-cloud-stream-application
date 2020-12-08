package pl.ims.spring.cloud.stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

/*
 * Created on 2020-12-08 13:39
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ResultMerged {

    @JsonProperty("Countries")
    @Singular("country")
    private List<String> countries;
    @JsonProperty("Confirmed")
    private int confirmed;
    @JsonProperty("Deaths")
    private int deaths;
    @JsonProperty("Recovered")
    private int recovered;
    @JsonProperty("Active")
    private int active;
    @JsonProperty("Date")
    private Date date;

}
