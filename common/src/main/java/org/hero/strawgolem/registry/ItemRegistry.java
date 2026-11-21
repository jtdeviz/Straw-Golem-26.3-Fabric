package org.hero.strawgolem.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.hero.strawgolem.Constants;
import org.hero.strawgolem.platform.Services;

import java.util.function.Supplier;

public class ItemRegistry {
    public static void init() {
        Services.PLATFORM.registerCreativeModeTab("strawgolem_tab", () ->
                Services.PLATFORM.newCreativeTabBuilder()
                        .title(Component.translatable("itemGroup.strawgolem.tab"))
                        .icon(() -> new ItemStack(ItemRegistry.STRAW_HAT.get()))
                        .build()
        );
    }

    public static final Supplier<Item> STRAW_HAT = registerItem("straw_hat", new Item.Properties()
            .stacksTo(1));

    private static Supplier<Item> registerItem(String name, Item.Properties prop) {
        return Constants.COMMON_PLATFORM.registerItem(name, () ->
                new Item(prop.setId(
                        ResourceKey.create(Registries.ITEM,
                                Identifier.fromNamespaceAndPath(
                                        Constants.MODID, name)
                        )
                )));
    }
}
