package com.example.examplemod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

public abstract class LanguageProviderZhEn implements DataProvider {

    private final Logger LOGGER = LogManager.getLogger();

    protected final Map<String, String> enData = new HashMap<>();
    protected final Map<String, String> zhData = new HashMap<>();

    protected final SubProvider en, zh;

    protected final DataGenerator generator;
    protected final String modid;

    private boolean added = false;

    public LanguageProviderZhEn(DataGenerator generator, String modid) {
        this.generator = generator;
        this.modid = modid;

        this.en = new SubProvider(generator, modid, true);
        this.zh = new SubProvider(generator, modid, false);
    }

    /**
     * Add all translations
     */
    protected abstract void addTranslations();

    /**
     * Add block name
     * @param key block
     * @param en english name
     * @param zh chinese name
     */
    public void addBlock(Supplier<? extends Block> key, String en, String zh) {
        add(key.get(), en, zh);
    }

    /**
     * Add block name
     * @param key block
     * @param en english name
     * @param zh chinese name
     */
    public void add(Block key, String en, String zh) {
        add(key.getDescriptionId(), en, zh);
    }

    /**
     * Add item name
     * @param key item
     * @param en english name
     * @param zh chinese name
     */
    public void addItem(Supplier<? extends Item> key, String en, String zh) {
        add(key.get(), en, zh);
    }

    /**
     * Add item name
     * @param key item
     * @param en english name
     * @param zh chinese name
     */
    public void add(Item key, String en, String zh) {
        add(key.getDescriptionId(), en, zh);
    }

    /**
     * Add item name
     * @param key item
     * @param en english name
     * @param zh chinese name
     */
    public void addItemStack(Supplier<ItemStack> key, String en, String zh) {
        add(key.get(), en, zh);
    }

    /**
     * Add item name
     * @param key item
     * @param en english name
     * @param zh chinese name
     */
    public void add(ItemStack key, String en, String zh) {
        add(key.getDescriptionId(), en, zh);
    }

    /**
     * Add enchantment name
     * @param key enchantment
     * @param en english name
     * @param zh chinese name
     */
    public void addEnchantment(Supplier<? extends Enchantment> key, String en, String zh) {
        add(key.get(), en, zh);
    }

    /**
     * Add enchantment name
     * @param key enchantment
     * @param en english name
     * @param zh chinese name
     */
    public void add(Enchantment key, String en, String zh) {
        add(key.getDescriptionId(), en, zh);
    }

    /**
     * Add potion effect name
     * @param key potion effect
     * @param en english name
     * @param zh chinese name
     */
    public void addEffect(Supplier<? extends MobEffect> key, String en, String zh) {
        add(key.get(), en, zh);
    }

    /**
     * Add potion effect name
     * @param key potion effect
     * @param en english name
     * @param zh chinese name
     */
    public void add(MobEffect key, String en, String zh) {
        add(key.getDescriptionId(), en, zh);
    }

    /**
     * Add entity name
     * @param key potion effect
     * @param en english name
     * @param zh chinese name
     */
    public void addEntityType(Supplier<? extends EntityType<?>> key, String en, String zh) {
        add(key.get(), en, zh);
    }

    /**
     * Add entity name
     * @param key potion effect
     * @param en english name
     * @param zh chinese name
     */
    public void add(EntityType<?> key, String en, String zh) {
        add(key.getDescriptionId(), en, zh);
    }

    /**
     * Add creative mode tab name
     * @param key tab
     * @param en english name
     * @param zh chinese name
     */
    public void addGroup(CreativeModeTab key, String en, String zh) {
        addKey((TranslatableComponent) key.getDisplayName(), en, zh);
    }

    /**
     * Add language id name
     * @param key key
     * @param en english name
     * @param zh chinese name
     */
    public void addKey(TranslatableComponent key, String en, String zh) {
        add(key.getKey(), en, zh);
    }

    /**
     * Add custom key
     * @param key key
     * @param en english name
     * @param zh chinese name
     */
    public void add(String key, String en, String zh) {
        if (enData.put(key, en) != null)
            throw new IllegalStateException("Duplicate en_us translation key " + key);
        if (zhData.put(key, zh) != null)
            throw new IllegalStateException("Duplicate zh_cn translation key " + key);
    }

    @Override
    public void run(HashCache pCache) throws IOException {
        en.run(pCache);
        zh.run(pCache);
        printDuplicateKeys(en);
        printDuplicateKeys(zh);
    }

    protected void printDuplicateKeys(SubProvider provider) {
        Map<String, List<String>> duplicates = provider.duplicates;
        if (!duplicates.isEmpty()) {
            LOGGER.warn("Duplicate keys in {}", provider.getName());
            duplicates.forEach((key, values) -> {
                LOGGER.warn("  Key={}, Value={}", key, String.join(", ", values));
            });
        }
    }

    @Override
    public String getName() {
        return "Languages: en_us & zh_cn";
    }

    protected void addTranslationsToProvider(boolean en, LanguageProvider provider) {
        addTranslations();
        (en ? enData : zhData).forEach(provider::add);
    }

    protected class SubProvider extends LanguageProvider {

        final boolean en;
        final Map<String, List<String>> duplicates = new HashMap<>();
        final Map<String, String> data = new TreeMap<>();

        public SubProvider(DataGenerator gen, String modid, boolean en) {
            super(gen, modid, en ? "en_us" : "zh_cn");
            this.en = en;
        }

        @Override
        protected void addTranslations() {
            if (!added) {
                LanguageProviderZhEn.this.addTranslationsToProvider(en, this);
                added = true;
            }
        }

        @Override
        public void add(String key, String value) {
            try {
                super.add(key, value);
                data.put(key, value);
            } catch (IllegalStateException e) {
                duplicates.computeIfAbsent(key, k -> {
                    List<String> list = new ArrayList<>();
                    list.add(data.get(value));
                    return list;
                }).add(value);
            }
        }
    }
}
