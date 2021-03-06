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

package baritone.process;

import baritone.Baritone;
import baritone.api.event.listener.AbstractGameEventListener;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.process.IChestSortProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.process.sub.processes.*;
import baritone.process.sub.processes.helper.*;
import baritone.utils.BaritoneProcessHelper;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;

public final class ChestSortProcess extends BaritoneProcessHelper implements IChestSortProcess, AbstractGameEventListener {
    private static final PathingCommand NO_PATH = new PathingCommand(null, PathingCommandType.DEFER);

    public static boolean debug = true;

    public static String filePath = Config.filePath;

    public static BlockPos targetPos = Config.targetPos;
    public static BlockPos putMaps = Config.putMaps;
    public static Vec3d putMapLocs = Config.putMapLocs;
    public static Vec3i relativeShulkerPos = Config.relativeShulkerPos;
    public static SlotConverter hotbarSlot = Config.hotbarSlot;
    public static BlockPos trashCoords = Config.trashCoords;

    public static BlockPos shulkerPos = Config.shulkerPos;

    public static int pauseTicks;


    private boolean active = false;

    public static ChestSortProcess INSTANCE;

    SubProcess process;

    public ChestSortProcess(Baritone baritone) {
        super(baritone);
        ChestSortProcess.INSTANCE = this;

    }


    public SubProcess getProcessBuild() {
        SubProcess rV;
        Item item = Item.getItemById(358);
        int mapsLeft = ChestHelper.itemsInInv(item);
        if (mapsLeft > 0) {
            SlotConverter mapSlot = new AbstractSlot(item, new SlotConverter(0, ContainerType.INVENTORY), true);

            int mapId = ChestHelper.convertMapId(mapSlot);
            logMapId(mapId);

            if (invalidId(mapId))
                rV = new Epsilon();
            else
                // how to do duplicate now?
                rV = new PutMap(mapSlot, putMaps, putMapLocs, hotbarSlot, relativeShulkerPos,
                        new ChatProcess("finished sorting map_" + mapId + ". " + mapsLeft + " maps are left", new Epsilon()));


        } else {
            rV = new GetMaps(targetPos, hotbarSlot, shulkerPos, trashCoords,
                    new ChatProcess("loaded maps", new Epsilon()));
        }




        /*return new GoalNearProcess(targetPos, 2,
                new EditChestShulkerProcess(targetPos, new Epsilon())
        ); // TODO its going to the same pos twice like this. so redo that....
        */


        return rV;/*
                new ChatProcess("start",
                //new BreakBlock(shulkerPos, true,
                        //new EditShulkerProcess(shulkerPos, hotbarSlot, new Epsilon(), true,
                //new getMaps(targetPos, hotbarSlot, shulkerPos,
                new PutMap(new SlotConverter(0, ContainerType.INVENTORY), putMaps, putMapLocs, hotbarSlot, relativeShulkerPos,
                        new ChatProcess("end", new Epsilon())));*/

    }

    private boolean invalidId(int mapId) {
        return mapId < 0;
    }

    // read file one line at a time
    // replace line as you read the file and store updated lines in StringBuffer
    // overwrite the file with the new lines
    public static void logMapId(int mapId) {
        try {
            // input the (modified) file content to the StringBuffer "input"
            BufferedReader file = new BufferedReader(new FileReader(filePath));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            boolean gotIt = false;
            while ((line = file.readLine()) != null) {
                if (line.split(":")[0].equals(mapId + "")) {
                    line += System.currentTimeMillis() + ", ";
                    gotIt = true;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }

            if (!gotIt)
                inputBuffer.append(mapId + ": " + System.currentTimeMillis() + ", \n");

            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(filePath);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean isActive() {
        return this.active;// && this.process.isFinished();
    }

    @Override
    public void activate(int pauseTicks) {
        this.active = true;
        this.process = new Epsilon(); // so we finish and onTick generates the process
        this.pauseTicks = pauseTicks;
    }


    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        // test wether maps in inv


        if (this.process.finished()) {
            this.process = getProcessBuild();
            //this.active = false;
        }

        try {
            this.process.tick();
        } catch (Exception e) {
            e.printStackTrace();
            logDirect("is done?... check error...");
            active = false;
        }

        PathingCommand rV = process.getReturn();
        if (rV == null) {
            //logDirect("No SubProcess returned a PathingCommand. THIS HAS TO BE FIXED");
            // could also be Request Pause
            rV = new PathingCommand(new GoalBlock(ctx.playerFeet()), PathingCommandType.REVALIDATE_GOAL_AND_PATH);
        }
        return rV;
    }


    @Override
    public void onLostControl() {
        this.active = false;

        // reset the state
        process = null;
    }

    @Override
    public String displayName0() {
        return "ChestSortProcess";
    }


}
