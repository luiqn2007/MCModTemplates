package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.LanguageProviderZhEn;
import com.example.examplemod.test.ModTab;
import com.example.examplemod.test.item.axe.stripping.StripTest;
import com.example.examplemod.test.tree.TreeTest;
import com.example.examplemod.test.woodwork.WoodworkTest;
import net.minecraft.data.DataGenerator;

public class LanguageProvider extends LanguageProviderZhEn {

    public LanguageProvider(DataGenerator generator, String modid) {
        super(generator, modid);
    }

    @Override
    protected void addTranslations() {
        addGroup(ModTab.INNSTANCE, "Template Examples", "模板测试");
        // item/axe/stripping
        addBlock(StripTest.BLOCK_M_BASE, "[STRIP][MIXIN]Base", "[STRIP][MIXIN]起始");
        addBlock(StripTest.BLOCK_M_STRIPPED, "[STRIP][MIXIN]Stripped", "[STRIP][MIXIN]剥皮");
    }

    @Override
    protected void addTranslationsToProvider(boolean en, net.minecraftforge.common.data.LanguageProvider provider) {
        super.addTranslationsToProvider(en, provider);

        TreeTest.TREE.register().addLanguagesEn(this.en);
        TreeTest.TREE.register().addLanguagesZh(this.zh, "测试");

        WoodworkTest.WOODWORK_WITH_CHEST.register().addLanguagesEn(this.en);
        WoodworkTest.WOODWORK_WITH_CHEST.register().addLanguagesZh(this.zh, "测试木1");
        WoodworkTest.WOODWORK_WITHOUT_CHEST.register().addLanguagesEn(this.en, "_Test Tree_");
        WoodworkTest.WOODWORK_WITHOUT_CHEST.register().addLanguagesZh(this.zh, "测试木2");
    }
}
