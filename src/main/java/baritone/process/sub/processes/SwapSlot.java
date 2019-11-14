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
import baritone.process.sub.processes.helper.SlotHelper;
import net.minecraft.inventory.ClickType;

/**
 * you need to have your inventory open for this to work
 */
public class SwapSlot extends SubProcess {

  protected int phase;
  protected SlotHelper slot1, slot2;

  public SwapSlot(SlotHelper slot1, SlotHelper slot2, SubProcess nextProcess) {
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
    return this.phase > 2;
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
        break;
    }
    phase ++;
  }

  protected void pressSlot(SlotHelper slot) {
    this.pressSlot(slot.getSlotNow());
  }

  protected void pressSlot(int slot) {
    super.ctx.playerController().windowClick(super.ctx.player().openContainer.windowId, slot, 0, ClickType.PICKUP, super.ctx.player());
  }
  
}
