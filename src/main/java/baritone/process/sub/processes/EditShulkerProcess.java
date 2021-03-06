
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


import baritone.process.sub.processes.helper.SlotConverter;
import net.minecraft.util.math.BlockPos;

/**
 * places a shulker and opens it to do what you tell him
 */

public class EditShulkerProcess extends ForwardProcess {

    protected static SubProcess getProcess(BlockPos placeCoords, SlotConverter hotbarSlot, SubProcess doInShulker, boolean pickUpShulker, SubProcess nextProcess) {
        return new PlaceBlock(placeCoords, hotbarSlot,
                new DoInContainerProcess(placeCoords, doInShulker,
                        new BreakBlock(placeCoords, pickUpShulker,
                                nextProcess)));
    }

    public EditShulkerProcess(BlockPos placeCoords, SlotConverter hotbarSlot, SubProcess doInShulker, boolean pickUpShulker, SubProcess nextProcess) {
        super(getProcess(placeCoords, hotbarSlot, doInShulker, pickUpShulker, nextProcess));
    }

}
