package de.timpavone1990.aws_ip_adress_ranges_filter.controller.model;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public record AwsIpAddressRangesResponseItem(String region, String ipAddressRange) {

    public static Set<AwsIpAddressRangesResponseItem> createItemsFromRegion(final Region region) {
        if (region == null) {
            throw new IllegalArgumentException("Parameter region must not be null");
        }

        final var regionCodeParts = region.code().split("-");
        if (regionCodeParts.length != 3) {
            throw new IllegalArgumentException("Region code " + region.code() + " has an unsupported format");
        }

        final var firstRegionCodePartUppercase = regionCodeParts[0].toUpperCase(Locale.getDefault());
        return region.ipAddressRanges().stream()
                .map(ipAddressRange -> new AwsIpAddressRangesResponseItem(firstRegionCodePartUppercase, ipAddressRange))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return region + " " + ipAddressRange;
    }
}
