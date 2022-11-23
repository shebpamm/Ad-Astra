package earth.terrarium.ad_astra.items;

import earth.terrarium.botarium.api.fluid.*;
import earth.terrarium.botarium.api.item.ItemStackHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.BiPredicate;

public class FluidTankItem extends Item implements FluidContainingItem {
    private final int buckets;

    public FluidTankItem(Properties properties, int buckets) {
        super(properties);
        this.buckets = buckets;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack offhand = player.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack itemInHand = player.getItemInHand(usedHand);
        if (usedHand == InteractionHand.MAIN_HAND && !getFluidContainer(itemInHand).isEmpty()) {
            if (!offhand.isEmpty()) {
                ItemStackHolder from = new ItemStackHolder(itemInHand);
                ItemStackHolder to = new ItemStackHolder(offhand);
                FluidHolder fluidHolder = getTank(itemInHand);
                var amount = FluidHooks.moveItemToItemFluid(from, to, fluidHolder);
                if (amount > 0) {
                    if (from.isDirty()) player.setItemInHand(usedHand, from.getStack());
                    if (to.isDirty()) player.setItemInHand(InteractionHand.OFF_HAND, to.getStack());
                    player.displayClientMessage(Component.translatable( "tooltip.ad_astra.tank_item.message", FluidHooks.toMillibuckets(amount)).withStyle(ChatFormatting.AQUA), true);
                    return InteractionResultHolder.success(itemInHand);
                }
            }
        }
        return InteractionResultHolder.pass(itemInHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag context) {
        long fuel = getTank(stack).getFluidAmount();
        tooltip.add(Component.translatable( "tooltip.ad_astra.tank_item.tooltip", FluidHooks.toMillibuckets(fuel), FluidHooks.toMillibuckets(this.getTankSize())).setStyle(Style.EMPTY.withColor(fuel > 0 ? ChatFormatting.GREEN : ChatFormatting.RED)));
    }

    @Override
    public long getTankSize() {
        return FluidHooks.buckets(buckets);
    }

    @Override
    public BiPredicate<Integer, FluidHolder> getFilter() {
        return (integer, fluidHolder) -> true;
    }
}
