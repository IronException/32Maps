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
import net.minecraft.util.math.Vec3i;

/**
 *
 * should go to the right chest / shulker / slot to put the map there... if the map is already there... Idk prly drop it? then? at least msg me
 *
 */
public class putMap extends SubProcess {

    public putMap(BlockPos putMapCoords, Vec3i putMapLocs, Vec3i relativeShulkerPos, SubProcess nextProcess) {
        super(nextProcess);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void doTick() {

    }
}
