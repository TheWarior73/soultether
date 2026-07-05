package com.thewarior73.soultether.Items;

import com.thewarior73.soultether.SoulTether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class ThunderStickItem extends Item {

    private int MAX_DISTANCE_FROM_PLAYER = 32;

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

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookAngle = player.getLookAngle();
        Vec3 endPos = eyePos.add(lookAngle.scale(MAX_DISTANCE_FROM_PLAYER));

        ClipContext context = new ClipContext(
                eyePos,
                endPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        );
        BlockHitResult hitResult = level.clip(context);

        BlockPos strikePos;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            strikePos = hitResult.getBlockPos();
        } else {
            // Fallback: calculate fixed horizontal position at MAX_DISTANCE_FROM_PLAYER
            double dx = lookAngle.x;
            double dz = lookAngle.z;
            double len = Math.sqrt(dx * dx + dz * dz);
            Vec3 horizontalLook;
            if (len > 1e-5) {
                horizontalLook = new Vec3(dx / len, 0.0, dz / len);
            } else {
                // If looking straight up or down, calculate horizontal vector from yaw
                float yaw = player.getYRot();
                float f = -yaw * ((float)Math.PI / 180F);
                horizontalLook = new Vec3(Math.sin(f), 0.0, Math.cos(f));
            }
            Vec3 fallbackPos = player.position().add(horizontalLook.scale(MAX_DISTANCE_FROM_PLAYER));
            strikePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(fallbackPos));
        }

        SoulTether.LOGGER.info("Player Pos: {}", player.position());
        SoulTether.LOGGER.info("New Strike Pos: {}", strikePos);

        LightningBolt bolt = new LightningBolt(EntityTypes.LIGHTNING_BOLT, level);
        bolt.setPos(new Vec3(strikePos.getX() + 0.5, strikePos.getY(), strikePos.getZ() + 0.5));
        level.addFreshEntity(bolt);

        return InteractionResult.SUCCESS;
    }

    public int getMAX_DISTANCE_FROM_PLAYER() {
        return MAX_DISTANCE_FROM_PLAYER;
    }

    public void setMAX_DISTANCE_FROM_PLAYER(int MAX_DISTANCE_FROM_PLAYER) {
        this.MAX_DISTANCE_FROM_PLAYER = MAX_DISTANCE_FROM_PLAYER;
    }
}
