package ee.telia.personbase.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum InternetSpeed {
    MBPS50(50),
    MBPS100(100),
    MBPS200(200),
    MBPS400(400),
    MBPS1000(1000),
    NONE(0);

    private final int speedMbps;

    InternetSpeed(int speedMbps) {
        this.speedMbps = speedMbps;
    }

    @Override
    @JsonValue
    public String toString() {
        return switch (this) {
            case MBPS50 -> "50 Mbps";
            case MBPS100 -> "100 Mbps";
            case MBPS200 -> "200 Mbps";
            case MBPS400 -> "400 Mbps";
            case MBPS1000 -> "1000 Mbps";
            case NONE -> "-";
        };
    }

    @JsonCreator
    public static InternetSpeed fromString(String value) {
        if (value != null) {
            return switch (value.trim()) {
                case "50 Mbps" -> MBPS50;
                case "100 Mbps" -> MBPS100;
                case "200 Mbps" -> MBPS200;
                case "400 Mbps" -> MBPS400;
                case "1000 Mbps" -> MBPS1000;
                default -> NONE;
            };
        }
        throw new IllegalArgumentException("Value cannot be null");
    }

}
