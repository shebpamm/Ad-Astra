package earth.terrarium.ad_astra.blocks.machines;

import earth.terrarium.ad_astra.blocks.machines.entity.AlloySmelterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AlloySmelterBlock extends AbstractMachineBlock {

    public AlloySmelterBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean useFacing() {
        return true;
    }

    @Override
    protected boolean useLit() {
        return true;
    }

    @Override
    public AlloySmelterBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlloySmelterBlockEntity(pos, state);
    }
}
