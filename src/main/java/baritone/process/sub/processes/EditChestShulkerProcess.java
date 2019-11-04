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

public class EditChestShulkerProcess extends ForwardProcess {

    protected static SubProcess getProcess(BlockPos chestCoords, SubProcess doInChest) {
        return doInChest(swapSlots()), EditShulkerProcesss(place shulk, doInContainer(what todo), break shulk), doInChest(swapSlots back);
    }

    public EditChestShulkerProcess(BlockPos chestCoords, SubProcess doInChest) {
        super(getProcess(chestCoords, doInChest);
    }

/*
putShulkerProcess(chestCoords, chestSlot, hotbar, shulkerCoords, invSlot, shulkerSlot, bool putBack):
-> goto chestCoords
-> openChest
-> swapSlots(chestSlot, hotbar)
-> closeChest
-> goto shulkerCoords
-> place shulker
-> openShulker
-> swapSlot(invSlot, shulkerSlot)
-> closeShulker
-> breakShulker
if putBack:
-> pick up
-> goto chestCoords
-> open Chest
-> swapSlot(hotbar, chestSlot)
-> closeChest*/

}
