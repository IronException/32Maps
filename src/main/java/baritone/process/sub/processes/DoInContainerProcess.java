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

import baritone.process.sub.processes.helper.ChestHelper;
import net.minecraft.util.math.BlockPos;

public class DoInContainerProcess extends LookProcess {

    /**
    // IMPORTANT: this opens and closes the chest and only then finishes
     also for understanding: this class is basically only closing the chest again
    */
    public DoInContainerProcess(BlockPos chestCoords, SubProcess doInChest, SubProcess nextProcess) {
        super(chestCoords, 2, new MultiProcess(new SubProcess[]{
                new OpenContainerProcess(chestCoords, doInChest),
                new SubProcess(nextProcess) {

                    @Override
                    public boolean isFinished() {
                        return !ChestHelper.isChestOpen();
                    }

                    @Override
                    public void doTick() {
                        if(!isFinished())
                          ctx.player().closeScreenAndDropStack();
                    }
                }

        }));

        // only finishes when chest is closed.
    }


}
