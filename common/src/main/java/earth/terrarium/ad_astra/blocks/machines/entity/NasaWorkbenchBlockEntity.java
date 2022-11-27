package earth.terrarium.ad_astra.blocks.machines.entity;

import earth.terrarium.ad_astra.recipes.NasaWorkbenchRecipe;
import earth.terrarium.ad_astra.registry.ModBlockEntities;
import earth.terrarium.ad_astra.registry.ModRecipeTypes;
import earth.terrarium.ad_astra.screen.menu.NasaWorkbenchMenu;
import earth.terrarium.ad_astra.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class NasaWorkbenchBlockEntity extends AbstractMachineBlockEntity implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public NasaWorkbenchBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.NASA_WORKBENCH.get(), blockPos, blockState);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, @NotNull Inventory inv, @NotNull Player player) {
        return new NasaWorkbenchMenu(syncId, inv, this);
    }

    @Override
    public int getInventorySize() {
        return 15;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return slot < 14;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    public void spawnWorkingParticles() {
        if (this.level instanceof ServerLevel serverWorld) {
            BlockPos pos = this.getBlockPos();
            ModUtils.spawnForcedParticles(serverWorld, ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 1, 0.1, 0.1, 0.1, 0.1);
        }
    }

    public void spawnResultParticles() {
        if (this.level instanceof ServerLevel serverWorld) {
            BlockPos pos = this.getBlockPos();
            ModUtils.spawnForcedParticles(serverWorld, ParticleTypes.TOTEM_OF_UNDYING, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 100, 0.1, 0.1, 0.1, 0.7);
            this.level.playSound(null, pos, SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 1.0f, 1.0f);
        }
    }

    public void spawnOutputAndClearInput(NasaWorkbenchRecipe recipe) {
        BlockPos pos = this.getBlockPos();
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.5, recipe.getResultItem().copy());
        itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().scale(0.5));
        this.level.addFreshEntity(itemEntity);
        itemEntity.setDefaultPickUpDelay();

        for (int i = 0; i < this.getItems().size() - 1 && i < recipe.getHolders().size(); i++) {
            this.getItems().get(i).shrink(recipe.getHolders().get(i).count());
        }
        this.setChanged();
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            for (ItemStack input : this.getItems()) {
                if (!input.isEmpty()) {
                    NasaWorkbenchRecipe recipe = ModRecipeTypes.NASA_WORKBENCH_RECIPE.get().findFirst(level, f -> f.test(input));
                    if (recipe != null) {
                        this.spawnWorkingParticles();
                        this.setActive(true);
                    } else {
                        this.setActive(false);
                    }
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "working", 0, this::work));
        data.addAnimationController(new AnimationController<>(this, "idle", 0, this::idle));
    }

    public PlayState work(AnimationEvent<NasaWorkbenchBlockEntity> event) {
        if (!this.isEmpty()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("activate", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("fabricating", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    public PlayState idle(AnimationEvent<NasaWorkbenchBlockEntity> event) {
        if (this.isEmpty()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}