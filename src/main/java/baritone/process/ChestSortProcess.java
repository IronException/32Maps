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
import baritone.api.event.events.PacketEvent;
import baritone.api.event.events.type.EventState;
import baritone.api.event.listener.AbstractGameEventListener;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalNear;
import baritone.api.process.IChestSortProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.IPlayerContext;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.process.chest.sorter.*;
import baritone.process.sub.processes.*;
import baritone.utils.BaritoneProcessHelper;
import baritone.utils.chestsorter.Categories;
import baritone.utils.chestsorter.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public final class ChestSortProcess extends BaritoneProcessHelper implements IChestSortProcess, AbstractGameEventListener {
    private static final PathingCommand NO_PATH = new PathingCommand(null, PathingCommandType.DEFER);

    public static BlockPos targetPos = new BlockPos(0, 5, 0);
    public static BlockPos putMaps = new BlockPos(5, 5, 0);
    public static int addX = 0;
    public static int addZ = 2;
    public static int hotbarSlot = 5;

    public static BlockPos shulkerPos = new BlockPos(-10, 4, 10);



    private boolean active = false;


    public static ChestSortProcess INSTANCE;

    ChestVisitor chestVisitor;

    SubProcess process;

    public ChestSortProcess(Baritone baritone) {
        super(baritone);
        ChestSortProcess.INSTANCE = this;

        this.process = getProcessBuild();
    }


    public SubProcess getProcessBuild(){
        /*return new GoalNearProcess(targetPos, 2,
                new EditChestShulkerProcess(targetPos, new Epsilon())
        ); // TODO its going to the same pos twice like this. so redo that....
        */


        return new MultiProcess(new SubProcess[]{
            new GoalNearProcess(targetPos, 2, new GoalNearProcess(targetPos.add(5, 0, -10), 1, new Epsilon())),
            new Epsilon(),
            new GoalNearProcess(targetPos, 2, new Epsilon())

        });
    }


    @Override
    public boolean isActive() {
        return this.active && this.process.finished();
    }

    @Override
    public void activate() {
        this.active = true;
    }



    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        // test wether maps in inv

        if(this.process.finished())
            this.process = getProcessBuild();


        this.process.superTick();




/*
        if(this.chestVisitor == null)
            this.chestVisitor = new GetMapVisitor(this, targetPos);

        if (isChestOpen(ctx)) {
            if (!this.chestVisitor.containerOpenTick((Container) ctx.player().openContainer) || this.chestVisitor.finished()) {
                logDirect("FUCK YOU JUST LEAVE THE CONTAINER");
                ctx.player().closeScreenAndDropStack();
            }
        }

        if(this.chestVisitor.finished())
            this.chestVisitor = chestVisitor.getNextVisitor();


        // after getting the maps
        // we need to calculate new goal

        
        Goal goal = new GoalNear(chestVisitor.getGoalPos(), 2);


        Optional<Rotation> newRotation = RotationUtils.reachable(ctx, chestVisitor.getGoalPos()); //getRotationForChest(target); // may be aiming at different chest than targetPos but thats fine
        newRotation.ifPresent(rotation -> {
            baritone.getLookBehavior().updateTarget(rotation, true);
        });


        if (!(isChestOpen(ctx)) && ctx.isLookingAt(chestVisitor.getGoalPos())) {
            if (this.chestVisitor.openChest()) {
                final RayTraceResult trace = ctx.objectMouseOver();
                ctx.playerController().processRightClickBlock(ctx.player(), ctx.world(), chestVisitor.getGoalPos(), trace.sideHit, trace.hitVec, EnumHand.OFF_HAND);
            } else if(this.chestVisitor instanceof PutMapVisitor)
                ((PutMapVisitor) this.chestVisitor).tickExChest();
        }

        logDirect(chestVisitor + " " + chestVisitor.getDebug());

*/






        return process.getReturn();//new PathingCommand(goal, PathingCommandType.REVALIDATE_GOAL_AND_PATH);
    }



    @Override
    public void onLostControl() {
        this.active = false;

        // reset the state
        chestVisitor = null;
    }

    @Override
    public String displayName0() {
        return "ChestSortProcess";
    }



    private static boolean isChestOpen(IPlayerContext ctx) {
        return ctx.player().openContainer instanceof ContainerChest || ctx.player().openContainer instanceof ContainerShulkerBox;
    }

    private static Comparator<UniqueChest> closestChestToPlayer(EntityPlayerSP player) {
        return Comparator.comparingDouble(unique ->
            unique.getAllChests()
                .stream()
                .mapToDouble(tileEnt -> player.getDistanceSq(tileEnt.getPos()))
                .min()
                .getAsDouble() // UniqueChest should be guaranteed to have at least 1 chest
        );
    }

    private static Comparator<TileEntityChest> closestTileEntToPlayer(EntityPlayerSP player) {
        return Comparator.comparingDouble(tileEnt -> player.getDistanceSq(tileEnt.getPos()));
    }

    public static final class UniqueChest {
        private final Set<TileEntityChest> connectedChests; // always has at least 1 chest

        public UniqueChest(TileEntityChest chest) {
            this(getConnectedChests(chest));
        }

        public UniqueChest(Set<TileEntityChest> connectedChests) {
            if (connectedChests.isEmpty()) throw new IllegalArgumentException("Must have at least 1 chest");
            if (!connectedChests.equals(getConnectedChests(connectedChests.iterator().next()))) throw new IllegalArgumentException("bad"); // lol
            this.connectedChests = Collections.unmodifiableSet(connectedChests);
        }

        public int slotCount() {
            return connectedChests.size() * (9 * 3);
        }

        public boolean isConnected(TileEntityChest chest) {
            return connectedChests.contains(chest);
        }

        // immutable
        public Set<TileEntityChest> getAllChests() {
            return this.connectedChests;
        }

        public TileEntityChest closestChest(IPlayerContext ctx) {
            return this.getAllChests().stream()
                .min(closestTileEntToPlayer(ctx.player()))
                .get();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o.getClass() != this.getClass()) return false;
            UniqueChest that = (UniqueChest) o;
            return connectedChests.equals(that.connectedChests);
        }

        @Override
        public int hashCode() {
            return connectedChests.hashCode();
        }
    }

    private static final class StackLocation {
        public final UniqueChest chest; // TODO: use UniqueChest
        public final int slot;

        public StackLocation(UniqueChest chest, int slot) {
            this.chest = chest;
            this.slot = slot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o.getClass() != this.getClass()) return false;
            StackLocation that = (StackLocation) o;
            return slot == that.slot &&
                chest.equals(that.chest);
        }

        @Override
        public int hashCode() {
            return Objects.hash(chest, slot);
        }

        @Override
        public String toString() {
            return String.format("StackLocation(%s, %d)", this.chest.toString(), this.slot);
        }
    }

    public static abstract class ChestVisitor {
        protected final ChestSortProcess parent; // non static inner class does not allow static methods :^(

        @Nullable
        protected UniqueChest currentTarget;

        public String getDebug(){
            return "";
        }

        public boolean openChest(){
            return true;
        }

        protected ChestVisitor(ChestSortProcess parent) {
            this.parent = parent;
        }

        public abstract ChestVisitor getNextVisitor();

        public final Optional<UniqueChest> getCurrentTarget() {
            return Optional.ofNullable(this.currentTarget);
        }

        // maybe should just check if target is empty??
        public abstract boolean finished();

        // return true if this visitor must do more work
        public abstract boolean onContainerOpened(Container container);

        // Called when SPacketCloseWindow is received.
        // This may happen unexpectedly while the visitor is doing stuff and this visitor may want to call onLostControl in that case
        public void onContainerClosed(int windowId) {}

        // do some work while the container is open
        // return true if the container should stay open
        public abstract boolean containerOpenTick(Container container);

        public abstract BlockPos getGoalPos();
    }


    @Override
    public void onReceivePacket(PacketEvent event) {
        if (event.getState() != EventState.POST) return;
        if (!this.active) return;

        if (event.getPacket() instanceof SPacketWindowItems) {
            final SPacketWindowItems packet = event.cast();

            Minecraft.getMinecraft().addScheduledTask(() -> { // cant be on netty thread
                final Container openContainer = ctx.player().openContainer;
                if (isChestOpen(ctx) && openContainer.windowId == packet.getWindowId()) {
                    Container containerChest = (Container)openContainer;
                    // we just got the items for the chest we have open
                    // the server sends our inventory with the chest inventory but we only care about chest inventory so only use the first 27/54 slots
                   ///final List<ItemStack> chestItems = packet.getItemStacks().subList(0, containerChest.getLowerChestInventory().getSizeInventory());
                    final boolean stayOpen = this.chestVisitor.onContainerOpened((Container) openContainer);
                    if (!stayOpen/*this.getVisitor().wantsContainerOpen()*/) {
                        ctx.player().closeScreenAndDropStack();
                    }
                }
            });
        }
        if (event.getPacket() instanceof SPacketCloseWindow) {
            // TODO: check if chest still exists
            final Field idField = SPacketCloseWindow.class.getDeclaredFields()[0]; // lol
            idField.setAccessible(true);
            try {
                this.chestVisitor.onContainerClosed((Integer) idField.get(event.getPacket()));
            } catch (ReflectiveOperationException ex) {
                System.out.println("oyyy vey " + ex.toString());
                throw new RuntimeException(ex);
            }
        }
    }


    private Optional<Rotation> getRotationForChest(UniqueChest chestIn) {
        return chestIn.getAllChests().stream()
            .sorted(closestTileEntToPlayer(ctx.player()))
            .map(chest -> RotationUtils.reachable(ctx, chest.getPos()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();
    }



    // takes any list of chests and returns a new list of chests where no 2 chests in the list are connected
    private Set<UniqueChest> getUniqueChests(final Collection<TileEntityChest> chestsIn) {
        final Set<Set<TileEntityChest>> graphs = new HashSet<>(); // set of graphs

        // this code is O(1) but more complicated
        /*Set<TileEntityChest> chestsToAdd = new HashSet<>(chestsIn);
        Iterator<TileEntityChest> iterator = chestsToAdd.iterator();
        while (iterator.hasNext()) {
            final TileEntityChest next = iterator.next();
            if (graphs.stream().noneMatch(set -> set.contains(next))) {
                final Set<TileEntityChest> newGraph = getConnectedChests(next);
                graphs.add(newGraph);
                chestsToAdd = Sets.difference(chestsToAdd, newGraph);
                iterator = chestsToAdd.iterator();
            }
        }*/
        for (TileEntityChest iter : chestsIn) {
            if (graphs.stream().noneMatch(set -> set.contains(iter))) {
                final Set<TileEntityChest> newGraph = getConnectedChests(iter);
                graphs.add(newGraph);
            }
        }

        return graphs.stream().map(UniqueChest::new).collect(Collectors.toSet());
    }


    private static Set<TileEntityChest> getConnectedChests(TileEntityChest root) {
        Set<TileEntityChest> out = new HashSet<>();
        addToGraph(out, root);
        return out;
    }

    private static void addToGraph(Set<TileEntityChest> graph, TileEntityChest node) {
        if (node == null || graph.contains(node)) return;
        graph.add(node);
        addToGraph(graph, node.adjacentChestXNeg);
        addToGraph(graph, node.adjacentChestXPos);
        addToGraph(graph, node.adjacentChestZNeg);
        addToGraph(graph, node.adjacentChestZPos);
    }

    private static class ItemSorter {

        // temporary
        public static int compare(ItemStack a, ItemStack b) {
            return Comparator.<ItemStack>comparingInt(stack -> {
                final int idx = Category.indexOf(stack, Categories.BASE_CATEGORY);
                return idx == -1 ? Integer.MAX_VALUE : idx;
            }).compare(a, b);
        }
    }

}
