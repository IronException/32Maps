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
import baritone.process.sub.processes.helper.ContainerType;
import baritone.process.sub.processes.helper.SlotConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;


/**
 *
 * should go to the right chest / shulker / slot to put the map there... if the map is already there... Idk prly drop it? then? at least msg me
 *
 */
public class PutMap extends OneTimeCommand {


    protected static SubProcess getProcess(SlotConverter mapSlot, BlockPos putMapCoords, Vec3d putMapLocs, SlotConverter hotbarSlot, Vec3i relativeShulkerPos, SubProcess nextProcess){
        int mapId = ChestHelper.convertMapId(mapSlot);
        if(ChestSortProcess.debug) {
            ChestSortProcess.INSTANCE.logDirect("calculated " + mapId + " at slot " + mapSlot + " to:");
            ChestSortProcess.INSTANCE.logDirect(calculateChestCoords(mapId) + " / " + calculateChestSlot(mapId) + " / " + calculateShulkerSlot(mapId));
            ChestSortProcess.INSTANCE.logDirect("");
        }
        putMapLocs = putMapLocs.scale(calculateChestCoords(mapId));
        return new EditChestShulkerProcess(putMapCoords.add(putMapLocs.x, putMapLocs.y, putMapLocs.z),
                new SlotConverter(calculateChestSlot(mapId), ContainerType.NORMAL_CHEST),
                hotbarSlot, putMapCoords.add(relativeShulkerPos),
                new SwapSlot(mapSlot, new SlotConverter(calculateShulkerSlot(mapId), ContainerType.NORMAL_CHEST), new Epsilon()),
                true, nextProcess);
    }

    public PutMap(SlotConverter mapSlot, BlockPos putMapCoords, Vec3d putMapLocs, SlotConverter hotbarSlot, Vec3i relativeShulkerPos, SubProcess nextProcess) {
        super(getProcess(mapSlot, putMapCoords, putMapLocs, hotbarSlot, relativeShulkerPos, nextProcess));
    }

    private static int calculateChestCoords(int mapId) {
        return mapId / (27 * 27);
    }

    private static int calculateChestSlot(int mapId) {
        return mapId / 27;
    }

    private static int calculateShulkerSlot(int mapId) {
        return mapId % 27;
    }

    @Override
    public void doTick() {

    }
}
