package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class DragonAIRiding extends EntityAIBase {

	private final double speed;
	private EntityDragonBase dragon;

	public DragonAIRiding(EntityDragonBase dinosaur, double speed) {
		this.dragon = dinosaur;
		this.speed = speed;
	}

	@Override
	public boolean shouldExecute() {
		return dragon.getControllingPassenger() != null;
	}

	@Override
	public void startExecuting() {
		dragon.getNavigator().clearPathEntity();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (dragon.getControllingPassenger() != null && dragon.getControllingPassenger() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)dragon.getControllingPassenger();
			float speedX = player.moveForward / dragon.getDragonStage();
			float speedZ = player.moveStrafing / dragon.getDragonStage();
			float speedPlayer = Math.max(Math.abs(speedX), Math.abs(speedZ));
			Vec3d look = player.getLookVec();
			float dir = Math.min(speedX, 0) * -1;
			dir += speedZ / (speedX * 2 + (speedX < 0 ? -2 : 2));
			if (dir != 0) {
				look.rotateYaw((float) Math.PI * dir);
			}
			if (speedPlayer > 0) {
				if ((dragon.isFlying() || dragon.isHovering()) && !dragon.onGround) {
					dragon.motionX = (double) (-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI));
					dragon.motionZ = (double) (MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI));
				} else {
					dragon.getMoveHelper().setMoveTo(dragon.posX + look.xCoord, dragon.posY, dragon.posZ + look.zCoord, speed);
				}
			}
		}
	}
}