package com.example.examplemod.woodwork;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;

public class DefaultBoatDispenseBehavior extends ModBoatDispenseBehavior<ModBoat> {

    private final Woodwork woodwork;

    public DefaultBoatDispenseBehavior(Woodwork woodwork) {
        this.woodwork = woodwork;
    }

    @Override
    protected ModBoat newBoat(Level level, double x, double y, double z, float yRot) {
        ModBoat boat = new ModBoat(level, x, y, z);
        boat.setType(Boat.Type.OAK);
        boat.setWoodwork(woodwork);
        boat.setYRot(yRot);
        return boat;
    }
}
