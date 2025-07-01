package shop.mtcoding.blog.domain.subject.model;

public enum NcsType {
    NCS,
    NON_NCS;

    public String toKorean() {
        return switch (this) {
            case NCS -> "NCS";
            case NON_NCS -> "비NCS";
        };
    }

    public static NcsType fromKorean(String value) {
        return switch (value) {
            case "NCS" -> NCS;
            case "비NCS" -> NON_NCS;
            default -> throw new IllegalArgumentException("지원하지 않는 구분입니다: " + value);
        };
    }
}