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


import baritone.process.sub.processes.helper.SlotHelper;
import net.minecraft.util.math.BlockPos;

/**
 * open chest, takes shulker out, places it and does whatever you tell him in shulker, then puts shulker back
 */

public class EditChestShulkerProcess extends ForwardProcess {

    protected static SubProcess getSwapInChest(BlockPos chestCoords, SlotHelper chestSlot, SlotHelper hotbarSlot) {
        return new DoInContainerProcess(chestCoords,
                new SwapSlot(chestSlot, hotbarSlot, new Epsilon()),
                new Epsilon());
    }

    protected static SubProcess getProcess(BlockPos chestCoords, SlotHelper chestSlot, SlotHelper hotbarSlot, BlockPos placeShulker, SubProcess doInShulker, boolean putBack, SubProcess nextProcess) {
        SubProcess[] rV = new SubProcess[]{
                getSwapInChest(chestCoords, chestSlot, hotbarSlot),
                new ChatProcess("now what?", new Epsilon()),
                new EditShulkerProcess(placeShulker, hotbarSlot, doInShulker, new Epsilon()),
                // TODO pick up the dropped item...
                getSwapInChest(chestCoords, chestSlot, hotbarSlot),
                nextProcess
        };
        if(!putBack)
            rV[3] = new Epsilon();
        return new MultiProcess(rV);
    }

    /**
     * puts the shulker back ovsly. I mean why wouldnt you?
     * @param chestCoords
     * @param chestSlot
     * @param hotbarSlot
     * @param placeShulker
     * @param doInShulker
     * @param nextProcess
     */
    public EditChestShulkerProcess(BlockPos chestCoords, SlotHelper chestSlot, SlotHelper hotbarSlot, BlockPos placeShulker, SubProcess doInShulker, SubProcess nextProcess) {
        this(chestCoords, chestSlot, hotbarSlot, placeShulker, doInShulker, true, nextProcess);
    }

    public EditChestShulkerProcess(BlockPos chestCoords, SlotHelper chestSlot, SlotHelper hotbarSlot, BlockPos placeShulker, SubProcess doInShulker, boolean putBack, SubProcess nextProcess) {
        super(getProcess(chestCoords, chestSlot, hotbarSlot, placeShulker, doInShulker, putBack, nextProcess));
    }

}
