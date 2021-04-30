package net.justminecraft.prisons.villager;

import org.bukkit.entity.Villager;

public class VillagerShopConfig {
    private String name = null;
    private Villager.Profession profession = null;
    private boolean nameVisible = true;

    public VillagerShopConfig() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setProfession(Villager.Profession profession) {
        this.profession = profession;
    }

    public void setNameVisible(boolean nameVisible) {
        this.nameVisible = nameVisible;
    }

    public String getName() {
        return name;
    }

    public Villager.Profession getProfession() {
        return profession;
    }

    public boolean isNameVisible() {
        return nameVisible;
    }
}
