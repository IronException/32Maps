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
import net.minecraft.util.math.BlockPos;

public class GoalNearProcess extends SubProcess {

    protected BlockPos nearGoal;
    protected int range;

    public GoalNearProcess(BlockPos nearGoal, int range, SubProcess nextProcess) {
        super(nextProcess);
        this.nearGoal = nearGoal;
        this.range = range;
    }

    @Override
    public boolean finished() {
        return true; // TODO is this right?
    }

    @Override
    public void tick() {
        // returning is my job
    }

    @Override
    public PathingCommand getReturn() {
        PathingCommand rV = super.getReturn();
        return (rV != null) ? rV : new PathingCommand(new GoalNear(nearGoal, range), PathingCommandType.REVALIDATE_GOAL_AND_PATH);
    }
}
