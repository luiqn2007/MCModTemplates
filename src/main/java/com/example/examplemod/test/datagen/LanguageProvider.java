package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.LanguageProviderZhEn;
import com.example.examplemod.test.*;
import net.minecraft.data.DataGenerator;

public class LanguageProvider extends LanguageProviderZhEn {

    public LanguageProvider(DataGenerator generator, String modid) {
        super(generator, modid);
    }

    @Override
    protected void addTranslations() {
        addGroup(Registers.TAB, "Template Examples", "模板测试");
    }

    @Override
    protected void addTranslationsToProvider(boolean en, net.minecraftforge.common.data.LanguageProvider provider) {
        super.addTranslationsToProvider(en, provider);

        TestTree.TREE.register().addLanguagesEn(this.en);
        TestTree.TREE.register().addLanguagesZh(this.zh, "测试");

        TestWoodwork.WOODWORK1.register().addLanguagesEn(this.en);
        TestWoodwork.WOODWORK1.register().addLanguagesZh(this.zh, "测试木1");
        TestWoodwork.WOODWORK2.register().addLanguagesEn(this.en, "_Test Tree_");
        TestWoodwork.WOODWORK2.register().addLanguagesZh(this.zh, "测试木2");

        TestChest.CHEST1.get().register().addLanguagesEn(this.en);
        TestChest.CHEST1.get().register().addLanguagesZh(this.zh, "箱1");
        TestChest.TRAPPED_CHEST1.get().register().addLanguagesEn(this.en);
        TestChest.TRAPPED_CHEST1.get().register().addLanguagesZh(this.zh, "箱2");
        TestChest.CHEST2.get().register().addLanguagesEn(this.en);
        TestChest.CHEST2.get().register().addLanguagesZh(this.zh, "箱3");
        TestChest.CHEST3.get().register().addLanguagesEn(this.en);
        TestChest.CHEST3.get().register().addLanguagesZh(this.zh, "箱3");

        TestBoat.BOAT.get().register().addLanguagesEn(this.en);
        TestBoat.BOAT.get().register().addLanguagesZh(this.zh, "船-");
    }
}
