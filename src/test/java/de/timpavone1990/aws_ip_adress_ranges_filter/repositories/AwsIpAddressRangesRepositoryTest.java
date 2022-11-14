package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.AwsIpAddressRangesClient;
import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.AwsIpAddressRangesClientResponse;
import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.Prefix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter.ALL;
import static de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter.EU;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AwsIpAddressRangesRepositoryTest {

    @Mock
    private AwsIpAddressRangesClient client;
    private final RegionFilterStrategySupplier regionFilterStrategySupplier = new RegionFilterStrategySupplier();

    @BeforeEach
    void setUp() {
        final var awsIpAddressRanges = new AwsIpAddressRangesClientResponse(Set.of(
                new Prefix("52.219.170.0/23", "eu-central-1"),
                new Prefix("52.219.160.0/23", "eu-central-2"),
                new Prefix("52.219.168.0/24", "us-east-1")
        ));
        when(client.getIpAddressRanges()).thenReturn(awsIpAddressRanges);
    }

    @Test
    void testRegionFilterIsNull() {
        final var repository = new AwsIpAddressRangesRepository(client, regionFilterStrategySupplier);
        final var ranges = repository.findRegions(null);
        assertThat(ranges).isNotNull();
        assertThat(ranges).hasSize(3);
    }

    @Test
    void testRegionFilterAll() {
        final var repository = new AwsIpAddressRangesRepository(client, regionFilterStrategySupplier);
        final var ranges = repository.findRegions(ALL);
        assertThat(ranges).isNotNull();
        assertThat(ranges).hasSize(3);
    }

    @Test
    void testRegionFilterEU() {
        final var repository = new AwsIpAddressRangesRepository(client, regionFilterStrategySupplier);
        final var regions = repository.findRegions(EU);
        assertThat(regions).isNotNull();
        assertThat(regions).hasSize(2);
        assertThat(regions).allMatch(region -> region.code().startsWith("eu"));
    }
}
