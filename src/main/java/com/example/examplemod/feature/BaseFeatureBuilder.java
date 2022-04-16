package com.example.examplemod.feature;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedList;
import java.util.List;

/**
 * A builder for feature
 * <p>This used to build placed feature</p>
 */
public abstract class BaseFeatureBuilder<C extends FeatureConfiguration, SELF extends BaseFeatureBuilder<C, SELF>> {

    protected final Feature<C> feature;
    protected final List<PlacementModifier> modifiers = new LinkedList<>();

    private C configuration = null;
    private volatile PlacedFeature placed = null;
    private volatile ConfiguredFeature<C, ?> configured = null;
    private volatile Holder<ConfiguredFeature<C, ?>> registeredConfigured = null;
    private volatile Holder<PlacedFeature> registeredPlaced = null;

    public BaseFeatureBuilder(Feature<C> feature) {
        this.feature = feature;
    }

    /**
     * A modifier can set generated base position by range, times, etc...
     *
     * @param modifier generate modifier
     * @return this builder
     */
    public SELF addModifier(PlacementModifier modifier) {
        modifiers.add(modifier);
        return getThis();
    }

    /**
     * Add a modifier and remove same type of other modifiers
     *
     * @param modifier generate modifier
     * @return this builder
     */
    public SELF replaceModifier(PlacementModifier modifier) {
        modifiers.removeIf(modifier.getClass()::isInstance);
        modifiers.add(modifier);
        return getThis();
    }

    /**
     * Copy modifiers from another feature
     *
     * @param parent base feature
     * @return this builder
     */
    public SELF fromModifier(PlacedFeature parent) {
        modifiers.addAll(parent.placement());
        return getThis();
    }

    /**
     * Copy modifiers from another feature
     *
     * @param parent base feature
     * @return this builder
     */
    public SELF fromModifier(Holder<PlacedFeature> parent) {
        return fromModifier(parent.value());
    }

    /**
     * Create a configuration base on parent
     *
     * @param parent base configuration
     * @return this builder
     */
    public abstract SELF fromConfiguration(C parent);

    /**
     * Create a configuration base on parent
     *
     * @param parent base configuration
     * @return this builder
     */
    public SELF fromConfiguration(Holder<C> parent) {
        return fromConfiguration(parent.value());
    }

    /**
     * Create a configuration
     *
     * @return configuration
     */
    protected abstract C buildConfiguration();

    protected C getConfiguration() {
        if (configuration == null) {
            configuration = buildConfiguration();
        }
        return configuration;
    }

    /**
     * Create {@link ConfiguredFeature} from the builder.
     * <p>Note: Not assure that the configured feature is registered in {@link FeatureUtils}.</p>
     *
     * @return configured feature, not ensure registered
     */
    public ConfiguredFeature<C, ?> configured() {
        if (configured == null) {
            configured = new ConfiguredFeature<>(feature, getConfiguration());
        }
        return configured;
    }

    /**
     * Create a {@link ConfiguredFeature} and register it.
     *
     * @param name default name of configured feature
     * @return a registered configured feature
     */
    public Holder<ConfiguredFeature<C, ?>> configured(String name) {
        if (registeredConfigured == null) {
            registeredConfigured = FeatureUtils.register(name, feature, getConfiguration());
        }
        return registeredConfigured;
    }

    /**
     * Create a {@link ConfiguredFeature} and register it.
     *
     * @param modid mod id
     * @param name  name
     * @return a registered configured feature
     */
    public Holder<ConfiguredFeature<C, ?>> configured(String modid, String name) {
        return configured(modid + ":" + name);
    }

    /**
     * Create a {@link ConfiguredFeature} and register it.
     *
     * @param id name
     * @return a registered configured feature
     */
    public Holder<ConfiguredFeature<C, ?>> configured(ResourceLocation id) {
        return configured(id.toString());
    }

    /**
     * Create a {@link ConfiguredFeature} and register it.
     *
     * @param buildFor get name from it
     * @return a registered configured feature
     */
    public Holder<ConfiguredFeature<C, ?>> configured(RegistryObject<?> buildFor) {
        return configured(buildFor.getId());
    }

    /**
     * Create {@link PlacedFeature} from the builder.
     * <p>Note: Not assure that the configured feature or placed feature is registered in {@link PlacementUtils}.</p>
     *
     * @return placed feature, not ensure registered
     */
    public PlacedFeature build() {
        if (placed == null) {
            synchronized (this) {
                if (placed == null) {
                    placed = new PlacedFeature(Holder.direct(configured()), modifiers);
                }
            }
        }
        return placed;
    }

    /**
     * Create a {@link PlacedFeature} and register it.
     *
     * @param name default name of configured feature and placed feature
     * @return a registered placed feature
     */
    public Holder<PlacedFeature> build(String name) {
        synchronized (this) {
            if (registeredConfigured == null) {
                registeredConfigured = FeatureUtils.register(name, feature, getConfiguration());
            }
            if (registeredPlaced == null) {
                registeredPlaced = PlacementUtils.register(name, registeredConfigured, modifiers);
            }
        }
        return registeredPlaced;
    }

    /**
     * Create a {@link PlacedFeature} and register it, name is modid:name
     *
     * @param modid mod id
     * @param name  name
     * @return a registered placed feature
     */
    public Holder<PlacedFeature> build(String modid, String name) {
        return build(modid + ":" + name);
    }

    /**
     * Create a {@link PlacedFeature} and register it, name is modid:name
     *
     * @param id name
     * @return a registered placed feature
     */
    public Holder<PlacedFeature> build(ResourceLocation id) {
        return build(id.toString());
    }

    /**
     * Create a {@link PlacedFeature} and register it, name is builderFor.id
     *
     * @param buildFor get name from it
     * @return a registered placed feature
     */
    public Holder<PlacedFeature> build(RegistryObject<?> buildFor) {
        return build(buildFor.getId());
    }

    @SuppressWarnings("unchecked")
    private SELF getThis() {
        return (SELF) this;
    }
}
