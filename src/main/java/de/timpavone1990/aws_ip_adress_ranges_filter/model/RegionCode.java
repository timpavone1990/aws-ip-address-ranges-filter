package de.timpavone1990.aws_ip_adress_ranges_filter.model;

import static java.util.Locale.ROOT;

public enum RegionCode {
    AF_SOUTH_1,
    AP_EAST_1,
    AP_NORTHEAST_1,
    AP_NORTHEAST_2,
    AP_NORTHEAST_3,
    AP_SOUTH_1,
    AP_SOUTH_2,
    AP_SOUTHEAST_1,
    AP_SOUTHEAST_2,
    AP_SOUTHEAST_3,
    AP_SOUTHEAST_4,
    AP_SOUTHEAST_5,
    AP_SOUTHEAST_6,
    CA_CENTRAL_1,
    CA_WEST_1,
    CN_NORTH_1,
    CN_NORTHWEST_1,
    EU_CENTRAL_1,
    EU_CENTRAL_2,
    EU_NORTH_1,
    EU_SOUTH_1,
    EU_SOUTH_2,
    EU_WEST_1,
    EU_WEST_2,
    EU_WEST_3,
    IL_CENTRAL_1,
    ME_CENTRAL_1,
    ME_SOUTH_1,
    SA_EAST_1,
    US_EAST_1,
    US_EAST_2,
    US_GOV_EAST_1,
    US_GOV_WEST_1,
    US_WEST_1,
    US_WEST_2;

    private final String codeFirstPartUpperCase = name().substring(0, 2);

    public String getCodeFirstPartUpperCase() {
        return codeFirstPartUpperCase;
    }

    public static RegionCode findByRegionCode(final String regionCode) {
        return valueOf(regionCode.toUpperCase(ROOT).replaceAll("-", "_"));
    }
}
