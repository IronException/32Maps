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

import net.minecraft.inventory.ClickType;

/**
 * you need to have your inventory open for this to work
 */
public class SwapSlot extends SubProcess {
  
  public static final int HOTBAR = 0;
  public static final int INVENTORY = 1;
  public static final int NORMAL_CHEST = 2;
  public static final int DOUBLE_CHEST = 3;


  protected int phase;
  protected int slot1, slot2;
  
  public SwapSlot(int type1, int slot1, int type2, int slot2, int typeIn, SubProcess nextProcess) {
    this(getAsSlotIn(type1, slot1, typeIn), getAsSlotIn(type2, slot2, typeIn), nextProcess);
  }

  public SwapSlot(int slot1, int slot2, SubProcess nextProcess) {
    super(nextProcess);
    
    this.phase = 0;
    
    this.slot1 = slot1;
    this.slot2 = slot2;
  }

  /**
   * typeAs before slot might confuse things but maybe its also easier to read?
   */
  public static int getAsSlotIn(int typeAs, int slot, int typeIn) {
    return (typeIn - typeAs) * 27 + slot;
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

  protected void pressSlot(int slot) {
    super.ctx.playerController().windowClick(super.ctx.player().openContainer.windowId, slot, 0, ClickType.PICKUP, super.ctx.player());
  }
  
}
