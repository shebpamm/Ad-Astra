package earth.terrarium.ad_astra.client.renderer.block.fabric;

import earth.terrarium.ad_astra.blocks.machines.entity.NasaWorkbenchBlockEntity;
import earth.terrarium.ad_astra.client.renderer.block.model.NasaWorkbenchModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class NasaWorkbenchRenderer extends GeoBlockRenderer<NasaWorkbenchBlockEntity> {
    public NasaWorkbenchRenderer() {
        super(new NasaWorkbenchModel());
    }
}
