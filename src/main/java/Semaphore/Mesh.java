package Semaphore;

import utils.Instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mesh {
    private final MeshTileWithSemaphore[][] meshTiles;
    private List<MeshTileWithSemaphore> entryPoints;
    private List<MeshTileWithSemaphore> exitPoints;

    public Mesh(Instance instance){
        this.meshTiles = buildMeshTiles(instance.getHeight(), instance.getDepth(), instance.getMesh());
    }

    private MeshTileWithSemaphore[][] buildMeshTiles(int height, int depth, int[][] points){
        MeshTileWithSemaphore[][] unfinishedMeshTiles = new MeshTileWithSemaphore[height][depth];
        entryPoints = new ArrayList<>();
        exitPoints = new ArrayList<>();
        for (int height_index = 0; height_index < height; height_index++) {
            for (int depth_index = 0; depth_index < depth; depth_index++) {
                MeshTileWithSemaphore tile = new MeshTileWithSemaphore(height_index, depth_index, points[height_index][depth_index]);
                classifyPoint(tile, height_index, depth_index, height, depth);
                unfinishedMeshTiles[height_index][depth_index] = tile;
            }
        }
        return unfinishedMeshTiles;
    }

    public List<MeshTileWithSemaphore> getEntryPoints() {
        return entryPoints;
    }

    public List<MeshTileWithSemaphore> getExitPoints() {
        return exitPoints;
    }

    private void classifyPoint(MeshTileWithSemaphore tile, int height_index, int depth_index, int height, int depth){
        if(tile.getId() != 0){
            // Upper tiles
            if(height_index == 0){
                if (tile.getId() == 3){
                    entryPoints.add(tile);
                    return;
                }else if (tile.getId() == 1){
                    exitPoints.add(tile);
                    return;
                }
            }
            // Downer tiles
            if(height_index == height-1){
                if (tile.getId() == 1){
                    entryPoints.add(tile);
                    return;
                }else if (tile.getId() == 3){
                    exitPoints.add(tile);
                    return;
                }
            }
            // Left tiles
            if(depth_index == 0){
                if (tile.getId() == 2){
                    entryPoints.add(tile);
                    return;
                }else if(tile.getId() == 4){
                    exitPoints.add(tile);
                    return;
                }
            }
            // Right tiles
            if(depth_index == depth-1){
                if (tile.getId() == 4){
                    entryPoints.add(tile);
                }else if(tile.getId() == 2){
                    exitPoints.add(tile);
                }
            }
        }
    }

    public MeshTileWithSemaphore[][] getMeshTiles() {
        return meshTiles;
    }

    @Override
    public String toString() {
        return "Semaphore.Mesh{" +
                "meshTiles=" + Arrays.deepToString(meshTiles) +
                '}';
    }

}
