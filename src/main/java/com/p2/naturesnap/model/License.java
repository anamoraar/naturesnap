package com.p2.naturesnap.model;

//Enum para las licencias de CC
public enum License {
    CC_BY("Attribution"),
    CC_BY_SA("Attribution-ShareAlike"),
    CC_BY_ND("Attribution-NoDerivs"),
    CC_BY_NC("Attribution-NonCommercial"),
    CC_BY_NC_SA("Attribution-NonCommercial-ShareAlike"),
    CC_BY_NC_ND("Attribution-NonCommercial-NoDerivs");

    //El nombre de la licencia
    private final String name;

    License(String name) {
        this.name = name;
    }

    public String getLicenseName() {
        return this.name;
    }
}
