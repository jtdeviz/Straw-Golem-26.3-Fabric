package org.hero.strawgolem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.hero.strawgolem.registry.EntityRegistry;
import org.hero.strawgolem.registry.ItemRegistry;

public class StrawFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();
        EntityRegistry.registerEntityAttributes(FabricDefaultAttributeRegistry::register);

        CreativeModeTabEvents.modifyOutputEvent(
                ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Constants.MODID, "strawgolem_tab"))
        ).register(entries -> {
            entries.accept(ItemRegistry.STRAW_HAT.get());
        });

        Constants.LOG.info("Hello Fabric world!");
    }
}