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
import baritone.process.sub.processes.helper.ContainerType;
import baritone.process.sub.processes.helper.SlotConverter;

/**
 * you need to have your inventory open for this to work
 */
public class SwapSlots extends SubProcess {
  
  protected static MultiProcess getNextProcess() {
    SubProcess[] rV = new SubProcess[27];
    SubProcess next = new Epsilon();
    for(int i = 0; i < rV.length; i ++) {
      if(ChestSortProcess.INSTANCE.debug)
        next = new ChatProcess("debugging: swapped slot " + i, new Epsilon());
      rV[i] = new SwapSlot(new SlotConverter(i, ContainerType.NORMAL_CHEST), new SlotConverter(i, ContainerType.INVENTORY), next);
    }
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
