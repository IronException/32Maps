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

import baritone.api.process.PathingCommand;


public class MultiProcess extends SubProcess {

    protected SubProcess process;

    public MultiProcess(SubProcess[] processes) {
        super(extractNextProcess(processes));

        if(processes.length > 0)
            this.process = processes[0];
        else
            this.process = new Epsilon();

    }

    @Override
    public boolean isFinished() {
        return this.process.finished();
    }

    @Override
    public void doTick() {
        this.process.tick();
    }

    @Override
    public PathingCommand getReturn(){
        return this.process.getReturn();
    }

    @Override
    public PathingCommand getReturn(PathingCommand alt) {
        return this.process.finished() ? this.nextProcess.getReturn(alt) : this.process.getReturn(alt);
    }

    /**
     * this is recursivly pushing the tasks to the next multiProcess.
     * Each MultiProcess takes the first process to handle it and gives the rest as a new MultiProcess as nextProcess to super
     * if there is nothing left the process is just Epsilon
     * @param in
     * @return
     */
    private static SubProcess extractNextProcess(SubProcess[] in){
        if(in.length < 1) // < 2 because index 0 is handled in this instance and next one wouldnt have anything anymore
            return new Epsilon();

        // removing the first process to give the rest to super
        SubProcess[] rV = new SubProcess[in.length - 1];
        for (int i = 0; i < rV.length; i++) {
            rV[i] = in[i + 1];
        }
        return new MultiProcess(rV);
    }
}
