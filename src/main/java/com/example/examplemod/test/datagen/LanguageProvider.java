package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.LanguageProviderZhEn;
import com.example.examplemod.test.ItemGroup;
import com.example.examplemod.test.item.axe.stripping.StripTest;
import com.example.examplemod.test.tree.TreeTest;
import com.example.examplemod.test.woodwork.WoodworkTest;
import com.example.examplemod.tree.TreeRegister;
import com.example.examplemod.woodwork.with_mixin.WoodworkRegister;
import net.minecraft.data.DataGenerator;

public class LanguageProvider extends LanguageProviderZhEn {

    public LanguageProvider(DataGenerator generator, String modid) {
        super(generator, modid);
    }

    @Override
    protected void addTranslations() {
        addGroup(ItemGroup.INNSTANCE, "Template Examples", "模板测试");
        // item/axe/stripping
        addBlock(StripTest.BLOCK_M_BASE, "[STRIP][MIXIN]Base", "[STRIP][MIXIN]起始");
        addBlock(StripTest.BLOCK_M_STRIPPED, "[STRIP][MIXIN]Stripped", "[STRIP][MIXIN]剥皮");
        addBlock(StripTest.BLOCK_A_BASE, "[STRIP][AT]Base", "[STRIP][AT]起始");
        addBlock(StripTest.BLOCK_A_STRIPPED, "[STRIP][AT]Stripped", "[STRIP][AT]剥皮");
    }

    @Override
    protected void addTranslationsToProvider(boolean en, net.minecraftforge.common.data.LanguageProvider provider) {
        super.addTranslationsToProvider(en, provider);
        TreeRegister tRegister = TreeTest.TREE.register();
        tRegister.addLanguagesEn(this.en);
        tRegister.addLanguagesZh(this.zh, "测试");
        WoodworkRegister wRegisterM = WoodworkTest.TEST_MIXIN.register();
        wRegisterM.addLanguagesEn(this.en);
        wRegisterM.addLanguagesZh(this.zh, "测试MIXIN木");
        com.example.examplemod.woodwork.with_at.WoodworkRegister wRegisterA = WoodworkTest.TEST_AT.register();
        wRegisterA.addLanguagesEn(this.en);
        wRegisterA.addLanguagesZh(this.zh, "测试AT木");
    }
}
