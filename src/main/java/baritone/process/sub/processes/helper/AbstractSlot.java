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

    SlotHelper startSearch;
    boolean topBottom;

    public AbstractSlot(Item item, SlotHelper startSearching, boolean topToBottom) {
        super(0, null);
        this.item = item;
        this.startSearch = startSearching;
        this.topBottom = topToBottom;
    }


    protected boolean isTheOne(int i) {
        return ChestSortProcess.INSTANCE.ctx.player().openContainer.getSlot(i).getStack().getItem().equals(this.item);
    }

    @Override
    public int getSlotIn(ContainerType in) {
        if(slot == null)
            if (topBottom) {
                for (int i = startSearch.getSlotIn(in); i < ChestSortProcess.INSTANCE.ctx.player().openContainer.getInventory().size(); i++)
                    if (isTheOne(i)) {
                        this.slot = i;
                        return this.slot;
                    }
                for (int i = 0; i < startSearch.getSlotIn(in); i++)
                    if (isTheOne(i)) {
                        this.slot = i;
                        return this.slot;
                    }
            } else {
                for (int i = startSearch.getSlotIn(in); i >= 0; i--)
                    if (isTheOne(i)) {
                        this.slot = i;
                        return this.slot;
                    }
                for (int i = ChestSortProcess.INSTANCE.ctx.player().openContainer.getInventory().size() - 1; i > startSearch.getSlotIn(in); i--)
                    if (isTheOne(i)) {
                        this.slot = i;
                        return this.slot;
                    }
            }
        return this.slot;
    }
}
