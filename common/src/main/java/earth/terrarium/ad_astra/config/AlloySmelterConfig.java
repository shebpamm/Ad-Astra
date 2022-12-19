package earth.terrarium.ad_astra.config;

import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;

public class AlloySmelterConfig {
    @ConfigEntry(
            id = "maxEnergy",
            type = EntryType.LONG,
            translation = "text.autoconfig.ad_astra.option.alloy_smelter.maxEnergy"
    )
    public static long maxEnergy = 9000L;

    @ConfigEntry(
            id = "energyPerTick",
            type = EntryType.LONG,
            translation = "text.autoconfig.ad_astra.option.alloy_smelter.energyPerTick"
    )
    public static long energyPerTick = 10L;
}
