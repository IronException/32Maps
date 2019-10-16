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
import com.google.common.collect.*;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class GetMapVisitor extends ChestSortProcess.ChestVisitor {

    // temp impl
    private int workingSlot = 0;

    @Nullable
    private ContainerChest openContainer; // not used for functionality




    public GetMapVisitor(ChestSortProcess parent){//, Set<ChestSortProcess.UniqueChest> toSort, Map<ChestSortProcess.UniqueChest, List<ItemStack>> chestData) {
        super(parent);
       // this.chestsToSort = ImmutableSet.copyOf(toSort);
        //this.chestData = chestData;
        //this.howToSort = ImmutableBiMap.copyOf(howToSortChests(chestData));
        this.currentlyMoving = nextTarget(this.howToSort, Collections.emptySet()).map(pair -> new Tuple<>(pair, ChestSortProcess.SortingChestVisitor.SortState.FETCHING)).orElse(null);
        super.currentTarget = currentlyMoving != null ? currentlyMoving.getFirst().from.chest : null;
    }


    private enum SortState {
        FETCHING,
        MOVING
    }

    public boolean finished() { // TODO don't create new hashsets from the values
        //return Sets.difference(Sets.newHashSet(howToSort.values()), sortedSlots).isEmpty();
        return false; // TODO
    }

    @Override
    public boolean onContainerOpened(ContainerChest container, List<ItemStack> itemStacks) {
        this.openContainer = container;
        return true;
    }

    @Override
    public void onContainerClosed(int windowId) {
    }


    @Override
    public boolean containerOpenTick(ContainerChest container) {
        //if (this.openContainer == null) throw new IllegalStateException();

                // from chest to inv
                pickupClick(currentlyMoving.getFirst().from.slot, parent.ctx);
                pickupClick(getInvSlotIndex(WORKING_SLOT, container), parent.ctx);

                this.currentlyMoving = new Tuple<>(this.currentlyMoving.getFirst(), ChestSortProcess.SortingChestVisitor.SortState.MOVING); // change the state
                this.currentTarget = this.currentlyMoving.getFirst().to.chest;

                //this.openContainer = null; // close chest
                return false;

    }

    private static int getInvSlotIndex(int slot, ContainerChest chest) {
        return chest.getLowerChestInventory().getSizeInventory() + slot;
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
