package com.thewarior73.soultether.Items;

import com.thewarior73.soultether.SoulTether;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class SoulTetherItem extends Item {

    public SoulTetherItem(Properties properties) {
        super(properties);

        SoulTether.LOGGER.info("Soul Tether Item Init");
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        // Do not run on client side to prevent desync
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }




        return InteractionResult.SUCCESS;
    }
}
