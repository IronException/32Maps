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

import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class LookProcess extends GoalNearProcess {

    public LookProcess(BlockPos nearGoal, int range, SubProcess nextProcess) {
        super(nearGoal, range, nextProcess);
    }

    @Override
    public boolean finished(){
        return ctx.isLookingAt(nearGoal);
    }

    @Override
    public void tick(){
        Optional<Rotation> newRotation = RotationUtils.reachable(ctx, nearGoal); //getRotationForChest(target); // may be aiming at different chest than targetPos but thats fine
        newRotation.ifPresent(rotation -> {
            baritone.getLookBehavior().updateTarget(rotation, true);
        });
    }


}
