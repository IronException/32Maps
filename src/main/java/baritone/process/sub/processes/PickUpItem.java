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

import baritone.api.pathing.goals.GoalBlock;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.process.sub.processes.helper.ChestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class PickUpItem extends ReturnProcess {

    Item toPickUp;
    BlockPos nearGoal, actual;
    int itemsInInv;

    public PickUpItem(Item itemWeWant, BlockPos nearGoal, SubProcess nextProcess) {
        super(nextProcess);
        // TODO need ro move this
        this.toPickUp = itemWeWant;
        this.nearGoal = nearGoal;
    }

    public void setItem(Item item){
        this.toPickUp = item;
    }

    @Override
    public boolean isFinished() {
        // well, how do we know we picked up exactly that?
        return ChestHelper.itemsInInv(toPickUp) > this.itemsInInv;
    }

    @Override
    public void doTick() {
        List<BlockPos> blocks = droppedItemsScan(this.toPickUp, ctx.world());
        this.itemsInInv = ChestHelper.itemsInInv(toPickUp);
        blocks.sort(new Comparator<BlockPos>() {
            @Override
            public int compare(BlockPos o1, BlockPos o2) {
                return (int) (o1.distanceSq((double) nearGoal.getX(), (double) nearGoal.getY(), (double) nearGoal.getZ())
                        - o2.distanceSq((double) nearGoal.getX(), (double) nearGoal.getY(), (double) nearGoal.getZ()));
            }
        });

        if (!blocks.isEmpty())
            actual = blocks.get(0);
    }

    @Override
    public PathingCommand generateReturn() {
        if (this.actual == null)
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        return new PathingCommand(new GoalBlock(this.actual), PathingCommandType.REVALIDATE_GOAL_AND_PATH);
    }

    public static List<BlockPos> droppedItemsScan(Item item, World world) {

        // this must also work without a list. but I dont know and never change a running system...
        Set<Item> searchingFor = new HashSet<>();
        searchingFor.add(item);


        List<BlockPos> ret = new ArrayList<>();
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                EntityItem ei = (EntityItem) entity;
                if (searchingFor.contains(ei.getItem().getItem())) {
                    ret.add(new BlockPos(entity));
                }
            }
        }
        return ret;
    }
}
