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

public class ThunderStickItem extends Item {

    private int DISTANCE_FROM_PLAYER = 10;

    public ThunderStickItem(Properties properties) {
        super(properties);

        SoulTether.LOGGER.info("Thunder Stick Item Init");
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        // Do not run on client side to prevent desync
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        // x Blocks in front of player
        Vec3 position = new Vec3(player.getX(), player.getY(), player.getZ());
        Vec3 PlayerLookVec = player.getLookAngle();

        Vec3 frontOfPlayer = PlayerLookVec.multiply(DISTANCE_FROM_PLAYER, 0.0, DISTANCE_FROM_PLAYER).add(position);

        BlockPos frontOfPlayerGround = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(frontOfPlayer));

        SoulTether.LOGGER.info(String.valueOf(position));
        SoulTether.LOGGER.info("New Pos: {}", frontOfPlayerGround);

        LightningBolt bolt = new LightningBolt(EntityTypes.LIGHTNING_BOLT, level);
        bolt.setPos(new Vec3(frontOfPlayerGround.getX(),  frontOfPlayerGround.getY(), frontOfPlayerGround.getZ()));
        level.addFreshEntity(bolt);

        return InteractionResult.SUCCESS;
    }

    public int getDISTANCE_FROM_PLAYER() {
        return DISTANCE_FROM_PLAYER;
    }

    public void setDISTANCE_FROM_PLAYER(int DISTANCE_FROM_PLAYER) {
        this.DISTANCE_FROM_PLAYER = DISTANCE_FROM_PLAYER;
    }
}
