package fr.ferfoui.softcobalt.api.requestformat.header;

public enum HeaderPrincipalKeyword {
    NONE("none"),
    STRING("string"),
    FILE("file"),
    PUBLIC_KEY("public_key"),
    ASK_PUBLIC_KEY("need_a_key"),;

    private final String keyword;

    HeaderPrincipalKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public boolean isKeywordMatching(String keyword) {
        return this.keyword.equals(keyword);
    }

    public static HeaderPrincipalKeyword witchKeywordIsMatching(String keyword) {
        for (HeaderPrincipalKeyword headerKeyword : values()) {
            if (headerKeyword.isKeywordMatching(keyword)) {
                return headerKeyword;
            }
        }
        return NONE;
    }
}
