package earth.terrarium.ad_astra.blocks.machines.entity;

import earth.terrarium.ad_astra.AdAstra;
import earth.terrarium.ad_astra.config.CoalGeneratorConfig;
import earth.terrarium.ad_astra.recipes.CookingRecipe;
import earth.terrarium.ad_astra.registry.ModBlockEntities;
import earth.terrarium.ad_astra.registry.ModRecipeTypes;
import earth.terrarium.ad_astra.screen.menu.AlloySmelterMenu;
import earth.terrarium.ad_astra.screen.menu.CoalGeneratorMenu;
import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.EnergyHooks;
import earth.terrarium.botarium.api.energy.ExtractOnlyEnergyContainer;
import earth.terrarium.botarium.util.CommonHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AlloySmelterBlockEntity extends ProcessingMachineBlockEntity implements EnergyBlock {
    private ExtractOnlyEnergyContainer energyContainer;

    public AlloySmelterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.COAL_GENERATOR.get(), blockPos, blockState);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, @NotNull Inventory inv, @NotNull Player player) {
        return new AlloySmelterMenu(syncId, inv, this);
    }

    // Only input.
    @Override
    public int getInventorySize() {
        return 4;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return slot < 4;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide()) {
            if (this.getEnergyStorage().internalExtract(this.getEnergyPerTick(), true) > 0) {
                ItemStack input = this.getItem(0);
                if (!input.isEmpty() && (input.getItem().equals(this.inputItem) || this.inputItem == null)) {
                    this.setActive(true);
                    if (this.cookTime < this.cookTimeTotal) {
                        this.cookTime++;
                        this.getEnergyStorage().internalExtract(this.getEnergyPerTick(), false);

                    } else if (this.outputStack != null) {
                        input.shrink(1);
                        this.finishCooking();

                    } else {
                        CookingRecipe recipe = this.createRecipe(ModRecipeTypes.COMPRESSING_RECIPE.get(), input, true);
                        if (recipe != null) {
                            this.cookTimeTotal = recipe.getCookTime();
                            this.cookTime = 0;
                        }
                    }
                } else if (this.outputStack != null) {
                    this.stopCooking();
                } else {
                    this.setActive(false);
                }
            }
        }
    }

    public long getEnergyPerTick() {
        return CoalGeneratorConfig.energyPerTick;
    }

    public long getMaxCapacity() {
        return this.getEnergyStorage().getMaxCapacity();
    }

    @Override
    public ExtractOnlyEnergyContainer getEnergyStorage() {
        return energyContainer == null ? energyContainer = new ExtractOnlyEnergyContainer(this, (int) CoalGeneratorConfig.maxEnergy) : this.energyContainer;
    }

    @Override
    public void update() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }
}