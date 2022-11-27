package earth.terrarium.ad_astra.client.renderer.block.forge;

import earth.terrarium.ad_astra.blocks.machines.entity.NasaWorkbenchBlockEntity;
import earth.terrarium.ad_astra.client.renderer.block.model.NasaWorkbenchModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class NasaWorkbenchRenderer extends GeoBlockRenderer<NasaWorkbenchBlockEntity> {
    public NasaWorkbenchRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(rendererProvider, new NasaWorkbenchModel());
    }
}
