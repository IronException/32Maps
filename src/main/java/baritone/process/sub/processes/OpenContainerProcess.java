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

import baritone.process.sub.processes.helper.ChestHelper;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;


public class OpenContainerProcess extends LookProcess {

    /**
     *
     *
     * @param blockCoords
     * @param nextProcess
     */

    public OpenContainerProcess(BlockPos blockCoords, SubProcess nextProcess) {
        super(blockCoords, 2, nextProcess);
    }

    @Override
    public boolean isFinished() {
        return ChestHelper.isChestOpen();
    }



    @Override
    public void doTick(){
        // super is adjusting the rotation
        super.doTick();

        // there was no other check in original but can you be sure we don't need any check here?
        rightClick(ctx.objectMouseOver());


    }

    private void rightClick(RayTraceResult trace) {
        ctx.playerController().processRightClickBlock(ctx.player(), ctx.world(), nearGoal, trace.sideHit, trace.hitVec, EnumHand.OFF_HAND);
    }


}
