package fr.ferfoui.softcobalt.api.requestformat;

import fr.ferfoui.softcobalt.api.requestformat.header.HeaderPrincipalKeyword;
import fr.ferfoui.softcobalt.api.requestformat.request.Request;

public class TypeRecognizer {

    private final Request request;

    public TypeRecognizer(Request request) {
        this.request = request;
    }

    public HeaderPrincipalKeyword getHeaderPrincipalKeyword() {
        return HeaderPrincipalKeyword.witchKeywordIsMatching(request.header().getPrincipalKeyword());
    }

    public boolean isHeaderPrincipalKeyword(HeaderPrincipalKeyword keyword) {
        return getHeaderPrincipalKeyword().equals(keyword);
    }

}
