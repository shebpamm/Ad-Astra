package earth.terrarium.ad_astra.items;

import earth.terrarium.ad_astra.config.EnergizerConfig;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.energy.ItemEnergyContainer;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class EnergizerBlockItem extends MachineBlockItem implements EnergyItem {
    public EnergizerBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag context) {
        long energy = getEnergyStorage(stack).getStoredEnergy();
        tooltip.add(Component.translatable("gauge_text.ad_astra.storage", energy, EnergizerConfig.maxEnergy).setStyle(Style.EMPTY.withColor(energy > 0 ? ChatFormatting.GREEN : ChatFormatting.RED)));
        super.appendHoverText(stack, level, tooltip, context);
    }

    @Override
    public StatefulEnergyContainer<ItemStack> getEnergyStorage(ItemStack object) {
        return new ItemEnergyContainer(object, EnergizerConfig.maxEnergy);
    }
}
