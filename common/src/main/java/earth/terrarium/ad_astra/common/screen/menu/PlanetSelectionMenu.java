package earth.terrarium.ad_astra.common.screen.menu;

import com.mojang.serialization.DataResult;
import com.teamresourceful.resourcefullib.common.networking.PacketHelper;
import earth.terrarium.ad_astra.AdAstra;
import earth.terrarium.ad_astra.common.data.Planet;
import earth.terrarium.ad_astra.common.data.PlanetData;
import earth.terrarium.ad_astra.common.registry.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlanetSelectionMenu extends AbstractContainerMenu {

    private final Player player;
    private final int tier;

    public PlanetSelectionMenu(int syncId, Player player, FriendlyByteBuf buf) {
        this(syncId, player, buf.readInt());
        PacketHelper.readWithYabn(buf, Planet.CODEC.listOf(), true)
                .get()
                .ifLeft(PlanetData::updatePlanets)
                .mapRight(DataResult.PartialResult::message)
                .ifRight(AdAstra.LOGGER::error);
    }

    public PlanetSelectionMenu(int syncId, Player player, int tier) {
        super(ModMenus.PLANET_SELECTION_SCREEN_HANDLER.get(), syncId);
        this.tier = tier;
        this.player = player;
    }

    public int getTier() {
        return tier;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isDeadOrDying();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return null;
    }
}