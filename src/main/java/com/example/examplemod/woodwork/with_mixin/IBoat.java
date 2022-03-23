package com.example.examplemod.woodwork.with_mixin;

import java.util.Optional;

public interface IBoat {

    Optional<Woodwork> getWoodwork();

    void setWoodwork(Woodwork wood);
}
