package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Locale.ROOT;

public enum RegionCode {
    GLOBAL,
    @JsonProperty("af-south-1") AF_SOUTH_1,
    @JsonProperty("ap-east-1") AP_EAST_1,
    @JsonProperty("ap-northeast-1") AP_NORTHEAST_1,
    @JsonProperty("ap-northeast-2") AP_NORTHEAST_2,
    @JsonProperty("ap-northeast-3") AP_NORTHEAST_3,
    @JsonProperty("ap-south-1") AP_SOUTH_1,
    @JsonProperty("ap-south-2") AP_SOUTH_2,
    @JsonProperty("ap-southeast-1") AP_SOUTHEAST_1,
    @JsonProperty("ap-southeast-2") AP_SOUTHEAST_2,
    @JsonProperty("ap-southeast-3") AP_SOUTHEAST_3,
    @JsonProperty("ap-southeast-4") AP_SOUTHEAST_4,
    @JsonProperty("ap-southeast-5") AP_SOUTHEAST_5,
    @JsonProperty("ap-southeast-6") AP_SOUTHEAST_6,
    @JsonProperty("ca-central-1") CA_CENTRAL_1,
    @JsonProperty("ca-west-1") CA_WEST_1,
    @JsonProperty("cn-north-1") CN_NORTH_1,
    @JsonProperty("cn-northwest-1") CN_NORTHWEST_1,
    @JsonProperty("eu-central-1") EU_CENTRAL_1,
    @JsonProperty("eu-central-2") EU_CENTRAL_2,
    @JsonProperty("eu-north-1") EU_NORTH_1,
    @JsonProperty("eu-south-1") EU_SOUTH_1,
    @JsonProperty("eu-south-2") EU_SOUTH_2,
    @JsonProperty("eu-west-1") EU_WEST_1,
    @JsonProperty("eu-west-2") EU_WEST_2,
    @JsonProperty("eu-west-3") EU_WEST_3,
    @JsonProperty("il-central-1") IL_CENTRAL_1,
    @JsonProperty("me-central-1") ME_CENTRAL_1,
    @JsonProperty("me-south-1") ME_SOUTH_1,
    @JsonProperty("sa-east-1") SA_EAST_1,
    @JsonProperty("us-east-1") US_EAST_1,
    @JsonProperty("us-east-2") US_EAST_2,
    @JsonProperty("us-gov-east-1") US_GOV_EAST_1,
    @JsonProperty("us-gov-west-1") US_GOV_WEST_1,
    @JsonProperty("us-west-1") US_WEST_1,
    @JsonProperty("us-west-2") US_WEST_2;

    private final String code;
    private final static Set<RegionCode> regionsExceptGlobal = Arrays.stream(RegionCode.values())
            .filter(region -> region != GLOBAL).collect(Collectors.toSet());

    RegionCode() {
        code = name().toLowerCase(ROOT).replaceAll("_", "-");
    }

    public String getCode() {
        return code;
    }

    public static Set<RegionCode> getRegionsExceptGlobal() {
        return regionsExceptGlobal;
    }
}
