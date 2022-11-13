package de.timpavone1990.aws_ip_adress_ranges_filter.controller;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.AwsIpAddressRangesClient;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.api.V1Api;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.Prefix;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class V1ApiController implements V1Api {

    private final AwsIpAddressRangesClient client;

    public V1ApiController(final AwsIpAddressRangesClient client) {
        this.client = client;
    }

    @Override
    public ResponseEntity<String> findAwsIpAddressRangesByRegion(RegionFilter region) {
        final var ipAddressRanges = client.getIpAddressRanges().prefixes();
        final var filteredIpRanges = region == null || region == RegionFilter.ALL ? ipAddressRanges : filterByRegion(ipAddressRanges, region);
        final var response = filteredIpRanges.stream()
                .map(entry -> entry.region().substring(0, 2).toUpperCase(Locale.getDefault()) + " " + entry.ipPrefix())
                .collect(Collectors.joining("\n"));
        return ResponseEntity.of(Optional.of(response + "\n"));
    }

    private Set<Prefix> filterByRegion(Set<Prefix> ipAddressRanges, RegionFilter region) {
        return ipAddressRanges.stream()
                .filter(prefix -> prefix.region().startsWith(region.getValue().toLowerCase(Locale.getDefault())))
                .collect(Collectors.toSet());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleInvalidRegionFilter(final HttpServletResponse response) throws IOException {
        final var validRegionFilters = Arrays.stream(RegionFilter.values()).map(RegionFilter::getValue).collect(Collectors.joining(", "));
        response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Parameter region must be empty or one of: " + validRegionFilters);
    }
}
