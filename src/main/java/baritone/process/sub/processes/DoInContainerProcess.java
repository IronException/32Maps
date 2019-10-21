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

import net.minecraft.util.math.BlockPos;

public class DoInContainerProcess extends SubProcess {

    /**
    // IMPORTANT: this opens and closes the chest and only then finishes
     also for understanding: this class is basically only closing the chest again
    */
    public DoInContainerProcess(BlockPos chestCoords, SubProcess doInChest) {
        super(new OpenContainerProcess(chestCoords, doInChest));

        // only finishes when chest is closed.
    }



    @Override
    public boolean isFinished() {
        return true;
    }

    /**
     * this might complicate things but I cant think of an easier way. We first do the next processes (cuz opening is there too...)
     * and this process is rather to close the chest again... So yea tick() has to be overwritten...
     *
     * so basically this class is only closing the chest lol
     *
     */
    public void tick() {
        if(nextProcess.isFinished())
            doTick();
        else
            nextProcess.tick();
    }

    @Override
    public void doTick() {

    }



    // TODO is getReturn a problem || has to be overwritten? because it calles isFinished...


}
