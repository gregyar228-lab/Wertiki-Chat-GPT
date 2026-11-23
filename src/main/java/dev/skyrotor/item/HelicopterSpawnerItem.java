package dev.skyrotor.item;

import dev.skyrotor.entity.HelicopterEntity;
import dev.skyrotor.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HelicopterSpawnerItem extends Item {
    public HelicopterSpawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos spawnPos = context.getClickedPos().relative(context.getClickedFace());

        if (!level.isClientSide) {
            HelicopterEntity helicopter = ModEntities.HELICOPTER.get().create(level);
            if (helicopter == null) {
                return InteractionResult.FAIL;
            }

            float yaw = player == null ? 0.0F : player.getYRot();
            helicopter.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, yaw, 0.0F);
            level.addFreshEntity(helicopter);
            level.playSound(null, spawnPos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 0.9F, 0.8F);

            if (player == null || !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
