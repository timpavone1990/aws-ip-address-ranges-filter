package de.timpavone1990.aws_ip_adress_ranges_filter.controller;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AwsIpAddressRangesResponseRenderer {

    private final StringBuilderFactory stringBuilderFactory;

    public AwsIpAddressRangesResponseRenderer(final StringBuilderFactory stringBuilderFactory) {
        this.stringBuilderFactory = stringBuilderFactory;
    }

    public String renderRegions(final Set<Region> regions) {
        if (regions == null) {
            throw new IllegalArgumentException("Parameter regions must not be null.");
        }

        final var responseBuilder = stringBuilderFactory.createStringBuilder(regions);

        for (Region region : regions) {
            for (String ipAddressRange : region.ipAddressRanges()) {
                responseBuilder.append(region.code().getCodeFirstPartUpperCase());
                responseBuilder.append(" ");
                responseBuilder.append(ipAddressRange);
                responseBuilder.append("\n");
            }
        }

        return responseBuilder.toString();
    }

    @Component
    public static class StringBuilderFactory {
        private final static int MAX_RESPONSE_LINE_LENGTH = 47; // EU 0000:0000:0000:0000:0000:0000:0000:0000/128\n

        public StringBuilder createStringBuilder(final Set<Region> regions) {
            final var numberOfLines = regions.stream()
                    .map(region -> region.ipAddressRanges().size())
                    .reduce(Integer::sum)
                    .orElse(0);
            return new StringBuilder(MAX_RESPONSE_LINE_LENGTH * numberOfLines);
        }
    }
}
