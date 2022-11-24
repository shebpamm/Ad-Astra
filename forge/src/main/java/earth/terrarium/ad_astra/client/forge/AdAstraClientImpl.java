package earth.terrarium.ad_astra.client.forge;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class AdAstraClientImpl {
    public static void registerItemProperty(Item item, ResourceLocation name, ClampedItemPropertyFunction property) {
        ItemProperties.register(item, name, property);
    }
}
