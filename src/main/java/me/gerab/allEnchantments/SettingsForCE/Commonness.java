package me.gerab.allEnchantments.SettingsForCE;

public enum Commonness {

    COMMON("Gyakori"),
    RARE("Ritka"),
    LEGENDARY("Legendás");

    private final String name;

    Commonness(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
