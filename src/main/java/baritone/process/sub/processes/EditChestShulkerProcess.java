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


import net.minecraft.util.math.BlockPos;

/**
 * open chest, takes shulker out, places it and does whatever you tell him in shulker, then puts shulker back
 */

public class EditChestShulkerProcess extends ForwardProcess {

    protected static SubProcess getProcess(BlockPos chestCoords, int chestSlot, int hotbarSlot, BlockPos placeShulker, SubProcess doInShulker) {
        SubProcess swapInChest = new DoInContainer(chestCoords, 
                                  new SwapSlot(chestSlot, hotbarSlot, new Epsilon()),// TODO hotbarSlot in swapslot != editShulkerProcess
                                  new Epsilon());
        return new MultiProcess(new SubProcess[]{
                swapInChest,
                new EditShulkerProcess(placeShulker, hotbarSlot, doInShulker)),
                swapInChest
               });
    }

    public EditChestShulkerProcess(BlockPos chestCoords, int chestSlot, int hotbarSlot, BlockPos placeShulker, SubProcess doInShulker) {
        super(getProcess(chestCoords, chestSlot, hotbarSlot, placeShulker, doInShulker);
    }

}
