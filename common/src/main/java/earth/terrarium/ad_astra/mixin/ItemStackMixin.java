package earth.terrarium.ad_astra.mixin;

import earth.terrarium.ad_astra.AdAstra;
import earth.terrarium.ad_astra.items.armour.JetSuit;
import earth.terrarium.botarium.api.energy.ItemEnergyContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Inject(method = "hurtAndBreak", at = @At("HEAD"), cancellable = true)
    public <T extends LivingEntity> void adastra_jetsuit(int amount, T entity, Consumer<T> onBroken, CallbackInfo ci) {
        if(this.getItem() instanceof JetSuit jetSuit) {
            ItemStack itemStack = (ItemStack) (Object) this;
            ItemEnergyContainer energyStorage = jetSuit.getEnergyStorage(itemStack);
            long jetSuitEnergyPerTick = AdAstra.CONFIG.spaceSuit.jetSuitEnergyPerTick;
            if(energyStorage.extractEnergy(jetSuitEnergyPerTick, false) == jetSuitEnergyPerTick) {
                energyStorage.update(itemStack);
                ci.cancel();
            }
            energyStorage.update(itemStack);
        }
    }
}
