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
    private static final int WORKING_SLOT = 0;

    @Nullable
    private ContainerChest openContainer; // not used for functionality




    public GetMapVisitor(ChestSortProcess parent){//, Set<ChestSortProcess.UniqueChest> toSort, Map<ChestSortProcess.UniqueChest, List<ItemStack>> chestData) {
        super(parent);
/*        this.chestsToSort = ImmutableSet.copyOf(toSort);
        this.chestData = chestData;
        this.howToSort = ImmutableBiMap.copyOf(howToSortChests(chestData));
        this.currentlyMoving = nextTarget(this.howToSort, Collections.emptySet()).map(pair -> new Tuple<>(pair, ChestSortProcess.SortingChestVisitor.SortState.FETCHING)).orElse(null);
        super.currentTarget = currentlyMoving != null ? currentlyMoving.getFirst().from.chest : null;*/
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

/*
    private static Optional<ChestSortProcess.SortingChestVisitor.StackLocationPair> nextTarget(BiMap<ChestSortProcess.StackLocation, ChestSortProcess.StackLocation> howToSort, Set<ChestSortProcess.StackLocation> sortedLocations) {
        return howToSort.entrySet().stream()
                .filter(entry  -> !sortedLocations.contains(entry.getValue()))
                .map(entry -> new ChestSortProcess.SortingChestVisitor.StackLocationPair(entry.getKey(), entry.getValue()))
                .findAny();
    }
*/
    private static int getInvSlotIndex(int slot, ContainerChest chest) {
        return chest.getLowerChestInventory().getSizeInventory() + slot;
    }

    private static ItemStack pickupClick(int slotId, IPlayerContext ctx) {
        return ctx.playerController().windowClick(ctx.player().openContainer.windowId, slotId, 0, ClickType.PICKUP, ctx.player());
    }

    /*
    // given all the chest data, return a map that says where to move chest slots to
    // number = slot slot
    private static BiMap<ChestSortProcess.StackLocation, ChestSortProcess.StackLocation> howToSortChests(final Map<ChestSortProcess.UniqueChest, List<ItemStack>> chestData) {
        final List<List<ItemStack>> sortedChestList = sortChestData(chestData.values()); // paritioned into groups of no more than size of double chest (need to allow single chests later)
        final Set<ChestSortProcess.UniqueChest> chests = Collections.unmodifiableSet(chestData.keySet());
        if (sortedChestList.size() > chests.size()) throw new IllegalStateException(sortedChestList.size() + " - " + chests.size());

        final List<Tuple<ChestSortProcess.UniqueChest, List<ItemStack>>> pairs =
                combine(
                        sortedChestList.iterator(), chests.iterator(),
                        (stack, chest) -> new Tuple<>(chest, stack)
                ); // TODO: don't choose random chests

        final Map<ChestSortProcess.UniqueChest, List<ItemStack>> sortedChestState = pairs.stream() // how we want the state of the chests to be
                .collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));

        return conversion(chestData, sortedChestState);
    }
*/

    /*

    private static BiMap<ChestSortProcess.StackLocation, ChestSortProcess.StackLocation> conversion(Map<ChestSortProcess.UniqueChest, List<ItemStack>> unsorted, Map<ChestSortProcess.UniqueChest, List<ItemStack>> sorted) {
        // set of slots that some stack has been decided to be moved to
        final Set<ChestSortProcess.StackLocation> reservedSlots = new HashSet<>();
        final BiMap<ChestSortProcess.StackLocation, ChestSortProcess.StackLocation> out = HashBiMap.create();

        unsorted.forEach((chestA, stacksFrom) -> {
            for (int i = 0; i < stacksFrom.size(); i++) {
                final int indexFrom = i; // dumb final meme

                final ItemStack stack = stacksFrom.get(indexFrom);
                if (stack.isEmpty()) continue;

                sorted.forEach((chestB, stacksTo) -> {
                    indexMatching(stacksTo, (stackSorted, idx) -> !reservedSlots.contains(new ChestSortProcess.StackLocation(chestB, idx)) && ItemStack.areItemStacksEqual(stack, stackSorted))
                            .ifPresent(indexTo -> {
                                final ChestSortProcess.StackLocation from = new ChestSortProcess.StackLocation(chestA, indexFrom);
                                final ChestSortProcess.StackLocation to = new ChestSortProcess.StackLocation(chestB, indexTo);

                                reservedSlots.add(to);
                                out.put(from, to);
                            });
                });
            }
        });

        return out;
    }
*/
    private static <T> OptionalInt indexMatching(List<T> list, BiPredicate<T, Integer> predicate) {
        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i), i)) return OptionalInt.of(i);
        }
        return OptionalInt.empty();
    }

        /*private static <T> Stream<Tuple<T, Integer>> streamWithIndex(List<T> list) {

        }*/

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
/*
    // TODO: dont partition here
    private static List<List<ItemStack>> sortChestData(Collection<List<ItemStack>> values) {
        List<ItemStack> sorted = values.stream()
                .flatMap(List::stream)
                .sorted(ChestSortProcess.ItemSorter::compare)
                .collect(Collectors.toList());
        System.out.println(sorted);
        final int CHEST_SIZE = 9 * 6; // TODO: dont assume double chests

        return Lists.partition(sorted, CHEST_SIZE); // guava is based
    }

    private static final class StackLocationPair {
        public final ChestSortProcess.StackLocation from;
        public final ChestSortProcess.StackLocation to;

        StackLocationPair(ChestSortProcess.StackLocation from, ChestSortProcess.StackLocation to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChestSortProcess.SortingChestVisitor.StackLocationPair that = (ChestSortProcess.SortingChestVisitor.StackLocationPair) o;
            return from.equals(that.from) &&
                    to.equals(that.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }*/
}