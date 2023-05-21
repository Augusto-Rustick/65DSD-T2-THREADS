package Simulation.mesh.semaphore;

import Simulation.mesh.Mesh;
import Simulation.mesh.MeshTile;
import utils.Instance;

public class SemaphoreMesh extends Mesh {
    public SemaphoreMesh(Instance instance) {
        super(instance);
    }

    @Override
    protected MeshTile createMeshTile(int heightIndex, int depthIndex, int point) {
        return new SemaphoreMeshTile(heightIndex, depthIndex, point);
    }
}
