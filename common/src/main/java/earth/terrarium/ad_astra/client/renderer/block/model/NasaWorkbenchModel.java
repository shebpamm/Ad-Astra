package earth.terrarium.ad_astra.client.renderer.block.model;

import earth.terrarium.ad_astra.blocks.machines.entity.NasaWorkbenchBlockEntity;
import earth.terrarium.ad_astra.util.ModResourceLocation;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NasaWorkbenchModel extends AnimatedGeoModel<NasaWorkbenchBlockEntity> {
    public static final ResourceLocation TEXTURE = new ModResourceLocation("textures/block/nasa_workbench.png");
    public static final ResourceLocation MODEL = new ModResourceLocation("geo/block/nasa_workbench.geo.json");
    public static final ResourceLocation ANIMATION = new ModResourceLocation("animations/block/nasa_workbench.animation.json");

    @Override
    public ResourceLocation getModelResource(NasaWorkbenchBlockEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(NasaWorkbenchBlockEntity object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(NasaWorkbenchBlockEntity animatable) {
        return ANIMATION;
    }
}
