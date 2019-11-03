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
public class SwapSlots extends SubProcess {
  
  protected static MultiProcess getNextProcess() {
    SubProcess[] rV = new SubProcess[27];
    for(int i = 0; i < rV.length; i ++)
      rV[i] = new SwapSlot(SwapSlot.NORMAL_CHEST, i, SwapSlot.INVENTORY, i, SwapSlot.NORMAL_CHEST, new Epsilon());
    return new MultiProcess(rV);
  }
  
  public SwapSlots() {
    super(getNextProcess());
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public void doTick() {

  }
}
