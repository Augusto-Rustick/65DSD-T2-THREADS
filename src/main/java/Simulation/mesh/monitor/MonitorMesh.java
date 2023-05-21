package Simulation.mesh.monitor;

import Simulation.mesh.Mesh;
import Simulation.mesh.MeshTile;
import utils.Instance;

public class MonitorMesh extends Mesh {
    public MonitorMesh(Instance instance) {
        super(instance);
    }

    @Override
    protected MeshTile createMeshTile(int heightIndex, int depthIndex, int point) {
        return new MonitorMeshTile(heightIndex, depthIndex, point);
    }
}
