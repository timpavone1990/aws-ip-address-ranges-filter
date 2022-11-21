package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

public interface Prefix {

    String ipPrefix();
    RegionCode region();

    Prefix withRegionCode(RegionCode regionCode);
}
