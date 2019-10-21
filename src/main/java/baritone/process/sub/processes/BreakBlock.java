/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.process.sub.processes;

import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.MovementHelper;
import baritone.utils.BlockStateInterface;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class BreakBlock extends GoalNearProcess {

    public BreakBlock(BlockPos nearGoal, SubProcess nextProcess) {
        super(nearGoal, 2, new OneTimeCommand(nextProcess) {

            @Override
            public void doTick() {
                baritone.getInputOverrideHandler().clearAllKeys();
            }
        });
    }

    @Override
    public boolean isFinished() {
        return BlockStateInterface.get(ctx, super.nearGoal).getBlock() instanceof BlockAir;
    }

    @Override
    public void doTick() {
        baritone.getInputOverrideHandler().clearAllKeys();

        if (ctx.player().onGround)
            if (!MovementHelper.avoidBreaking(baritone.bsi, super.nearGoal.getX(), super.nearGoal.getY(), super.nearGoal.getZ(), baritone.bsi.get0(super.nearGoal))) {
                Optional<Rotation> rot = RotationUtils.reachable(ctx, super.nearGoal);
                if (rot.isPresent()) {
                    baritone.getLookBehavior().updateTarget(rot.get(), true);
                    MovementHelper.switchToBestToolFor(ctx, ctx.world().getBlockState(super.nearGoal));
                    if (ctx.isLookingAt(super.nearGoal) || ctx.playerRotations().isReallyCloseTo(rot.get()))
                        baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_LEFT, true);
                }
            }
    }

    @Override
    public PathingCommand generateReturn() {
        if (ctx.player().onGround)
            if (!MovementHelper.avoidBreaking(baritone.bsi, super.nearGoal.getX(), super.nearGoal.getY(), super.nearGoal.getZ(), baritone.bsi.get0(super.nearGoal)))
                if (RotationUtils.reachable(ctx, super.nearGoal).isPresent())
                    return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
         return super.generateReturn();
    }

}
