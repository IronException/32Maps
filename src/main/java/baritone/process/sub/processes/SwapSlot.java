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
import baritone.process.sub.processes.helper.SlotConverter;
import net.minecraft.inventory.ClickType;

/**
 * you need to have your inventory open for this to work
 */
public class SwapSlot extends SubProcess {

    protected int phase;
    protected SlotConverter slot1, slot2;

    public SwapSlot(SlotConverter slot1, SlotConverter slot2, SubProcess nextProcess) {
        super(nextProcess);

        this.phase = 0;

        this.slot1 = slot1;
        this.slot2 = slot2;
    }

    /**
     * we are done after phase 2
     */
    @Override
    public boolean isFinished() {
        return this.phase > 4; // 2 ticks extra just in case
    }

    @Override
    public void doTick() {
        switch (phase) {
            case 0:
            case 2:
                this.pressSlot(this.slot1);
                break;
            case 1:
                this.pressSlot(this.slot2);
                if (ChestSortProcess.debug) {
                    logDirect("swapping slots:");
                    logDirect(this.slot1 + "");
                    logDirect(this.slot2 + "");

                    logDirect("");
                }

                break;
        }
        phase++;
    }

    protected void pressSlot(SlotConverter slot) {
        this.pressSlot(slot.getSlotNow());
    }

    protected void pressSlot(int slot) {
        super.ctx.playerController().windowClick(super.ctx.player().openContainer.windowId, slot, 0, ClickType.PICKUP, super.ctx.player());
    }

}
