package dev.skyrotor.entity;

import dev.skyrotor.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class HelicopterEntity extends Entity {
    private static final EntityDataAccessor<Boolean> ENGINE_ON =
            SynchedEntityData.defineId(HelicopterEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int TAKEOFF_TICKS = 60;
    private static final int BOMB_COOLDOWN_TICKS = 30;
    private static final double FORWARD_SPEED = 0.48D;
    private static final double VERTICAL_SPEED = 0.22D;

    private int takeoffTicks;
    private int bombCooldown;
    private float pilotForward;
    private float pilotStrafe;
    private float pilotLift;
    private float pilotYaw;

    public HelicopterEntity(EntityType<? extends HelicopterEntity> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    public HelicopterEntity(Level level, double x, double y, double z) {
        this(ModEntities.HELICOPTER.get(), level);
        setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ENGINE_ON, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setEngineOn(tag.getBoolean("EngineOn"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("EngineOn", isEngineOn());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean isEngineOn() {
        return this.entityData.get(ENGINE_ON);
    }

    private void setEngineOn(boolean value) {
        this.entityData.set(ENGINE_ON, value);
    }

    public boolean isPilot(Player player) {
        return getControllingPassenger() == player;
    }

    public void toggleEngine(Player pilot) {
        if (level().isClientSide || !isPilot(pilot)) {
            return;
        }

        boolean turnOn = !isEngineOn();
        setEngineOn(turnOn);
        if (turnOn) {
            takeoffTicks = TAKEOFF_TICKS;
            setNoGravity(true);
            level().playSound(null, blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.8F, 1.1F);
            pilot.displayClientMessage(net.minecraft.network.chat.Component.translatable("message.skyrotor.engine_on"), true);
        } else {
            takeoffTicks = 0;
            setNoGravity(false);
            level().playSound(null, blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 0.8F, 0.9F);
            pilot.displayClientMessage(net.minecraft.network.chat.Component.translatable("message.skyrotor.engine_off"), true);
        }
    }

    public void acceptPilotInput(Player pilot, float forward, float strafe, float lift, float yaw) {
        if (!isPilot(pilot)) {
            return;
        }
        this.pilotForward = Mth.clamp(forward, -1.0F, 1.0F);
        this.pilotStrafe = Mth.clamp(strafe, -1.0F, 1.0F);
        this.pilotLift = Mth.clamp(lift, -1.0F, 1.0F);
        if (Float.isFinite(yaw)) {
            this.pilotYaw = Mth.wrapDegrees(yaw);
        }
    }

    public void dropBomb(Player pilot) {
        if (level().isClientSide || !isPilot(pilot) || !isEngineOn() || bombCooldown > 0) {
            return;
        }

        SmallFireball bomb = new SmallFireball(
                level(),
                getX(),
                getY() - 0.8D,
                getZ(),
                0.0D,
                -1.0D,
                0.0D
        );
        bomb.setOwner(this);
        bomb.setDeltaMovement(0.0D, -0.85D, 0.0D);
        level().addFreshEntity(bomb);
        bombCooldown = BOMB_COOLDOWN_TICKS;
        level().playSound(null, blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.65F, 0.8F);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            return;
        }

        if (bombCooldown > 0) {
            bombCooldown--;
        }

        if (getControllingPassenger() == null) {
            pilotForward = 0.0F;
            pilotStrafe = 0.0F;
            pilotLift = 0.0F;
        }

        Vec3 motion = getDeltaMovement();
        if (isEngineOn()) {
            setNoGravity(true);
            setYRot(pilotYaw);
            setYBodyRot(pilotYaw);

            if (takeoffTicks > 0) {
                motion = new Vec3(motion.x * 0.92D, 0.17D, motion.z * 0.92D);
                takeoffTicks--;
            } else {
                float radians = pilotYaw * Mth.DEG_TO_RAD;
                double forwardX = -Mth.sin(radians);
                double forwardZ = Mth.cos(radians);
                double rightX = Mth.cos(radians);
                double rightZ = Mth.sin(radians);

                double targetX = (forwardX * pilotForward + rightX * pilotStrafe) * FORWARD_SPEED;
                double targetZ = (forwardZ * pilotForward + rightZ * pilotStrafe) * FORWARD_SPEED;
                double targetY = pilotLift * VERTICAL_SPEED;

                motion = new Vec3(
                        Mth.lerp(0.22D, motion.x, targetX),
                        Mth.lerp(0.25D, motion.y, targetY),
                        Mth.lerp(0.22D, motion.z, targetZ)
                );
            }
            setDeltaMovement(motion);
            move(MoverType.SELF, motion);
            hasImpulse = true;
        } else {
            setNoGravity(false);
            double nextY = Math.max(-0.65D, motion.y - 0.04D);
            double nextX = motion.x * 0.96D;
            double nextZ = motion.z * 0.96D;
            if (onGround()) {
                nextY = 0.0D;
            }
            Vec3 falling = new Vec3(nextX, nextY, nextZ);
            setDeltaMovement(falling);
            move(MoverType.SELF, falling);
            hasImpulse = true;
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        if (!level().isClientSide && getPassengers().isEmpty()) {
            player.startRiding(this);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public boolean canAddPassenger(Entity passenger) {
        return getPassengers().isEmpty();
    }

    @Override
    public Player getControllingPassenger() {
        return getFirstPassenger() instanceof Player player ? player : null;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.72D;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (hasPassenger(passenger)) {
            double seatY = getY() + getPassengersRidingOffset() + passenger.getMyRidingOffset();
            moveFunction.accept(passenger, getX(), seatY, getZ());
        }
    }

    @Override
    public boolean isPickable() {
        return !isRemoved();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isRemoved();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096.0D;
    }
}
