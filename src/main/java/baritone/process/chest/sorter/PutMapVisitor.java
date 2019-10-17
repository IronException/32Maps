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

package baritone.process.chest.sorter;

import baritone.api.utils.IPlayerContext;
import baritone.api.utils.input.Input;
import baritone.process.ChestSortProcess;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

public class PutMapVisitor extends ChestSortProcess.ChestVisitor {

    private int workingSlot;
    private MapHome mapHome;

    private int phase;

/*
phases:
0: goto chest + get shulker
1: place shulker where it belongs
2: get map in
3: break shulker
4: put shulker back
5: next PutMapVisitor

*/

    public static PutMapVisitor getPutMapVisitor(ChestSortProcess parent){
        IPlayerContext ctx = parent.ctx;

        for (int i = 0; i < ctx.player().inventoryContainer.inventorySlots.size(); i ++) {
            if (ctx.player().inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemMap)
                return new PutMapVisitor(parent, calculateCoords(ctx.player().inventoryContainer.getSlot(i).getStack().getMetadata()), i);
        }
        return null;
    }


    private static MapHome calculateCoords(int id) {// 27 * 27 = 729
        return new MapHome(ChestSortProcess.putMaps.add(id * ChestSortProcess.addX / 729, 0, id * ChestSortProcess.addZ / 729), (id % 729) / 27, id % 27, id);
    }



    public PutMapVisitor(ChestSortProcess parent, MapHome mapHome, int workingSlot){//, Set<ChestSortProcess.UniqueChest> toSort, Map<ChestSortProcess.UniqueChest, List<ItemStack>> chestData) {
        super(parent);
        this.phase = 0;

        this.workingSlot = workingSlot;
        this.mapHome = mapHome;
    }


    public boolean finished() {
        return false; // TODO
    }

    @Override
    public boolean openChest() {
        switch(this.phase){
            case 0:
                return true;
            case 1:
                return false;

        }
        return true;
    }

    public void tickExChest() {
// TODO
        // place shulker and then open it
        parent.baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
        parent.logDirect("SOMEHOW NOT BILDIN");
        phase = 2;
    }

    public String getDebug(){
        return phase + "";
    }

    @Override
    public boolean onContainerOpened(Container container) {
        return true;
    }

    @Override
    public void onContainerClosed(int windowId) {
    }



    @Override
    public boolean containerOpenTick(Container container) {
        switch(this.phase) {
            case 0:
                pickupClick(this.mapHome.chestSlot, parent.ctx);
                pickupClick(getInvSlotIndex(27 + ChestSortProcess.hotbarSlot, container), parent.ctx);

                // put the item back that could be in you hand now (player inv might not be empty)
                pickupClick(this.mapHome.chestSlot, parent.ctx);

                phase = 1;
                return false;
            case 2:
                pickupClick(getInvSlotIndex(workingSlot, container), parent.ctx);
                pickupClick(this.mapHome.shulkerSlot, parent.ctx);

                // put the item back that could be in you hand now (player inv might not be empty)
                pickupClick(getInvSlotIndex(workingSlot, container), parent.ctx);

                phase = 3;
                return false;

        }

        return false;
    }

    @Override
    public BlockPos getGoalPos() {
        switch(this.phase){
            case 0:
                return mapHome.chestCoords;
            case 1:
            case 2:
                return ChestSortProcess.shulkerPos;
        }
        return null;
    }

    @Override
    public ChestSortProcess.ChestVisitor getNextVisitor(){
        return PutMapVisitor.getPutMapVisitor(parent);
    }

    private static int getInvSlotIndex(int slot, Container chest) {

        if (chest instanceof ContainerChest)
            return ((ContainerChest) chest).getLowerChestInventory().getSizeInventory() + slot;

        if (chest instanceof ContainerShulkerBox)
            return 27 + slot;
        return slot;
    }

    private static ItemStack pickupClick(int slotId, IPlayerContext ctx) {
        return ctx.playerController().windowClick(ctx.player().openContainer.windowId, slotId, 0, ClickType.PICKUP, ctx.player());
    }

   
    public static class MapHome {
        BlockPos chestCoords;
        int chestSlot;
        int shulkerSlot;

        int mapId;

        public MapHome(BlockPos chestCoords, int chestSlot, int shulkerSlot, int mapId){
            this.chestCoords = chestCoords;
            this.chestSlot = chestSlot;
            this.shulkerSlot = shulkerSlot;
            this.mapId = mapId;
        }

    }

}
