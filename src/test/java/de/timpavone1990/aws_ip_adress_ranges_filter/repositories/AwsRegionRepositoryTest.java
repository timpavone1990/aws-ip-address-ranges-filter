package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.AwsIpAddressRangesClient;
import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.AwsIpAddressRangesClientResponse;
import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.IpV4Prefix;
import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.IpV6Prefix;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.RegionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.EU_CENTRAL_1;
import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.EU_CENTRAL_2;
import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.GLOBAL;
import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.US_EAST_1;
import static de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter.ALL;
import static de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter.EU;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AwsRegionRepositoryTest {

    @Mock
    private AwsIpAddressRangesClient client;
    private final RegionFilterStrategySupplier regionFilterStrategySupplier = new RegionFilterStrategySupplier();

    @BeforeEach
    void setUp() {
        final var ipV4Prefixes = Set.of(
                new IpV4Prefix("52.219.170.0/23", EU_CENTRAL_1),
                new IpV4Prefix("52.219.160.0/23", EU_CENTRAL_2),
                new IpV4Prefix("52.219.168.0/24", US_EAST_1)
        );
        final var ipV6Prefixes = Set.of(
                new IpV6Prefix("2600:1ff2:4000::/40", EU_CENTRAL_1),
                new IpV6Prefix("2a05:d07a:a000::/40", US_EAST_1)
        );
        final var awsIpAddressRanges = new AwsIpAddressRangesClientResponse(ipV4Prefixes, ipV6Prefixes);
        when(client.getIpAddressRanges()).thenReturn(awsIpAddressRanges);
    }

    @Test
    void testRegionFilterIsNull() {
        final var repository = new AwsRegionRepository(client, regionFilterStrategySupplier);
        final var ranges = repository.findRegions(null);
        assertThat(ranges).isNotNull();
        assertThat(ranges).hasSize(3);
    }

    @Test
    void testRegionFilterAll() {
        final var repository = new AwsRegionRepository(client, regionFilterStrategySupplier);
        final var ranges = repository.findRegions(ALL);
        assertThat(ranges).isNotNull();
        assertThat(ranges).hasSize(3);
    }

    @Test
    void testRegionFilterEU() {
        final var repository = new AwsRegionRepository(client, regionFilterStrategySupplier);
        final var regions = repository.findRegions(EU);
        assertThat(regions).isNotNull();
        assertThat(regions).hasSize(2);
        assertThat(regions).allMatch(region -> region.code().name().startsWith("EU"));
    }

    @Test
    void testExpandGlobalRegion() {
        final var ipV4Prefixes = Set.of(
                new IpV4Prefix("52.219.170.0/23", EU_CENTRAL_1),
                new IpV4Prefix("52.219.168.0/24", US_EAST_1),
                new IpV4Prefix("1.2.3.4/24", GLOBAL),
                new IpV4Prefix("4.5.6.7/26", GLOBAL)
        );
        final var ipV6Prefixes = Set.of(
                new IpV6Prefix("2600:1ff2:4000::/40", EU_CENTRAL_1),
                new IpV6Prefix("2a05:d07a:a000::/40", US_EAST_1),
                new IpV6Prefix("2600:1f69:e0c0::/46", GLOBAL),
                new IpV6Prefix("240f:80f8:4000::/40", GLOBAL)
        );
        final var awsIpAddressRanges = new AwsIpAddressRangesClientResponse(ipV4Prefixes, ipV6Prefixes);
        when(client.getIpAddressRanges()).thenReturn(awsIpAddressRanges);

        final var repository = new AwsRegionRepository(client, regionFilterStrategySupplier);
        final var regions = repository.findRegions(ALL);

        assertThat(regions).hasSize(35);

        assertThat(regions).contains(
                new de.timpavone1990.aws_ip_adress_ranges_filter.model.Region(RegionCode.EU_CENTRAL_1, Set.of("52.219.170.0/23", "1.2.3.4/24", "4.5.6.7/26", "2600:1ff2:4000::/40", "2600:1f69:e0c0::/46", "240f:80f8:4000::/40")),
                new de.timpavone1990.aws_ip_adress_ranges_filter.model.Region(RegionCode.US_EAST_1, Set.of("52.219.168.0/24", "1.2.3.4/24", "4.5.6.7/26", "2a05:d07a:a000::/40", "2600:1f69:e0c0::/46", "240f:80f8:4000::/40"))
        );

        de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.getRegionsExceptGlobal()
                .stream()
                .filter(region -> region != EU_CENTRAL_1 && region != US_EAST_1).forEach(region -> {
                    assertThat(regions).contains(new de.timpavone1990.aws_ip_adress_ranges_filter.model.Region(RegionCode.findByRegionCode(region.getCode()), Set.of("1.2.3.4/24", "4.5.6.7/26", "2600:1f69:e0c0::/46", "240f:80f8:4000::/40")));
                });
    }
}
