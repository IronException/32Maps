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

package baritone.process.chest.sorter;

import baritone.api.utils.IPlayerContext;
import baritone.process.ChestSortProcess;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
public class GetMapVisitor extends ChestSortProcess.ChestVisitor {

    // temp impl
    private int workingSlot;
    private BlockPos targetPos;





    public GetMapVisitor(ChestSortProcess parent, BlockPos targetPos){//, Set<ChestSortProcess.UniqueChest> toSort, Map<ChestSortProcess.UniqueChest, List<ItemStack>> chestData) {
        super(parent);
        this.targetPos = targetPos;

        this.workingSlot = 0;
    }


    public boolean finished() {
        return workingSlot > 26;
    }

    @Override
    public boolean onContainerOpened(Container container) {
        return true;
    }

    @Override
    public void onContainerClosed(int windowId) {
    }



    @Override
    public boolean containerOpenTick(Container container) {
        if(finished()){
            return false;
        }

        pickupClick(workingSlot, parent.ctx);
        pickupClick(getInvSlotIndex(workingSlot, container), parent.ctx);

        // put the item back that could be in you hand now (player inv might not be empty)
        pickupClick(workingSlot, parent.ctx);


        workingSlot ++;



        return !finished();

    }

    @Override
    public BlockPos getGoalPos() {
        return targetPos;
    }

    @Override
    public ChestSortProcess.ChestVisitor getNextVisitor(){
        return PutMapVisitor.getPutMapVisitor(parent);
    }

    private static int getInvSlotIndex(int slot, Container chest) {

        if (chest instanceof ContainerChest)
            return ((ContainerChest) chest).getLowerChestInventory().getSizeInventory() + slot;

        if (chest instanceof ContainerShulkerBox)
            return 27 + slot;
        return slot;
    }

    private static ItemStack pickupClick(int slotId, IPlayerContext ctx) {
        return ctx.playerController().windowClick(ctx.player().openContainer.windowId, slotId, 0, ClickType.PICKUP, ctx.player());
    }

   

    // combine 2 iterators into list of pairs
    // second argument must at least have the same number of elements as the first argument
    // any extra elements from the second iterator are unused
    private static <T extends Tuple<?, ?>, A, B> List<T> combine(Iterator<A> iterA, Iterator<B> iterB, BiFunction<A, B, T> toPairFn) {
        final List<T> out = new ArrayList<>();

        while(iterA.hasNext()) {
            if (!iterB.hasNext()) throw new IllegalArgumentException("second argument must not have less elements than the first argument");
            out.add(toPairFn.apply(iterA.next(), iterB.next()));
        }

        return out;
    }

}
