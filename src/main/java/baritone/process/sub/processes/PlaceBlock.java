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
import baritone.api.pathing.goals.GoalRunAway;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.input.Input;
import baritone.process.sub.processes.helper.ContainerType;
import baritone.process.sub.processes.helper.SlotConverter;
import baritone.utils.BlockStateInterface;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class PlaceBlock extends GoalNearProcess {

    private SlotConverter hotbar;

    public PlaceBlock(BlockPos nearGoal, SlotConverter hotbar, SubProcess nextProcess) {
        super(nearGoal, 2, new OneTimeCommand(nextProcess) {

            @Override
            public void doTick() {
                baritone.getInputOverrideHandler().clearAllKeys();
            }
        });
        this.hotbar = hotbar;
    }

    @Override
    public boolean isFinished() {
        return !(BlockStateInterface.get(ctx, super.nearGoal).getBlock() instanceof BlockAir);
    }

    @Override
    public void doTick() {
        // TODO WARNING: this is only looking on the block below to place...
        baritone.getInputOverrideHandler().clearAllKeys();

        Optional<Rotation> rot = RotationUtils.reachableOffset(ctx.player(), super.nearGoal.down(),
                new Vec3d(super.nearGoal.getX() + 0.5, super.nearGoal.getY(), super.nearGoal.getZ() + 0.5),
                ctx.playerController().getBlockReachDistance());

        if (rot.isPresent()) {
            baritone.getLookBehavior().updateTarget(rot.get(), true);
            if (ctx.isLookingAt(super.nearGoal.down())) {

                ctx.player().inventory.currentItem = this.hotbar.getSlotIn(ContainerType.HOTBAR);
                baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
            }

        }
    }

    @Override
    public PathingCommand generateReturn() {

        if(ctx.playerFeet().getDistance(super.nearGoal.getX(), super.nearGoal.getY(), super.nearGoal.getZ()) < 1.2)
            return new PathingCommand(new GoalRunAway(range, nearGoal), PathingCommandType.REVALIDATE_GOAL_AND_PATH);
        return new PathingCommand(new GoalNear(nearGoal, range), PathingCommandType.REVALIDATE_GOAL_AND_PATH);
    }


}