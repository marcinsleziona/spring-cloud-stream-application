package pl.ims.spring.cloud.stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

/*
 * Created on 2020-12-01 08:30
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Result {
    @JsonProperty("Country")
    public String country;
    @JsonProperty("CountryCode")
    public String countryCode;
    @JsonProperty("Province")
    public String province;
    @JsonProperty("City")
    public String city;
    @JsonProperty("CityCode")
    public String cityCode;
    @JsonProperty("Lat")
    public String lat;
    @JsonProperty("Lon")
    public String lon;
    @JsonProperty("Confirmed")
    public int confirmed;
    @JsonProperty("Deaths")
    public int deaths;
    @JsonProperty("Recovered")
    public int recovered;
    @JsonProperty("Active")
    public int active;
    @JsonProperty("Date")
    public Date date;
}
