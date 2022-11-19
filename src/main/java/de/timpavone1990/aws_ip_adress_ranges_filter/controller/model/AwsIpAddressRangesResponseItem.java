package de.timpavone1990.aws_ip_adress_ranges_filter.controller.model;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.RegionCode;

import java.util.Set;
import java.util.stream.Collectors;

public record AwsIpAddressRangesResponseItem(RegionCode regionCode, String ipAddressRange) {

    public static Set<AwsIpAddressRangesResponseItem> createItemsFromRegion(final Region region) {
        if (region == null) {
            throw new IllegalArgumentException("Parameter region must not be null");
        }

        return region.ipAddressRanges().stream()
                .map(ipAddressRange -> new AwsIpAddressRangesResponseItem(region.code(), ipAddressRange))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return regionCode.getCodeFirstPartUpperCase() + " " + ipAddressRange;
    }
}
