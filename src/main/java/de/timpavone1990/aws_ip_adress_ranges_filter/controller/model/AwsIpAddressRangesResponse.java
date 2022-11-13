package de.timpavone1990.aws_ip_adress_ranges_filter.controller.model;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Prefix;

import java.util.Set;
import java.util.stream.Collectors;

public record AwsIpAddressRangesResponse(Set<AwsIpAddressRangesResponseItem> responseItems) {

    public static AwsIpAddressRangesResponse fromPrefixes(final Set<Prefix> ipAddressRanges) {
        final var responseItems = ipAddressRanges.stream()
                .map(AwsIpAddressRangesResponseItem::fromPrefix)
                .collect(Collectors.toSet());
        return new AwsIpAddressRangesResponse(responseItems);
    }

    @Override
    public String toString() {
        return responseItems.stream()
                .map(AwsIpAddressRangesResponseItem::toString)
                .collect(Collectors.joining("\n"));
    }
}
