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

public class AbstractSlot extends SlotConverter {

    Item item;
    // slot and as from super are there for when the slot is found

    SlotConverter startSearch;
    boolean topBottom;

    public AbstractSlot(Item item, SlotConverter startSearching, boolean topToBottom) {
        super(0, null);
        this.item = item;
        this.startSearch = startSearching;
        this.topBottom = topToBottom;
    }


    protected boolean isTheOne(int i) {
        return ChestHelper.hasSlotItem(new SlotConverter(i, ChestHelper.getContainer()), this.item);
    }

    @Override
    public int getSlotIn(ContainerType in) {
        if(as == null){
            if (topBottom) {
                for (int i = startSearch.getSlotIn(in); i < ChestSortProcess.INSTANCE.ctx.player().openContainer.getInventory().size(); i++)
                    if (isTheOne(i)) {
                        saveNewSlot(i);
                        if(ChestSortProcess.debug)
                            ChestSortProcess.INSTANCE.logDirect("got slot: " + i + " and converted to " + this.toString());
                        return super.getSlotIn(in);
                    }
                for (int i = 0; i < startSearch.getSlotIn(in); i++)
                    if (isTheOne(i)) {
                        saveNewSlot(i);
                        return super.getSlotIn(in);
                    }
            } else {
                for (int i = startSearch.getSlotIn(in); i >= 0; i--)
                    if (isTheOne(i)) {
                        saveNewSlot(i);
                        return super.getSlotIn(in);
                    }
                for (int i = ChestSortProcess.INSTANCE.ctx.player().openContainer.getInventory().size() - 1; i > startSearch.getSlotIn(in); i--)
                    if (isTheOne(i)) {
                        saveNewSlot(i);
                        return super.getSlotIn(in);
                    }
            }
            ChestSortProcess.INSTANCE.logDirect("WARNING: terminated search for abstract slot... (" + item);
        }
        return super.getSlotIn(in);
    }

    public void saveNewSlot(int slot){
        // TODO we assume that every inv is 27 slots big for simplicity. If this whole program would be reused for something else special cases like hoppers also have to be treaded differently by considering ChestHelper.getContainer()

        ChestSortProcess.INSTANCE.logDirect("got to save slot " + slot);


        if(ChestHelper.getContainer() == ContainerType.INVENTORY) {

            slot -= 9;// this is so wrong but somehow sometimes minecraft inv is counted and sometimes not? (I think and hope I think so not all code is broken...) <- armorslots + crafting slots + 2nd hand are also in there smh..
            ChestSortProcess.INSTANCE.logDirect("we are in an inv with subslot " + (slot % 27));
        }
        super.slot = slot % 27;
        switch (slot / 27){
            case 0:
                super.as = ChestHelper.getContainer();
                break;
            case 1:
                if(ChestHelper.getContainer() == ContainerType.INVENTORY) {
                    super.as = ContainerType.HOTBAR;
                } else {
                    // TODO there are ofc more cases now but I hope just the case that we are in a normal chest is enough...
                    super.as = ContainerType.INVENTORY;
                }
                break;
            case 2:
                if(ChestHelper.getContainer() == ContainerType.NORMAL_CHEST) {
                    super.as = ContainerType.HOTBAR;
                } else {
                    super.as = ContainerType.INVENTORY;
                }
                break;

        }
    }


    @Override
    public String toString() {
        return "AbstractSlot: " + this.slot + " (" + super.getSlotNow() + ") (for " + this.item + ")";
    }
}
