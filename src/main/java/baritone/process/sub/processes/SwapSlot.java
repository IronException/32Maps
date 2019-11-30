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

import baritone.process.ChestSortProcess;
import baritone.process.sub.processes.helper.ChestHelper;
import baritone.process.sub.processes.helper.SlotConverter;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;

/**
 * you need to have your inventory open for this to work
 */
public class SwapSlot extends SubProcess {

    protected int phase;
    protected SlotConverter slot1, slot2;
    protected boolean putBack;

    protected int pauseTicks;

    public SwapSlot(SlotConverter slot1, SlotConverter slot2, SubProcess nextProcess) {
        this(slot1, slot2, true, nextProcess);
    }

    public SwapSlot(SlotConverter slot1, SlotConverter slot2, boolean putBack, SubProcess nextProcess) {
        super(nextProcess);

        this.phase = 0;

        this.slot1 = slot1;
        this.slot2 = slot2;

        this.putBack = putBack;

        this.pauseTicks = ChestSortProcess.INSTANCE.pauseTicks;
    }

    /**
     * we are done after phase 2
     */
    @Override
    public boolean isFinished() {
        return this.phase > pauseTicks * 5; // 2 ticks extra just in case
    }

    @Override
    public void doTick() {
        /**
         * find out what slot1 and slot2 is:
         * when having a shulker it behaves weird and also for when there is no item / an item when you put it back..
         * what do if you have a shulker?
         *
         */


        if(phase == pauseTicks * 1){
            this.pressSlot(this.slot1);
        } else if(phase == pauseTicks * 2) {
            this.pressSlot(this.slot2);
        } else if(phase == pauseTicks * 3) {
            if(this.putBack) {
                this.pressSlot(this.slot1);
            } else if(ChestSortProcess.INSTANCE.debug) {
                logDirect("didnt put back. as wished");
            }
            // when having hasItem(this.slot1) is within the code it could still accidently drop an itrem but its your fault when in your inv. When there is a circumstance that has to be treaten differently an option could also be added
            // but I have introduced a seperate variable that might make it easier...
        }

        phase++;
    }

    private boolean hasItem(SlotConverter slot) {
        return !ChestHelper.hasSlotItem(slot, Item.getItemFromBlock(Blocks.AIR));
    }

    protected void pressSlot(SlotConverter slot) {
        this.pressSlot(slot.getSlotNow());
    }

    protected void pressSlot(int slot) {
        super.ctx.playerController().windowClick(super.ctx.player().openContainer.windowId, slot, 0, ClickType.PICKUP, super.ctx.player());
    }

}
