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


import baritone.process.sub.processes.helper.AbstractSlot;
import baritone.process.sub.processes.helper.ContainerType;
import baritone.process.sub.processes.helper.SlotConverter;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

/**
 * open chest, takes shulker out, places it and does whatever you tell him in shulker, then puts shulker back
 */

public class EditChestShulkerProcess extends ForwardProcess {

    protected static SubProcess getSwapInChest(BlockPos chestCoords, SlotConverter chestSlot, SlotConverter hotbarSlot, boolean putBack) {
        return new DoInContainerProcess(chestCoords,
                new SwapSlot(chestSlot, hotbarSlot, putBack, new Epsilon()),
                new Epsilon());
    }

    protected static SubProcess getProcess(BlockPos chestCoords, SlotConverter chestSlot, SlotConverter hotbarSlot, BlockPos placeShulker, SubProcess doInShulker, boolean putBack, SubProcess nextProcess) {
        SubProcess[] rV = new SubProcess[]{
                getSwapInChest(chestCoords, chestSlot, hotbarSlot, putBack), // TODO is the last putBack right here? (it is for not picking up an additional shulker if there is a flow into the chest)
                new EditShulkerProcess(placeShulker, hotbarSlot, doInShulker, putBack, new Epsilon()),
                getSwapInChest(chestCoords, chestSlot, new AbstractSlot(Item.getItemFromBlock(Blocks.PURPLE_SHULKER_BOX), new SlotConverter(0, ContainerType.HOTBAR), true), true), // item could have also went somewhere else in inventory
                nextProcess
        };
        if(!putBack)
            rV[2] = new Epsilon();
        return new MultiProcess(rV);
    }

    /**
     * puts the shulker back ovsly. I mean why wouldnt you?
     * @param chestCoords
     * @param hotbarSlot
     * @param placeShulker
     * @param doInShulker
     * @param nextProcess
     */
    public EditChestShulkerProcess(BlockPos chestCoords, SlotConverter hotbarSlot, BlockPos placeShulker, SubProcess doInShulker, boolean putBack, SubProcess nextProcess) {
        this(chestCoords, new AbstractSlot(Item.getItemFromBlock(Blocks.PURPLE_SHULKER_BOX), new SlotConverter(0, ContainerType.NORMAL_CHEST), true), hotbarSlot, placeShulker, doInShulker, putBack, nextProcess);
    }

    public EditChestShulkerProcess(BlockPos chestCoords, SlotConverter chestSlot, SlotConverter hotbarSlot, BlockPos placeShulker, SubProcess doInShulker, boolean putBack, SubProcess nextProcess) {
        super(getProcess(chestCoords, chestSlot, hotbarSlot, placeShulker, doInShulker, putBack, nextProcess));
    }

}
