package eu.dozd.mongo.entity;

public class CustomCodecField {
    private final String part1;
    private final String part2;

    public CustomCodecField(String value) {
        String[] split = value.split("/");
        part1 = split[0];
        part2 = split[1];
    }

    public String getPart1() {
        return part1;
    }

    public String getPart2() {
        return part2;
    }

    @Override
    public String toString() {
        return part1 + "/" + part2;
    }
}
