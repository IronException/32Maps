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

import baritone.api.pathing.goals.GoalNear;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.MovementHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class BreakBlock extends LookProcess {

    public BreakBlock(BlockPos nearGoal, SubProcess nextProcess) {
        super(nearGoal, 2,
                new SubProcess(nextProcess) {

                    @Override
                    public boolean isFinished() {
                        return false;
                    }

                    @Override
                    public void doTick() {
                        if (ctx.player().onGround) {
                            IBlockState state = baritone.bsi.get0(nearGoal);
                            if (!MovementHelper.avoidBreaking(baritone.bsi, nearGoal.getX(), nearGoal.getY(), nearGoal.getZ(), state)) {
                                Optional<Rotation> rot = RotationUtils.reachable(ctx, nearGoal);
                                if (rot.isPresent()) {
                                    baritone.getLookBehavior().updateTarget(rot.get(), true);
                                    MovementHelper.switchToBestToolFor(ctx, ctx.world().getBlockState(nearGoal));
                                    if (ctx.isLookingAt(nearGoal) || ctx.playerRotations().isReallyCloseTo(rot.get()))
                                        baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_LEFT, true);
                                }
                            }
                        }
                    }
                });

    }

    // super got us covered with the ticks...

    @Override
    public PathingCommand generateReturn() {
        if (ctx.player().onGround)
            if (!MovementHelper.avoidBreaking(baritone.bsi, nearGoal.getX(), nearGoal.getY(), nearGoal.getZ(), baritone.bsi.get0(nearGoal)))
                if (RotationUtils.reachable(ctx, nearGoal).isPresent())
                    return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);

         return super.generateReturn();
    }

}
