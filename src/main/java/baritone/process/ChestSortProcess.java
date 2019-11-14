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

package baritone.process;

import baritone.Baritone;
import baritone.api.event.listener.AbstractGameEventListener;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.process.IChestSortProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.process.sub.processes.*;
import baritone.process.sub.processes.helper.AbstractSlot;
import baritone.process.sub.processes.helper.ContainerType;
import baritone.process.sub.processes.helper.SlotHelper;
import baritone.utils.BaritoneProcessHelper;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public final class ChestSortProcess extends BaritoneProcessHelper implements IChestSortProcess, AbstractGameEventListener {
    private static final PathingCommand NO_PATH = new PathingCommand(null, PathingCommandType.DEFER);

    public static BlockPos targetPos = new BlockPos(0, 5, 0);
    public static BlockPos putMaps = new BlockPos(5, 5, 0);
    public static Vec3d putMapLocs = new Vec3d(0, 0, 2);
    public static Vec3i relativeShulkerPos = new Vec3i(3, -1, 0);
    public static SlotHelper hotbarSlot = new SlotHelper(5, ContainerType.HOTBAR);

    public static BlockPos shulkerPos = new BlockPos(-10, 4, 10);


    private boolean active = false;

    public static ChestSortProcess INSTANCE;

    SubProcess process;

    public ChestSortProcess(Baritone baritone) {
        super(baritone);
        ChestSortProcess.INSTANCE = this;

    }


    public SubProcess getProcessBuild() {
        /*return new GoalNearProcess(targetPos, 2,
                new EditChestShulkerProcess(targetPos, new Epsilon())
        ); // TODO its going to the same pos twice like this. so redo that....
        */


        return
                new ChatProcess("start",
                //new putMap(putMaps, putMapLocs, hotbarSlot, relativeShulkerPos,
                //new BreakBlock(targetPos, true,
                new DoInContainerProcess(targetPos, new SwapSlot(new AbstractSlot(Item.getItemById(1)), new SlotHelper(0, ContainerType.HOTBAR), new Epsilon()),
                        new ChatProcess("end", new Epsilon())));

    }


    @Override
    public boolean isActive() {
        return this.active;// && this.process.isFinished();
    }

    @Override
    public void activate() {
        this.active = true;
        this.process = getProcessBuild();
    }


    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        // test wether maps in inv

        if (this.process.finished())
            this.active = false;


        this.process.tick();


        PathingCommand rV = process.getReturn();
        if (rV == null) {
            logDirect("No SubProcess returned a PathingCommand. THIS HAS TO BE FIXED");
            // could also be Request Pause
            rV = new PathingCommand(new GoalBlock(ctx.playerFeet()), PathingCommandType.REVALIDATE_GOAL_AND_PATH);
        }
        return rV;
    }


    @Override
    public void onLostControl() {
        this.active = false;

        // reset the state
        process = null;
    }

    @Override
    public String displayName0() {
        return "ChestSortProcess";
    }


}
