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

package baritone.process.sub.processes.helper;

import baritone.process.ChestSortProcess;
import net.minecraft.item.Item;

/**
 * when getting the slot once it stays as that...
 *
 */

public class AbstractSlot extends SlotHelper {

    Item item;
    Integer slot;

    public AbstractSlot(Item item) {
        super(0, null);
        this.item = item;
    }

    @Override
    public int getSlotIn(ContainerType in) {
        if(slot == null)
            for (int i = 0; i < ChestSortProcess.INSTANCE.ctx.player().openContainer.getInventory().size(); i ++)
                if(ChestSortProcess.INSTANCE.ctx.player().openContainer.getSlot(i).getStack().getItem().equals(this.item)) {
                    this.slot = i;
                    return this.slot;
                }
        return this.slot;
    }
}
