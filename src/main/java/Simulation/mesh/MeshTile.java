package Simulation.mesh;
import Simulation.Allocatable;
import Simulation.types.CarType;

import java.util.ArrayList;
import java.util.List;

public abstract class MeshTile implements Allocatable {

    private final int height, depth;
    private final String text;
    private final int id;
    private boolean occupied;
    private CarType car = CarType.FAMILY_CAR;

    public MeshTile(int height, int depth, int id) {
        this.height = height;
        this.depth = depth;
        this.id = id;
        this.text = this.getTextFromId();
        this.occupied = false;
    }

    public void setCar(CarType car) {
        this.car = car;
    }

    protected abstract String getExtraLayer();

    private String[] getImageFromId(){
        if (isOccupied()) {
            return new String[]{"assets/" + id + ".png", getCarOrientation(), getExtraLayer()};
        } else {
            return new String[]{"assets/" + id + ".png", getExtraLayer()};
        }
    }

    private String getCarOrientation() {
        switch (id) {
            case 1, 5, 9, 10 -> {
                return "assets/"+car.name()+"_going_up.png";
            }
            case 2, 6 -> {
                return "assets/"+car.name()+"_going_right.png";
            }
            case 3, 7, 11, 12 -> {
                return "assets/"+car.name()+"_going_down.png";
            }
            case 4, 8 -> {
                return "assets/"+car.name()+"_going_left.png";
            }
            default -> {
                return "assets/car_null.png";
            }
        }
    }

    private String getTextFromId() {
        switch (id) {
            case 0 -> {
                return "Nada";
            }
            case 1 -> {
                return "Estrada Cima";
            }
            case 2 -> {
                return "Estrada Direita";
            }
            case 3 -> {
                return "Estrada Baixo";
            }
            case 4 -> {
                return "Estrada Esquerda";
            }
            case 5 -> {
                return "Cruzamento Cima";
            }
            case 6 -> {
                return "Cruzamento Direita";
            }
            case 7 -> {
                return "Cruzamento Baixo";
            }
            case 8 -> {
                return "Cruzamento Esquerda";
            }
            case 9 -> {
                return "Cruzamento Cima e Direita";
            }
            case 10 -> {
                return "Cruzamento Cima e Esquerda";
            }
            case 11 -> {
                return "Cruzamento Baixo e Direita";
            }
            case 12 -> {
                return "Cruzamento Baixo e Esquerda";
            }
            default -> {
                return "Estrada não mapeada na malha";
            }
        }
    }

    public int[] getActionsFromId() {
        switch (id) {
            case 1, 5 -> {
                return new int[]{-1, 0};
            }
            case 2, 6 -> {
                return new int[]{0, 1};
            }
            case 3, 7 -> {
                return new int[]{1, 0};
            }
            case 4, 8 -> {
                return new int[]{0, -1};
            }
            case 9 -> {
                return new int[]{-1, 1};
            }
            case 10 -> {
                return new int[]{-1, -1};
            }
            case 11 -> {
                return new int[]{1, 1};
            }
            case 12 -> {
                return new int[]{1, -1};
            }
            default -> {
                return new int[]{0, 0};
            }
        }
    }

    public List<int[][]> getCrossingFromId(){

        int[] up = new int[]{-1, 0};
        int[] down = new int[]{1, 0};
        int[] left = new int[]{0, -1};
        int[] right = new int[]{0, 1};

        List<int[][]> requiredTiles = new ArrayList<>();

        switch (id) {
            case 1 -> {
                requiredTiles.add(new int[][]{up, up, up});
                requiredTiles.add(new int[][]{up, up, left, left});
                requiredTiles.add(new int[][]{up, right});
                return requiredTiles;
            }
            case 2 -> {
                requiredTiles.add(new int[][]{right, right, right});
                requiredTiles.add(new int[][]{right, down});
                requiredTiles.add(new int[][]{right, right, up, up});
                return requiredTiles;
            }
            case 3 -> {
                requiredTiles.add(new int[][]{down, down, down});
                requiredTiles.add(new int[][]{down, left});
                requiredTiles.add(new int[][]{down, down, right, right});
                return requiredTiles;
            }
            case 4 -> {
                requiredTiles.add(new int[][]{left, left, left});
                requiredTiles.add(new int[][]{left, up});
                requiredTiles.add(new int[][]{left, left, down, down});
                return requiredTiles;
            }
            default -> {return requiredTiles;}
        }
    }

    public String[] getImage() {
        return getImageFromId();
    }

    public String getText() {
        return text;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public void leaveTile() {
        this.occupied = false;
    }

    public void enterTile() {
        this.occupied = true;
    }

    public int getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        return "MeshTile{" +
                "height=" + height +
                ", depth=" + depth +
                ", text='" + text + '\'' +
                ", id=" + id +
                ", occupied=" + occupied +
                ", car=" + car +
                '}';
    }
}
