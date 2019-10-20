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
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.RayTraceUtils;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.MovementHelper;
import baritone.process.BuilderProcess;
import baritone.utils.BlockStateInterface;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalInt;

public class PlaceBlock extends GoalNearProcess {

    public PlaceBlock(BlockPos nearGoal, SubProcess nextProcess) {
        super(nearGoal, 2, nextProcess);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void doTick() {

        Optional<Rotation> rot = RotationUtils.reachableOffset(ctx.player(), super.nearGoal, new Vec3d(super.nearGoal.getX() + 0.5, super.nearGoal.getY() + 1, super.nearGoal.getZ() + 0.5), ctx.playerController().getBlockReachDistance());
        if (rot.isPresent()){// && baritone.getInventoryBehavior().throwaway(true, soulsand ? this::isNetherWart : this::isPlantable)) {
            baritone.getLookBehavior().updateTarget(rot.get(), true);
            if (ctx.isLookingAt(super.nearGoal)) {
                baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
            }

        }




        /*
        Optional<BuilderProcess.Placement> toPlace = possibleToPlace(IBlockState.wit);

        Rotation rot = toPlace.get().rot;
        baritone.getLookBehavior().updateTarget(rot, true);
        ctx.player().inventory.currentItem = toPlace.get().hotbarSelection;
        baritone.getInputOverrideHandler().setInputForceState(Input.SNEAK, true);
        if ((ctx.isLookingAt(toPlace.get().placeAgainst) && ctx.objectMouseOver().sideHit.equals(toPlace.get().side)) || ctx.playerRotations().isReallyCloseTo(rot)) {
            baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
        }
*/
    }

    /*
    private Optional<BuilderProcess.Placement> possibleToPlace(IBlockState toPlace, int x, int y, int z, BlockStateInterface bsi) {
        for (EnumFacing against : EnumFacing.values()) {
            BetterBlockPos placeAgainstPos = new BetterBlockPos(x, y, z).offset(against);
            IBlockState placeAgainstState = bsi.get0(placeAgainstPos);
            if (MovementHelper.isReplacable(placeAgainstPos.x, placeAgainstPos.y, placeAgainstPos.z, placeAgainstState, bsi)) {
                continue;
            }
            if (!ctx.world().mayPlace(toPlace.getBlock(), new BetterBlockPos(x, y, z), false, against, null)) {
                continue;
            }
            AxisAlignedBB aabb = placeAgainstState.getBoundingBox(ctx.world(), placeAgainstPos);
            for (Vec3d placementMultiplier : aabbSideMultipliers(against)) {
                double placeX = placeAgainstPos.x + aabb.minX * placementMultiplier.x + aabb.maxX * (1 - placementMultiplier.x);
                double placeY = placeAgainstPos.y + aabb.minY * placementMultiplier.y + aabb.maxY * (1 - placementMultiplier.y);
                double placeZ = placeAgainstPos.z + aabb.minZ * placementMultiplier.z + aabb.maxZ * (1 - placementMultiplier.z);
                Rotation rot = RotationUtils.calcRotationFromVec3d(ctx.playerHead(), new Vec3d(placeX, placeY, placeZ), ctx.playerRotations());
                RayTraceResult result = RayTraceUtils.rayTraceTowards(ctx.player(), rot, ctx.playerController().getBlockReachDistance());
                if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && result.getBlockPos().equals(placeAgainstPos) && result.sideHit == against.getOpposite()) {
                    OptionalInt hotbar = hasAnyItemThatWouldPlace(toPlace, result, rot);
                    if (hotbar.isPresent()) {
                        return Optional.of(new BuilderProcess.Placement(hotbar.getAsInt(), placeAgainstPos, against.getOpposite(), rot));
                    }
                }
            }
        }
        return Optional.empty();
    }*/

    @Override
    public PathingCommand generateReturn() {
        //return new PathingCommand(null, PathingCommandType.CANCEL_AND_SET_GOAL);
        return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
    }

}