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


import baritone.api.process.PathingCommand;
import baritone.process.ChestSortProcess;

public class EditChestShulkerProcess extends SubProcess {

    public EditChestShulkerProcess(BlockPos chestCoords, SubProcess doInChest) {
        super(
new OpenChest(chestCoords,
doInChest));

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

    @Override
    public boolean finished() {
        return true; // next process will be executed
    }

    @Override
    public void tick() {
// dont need to do anything. its all in nextProcess
    }

}
