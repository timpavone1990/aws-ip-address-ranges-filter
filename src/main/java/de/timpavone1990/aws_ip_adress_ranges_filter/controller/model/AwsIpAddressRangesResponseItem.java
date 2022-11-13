package de.timpavone1990.aws_ip_adress_ranges_filter.controller.model;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Prefix;

import java.util.Locale;

public record AwsIpAddressRangesResponseItem(String region, String ipAddressRange) {
    
    public static AwsIpAddressRangesResponseItem fromPrefix(final Prefix prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Parameter prefix must not be null");
        }
        final String[] regionParts = prefix.region().split("-");
        if (regionParts.length != 3) {
            throw new IllegalArgumentException("Prefix " + prefix + " has an unsupported format");
        }
        final String addressRange = prefix.ipPrefix();
        return new AwsIpAddressRangesResponseItem(regionParts[0].toUpperCase(Locale.getDefault()), addressRange);
    }

    @Override
    public String toString() {
        return region + " " + ipAddressRange;
    }
}
