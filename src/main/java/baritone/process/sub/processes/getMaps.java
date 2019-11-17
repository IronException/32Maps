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
 * this process is for getting new maps.. so it is filling up the inventory with maps in a shulker and then continues with the next process
 * Maybe from the chest it could look for the first slot with a shulker... also it shouldnt put the shulker back...
 */
public class getMaps extends OneTimeCommand {

    public getMaps(BlockPos chestCoords, SlotHelper hotbarSlot, BlockPos placeShulker, SubProcess nextProcess) {
        // TODO take the first shulker in the chest not the first slot. =>
        super(new EditChestShulkerProcess(chestCoords, hotbarSlot, placeShulker, new SwapSlots(), false, nextProcess));
    }

    @Override
    public void doTick() {

    }
}
