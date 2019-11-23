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
 * this process is for getting new maps.. so it is filling up the inventory with maps in a shulker and then continues with the next process
 * Maybe from the chest it could look for the first slot with a shulker... also it shouldnt put the shulker back...
 */
public class GetMaps extends OneTimeCommand {

    public GetMaps(BlockPos chestCoords, SlotConverter hotbarSlot, BlockPos placeShulker, BlockPos trashCoords, SubProcess nextProcess) {
        // TODO take the first shulker in the chest not the first slot. =>
        super(new MultiProcess(new SubProcess[]{ new EditChestShulkerProcess(chestCoords, hotbarSlot, placeShulker, new SwapSlots(), false,
                new PickUpItem(Item.getItemFromBlock(Blocks.PURPLE_SHULKER_BOX), placeShulker, new Epsilon())),
        new DoInContainerProcess(trashCoords,
                new SwapSlot(new AbstractSlot(Item.getItemFromBlock(Blocks.AIR), new SlotConverter(0, ContainerType.NORMAL_CHEST), true),
                        new AbstractSlot(Item.getItemFromBlock(Blocks.PURPLE_SHULKER_BOX), new SlotConverter(0, ContainerType.HOTBAR), true), new Epsilon()),
                nextProcess)}));

    }

    @Override
    public void doTick() {

    }
}
