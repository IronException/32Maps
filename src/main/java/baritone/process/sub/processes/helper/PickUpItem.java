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

package baritone.process.sub.processes.helper;

import baritone.process.sub.processes.SubProcess;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class PickUpItem extends SubProcess {
    public PickUpItem(BlockPos nearGoal, Item itemWeBreak, SubProcess nextProcess) {
        super(nextProcess);
        // TODO need ro move this
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void doTick() {

    }
    
    public static List<BlockPos> droppedItemsScan(List<Block> mining, World world) {
        if (!Baritone.settings().mineScanDroppedItems.value) {
            return Collections.emptyList();
        }
        Set<Item> searchingFor = new HashSet<>();
        for (Block block : mining) {
            Item drop = block.getItemDropped(block.getDefaultState(), new Random(), 0);
            Item ore = Item.getItemFromBlock(block);
            searchingFor.add(drop);
            searchingFor.add(ore);
        }
        List<BlockPos> ret = new ArrayList<>();
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                EntityItem ei = (EntityItem) entity;
                if (searchingFor.contains(ei.getItem().getItem())) {
                    ret.add(new BlockPos(entity));
                }
            }
        }
        return ret;
    }
}
