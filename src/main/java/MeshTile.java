import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class MeshTile {

    private final int height, depth;
    private final String text;
    private final int id;
    private boolean occupied;
    private Semaphore semaphore = new Semaphore(1);
    private CarType car = CarType.FAMILY_CAR;

    public MeshTile(int height, int depth, int id) {
        this.height = height;
        this.depth = depth;
        this.id = id;
        this.text = this.getTextFromId(id);
        this.occupied = false;
    }

    public void setCar(CarType car) {
        this.car = car;
    }

    private String[] getImageFromId(int id) {
        if (isOccupied()) {
            return new String[]{"assets/" + id + ".png", getCarOrientation(id)};
        } else {
            return new String[]{"assets/" + id + ".png"};
        }
    }

    private String getCarOrientation(int id) {
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

    private String getTextFromId(int id) {
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
                return "Cruzamento Direita e Baixo";
            }
            case 12 -> {
                return "Cruzamento Baixo e Esquerda";
            }
            default -> {
                return "Estrada não mapeada na malha";
            }
        }
    }

    public int[] getActionsFromId(int id) {
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

    public String[] getImage() {
        return getImageFromId(this.id);
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

    public Semaphore getSemaphore() {
        return semaphore;
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
        return "{x=" + height + ", y=" + depth + ", id=" + id + ", text=" + text + ", image=" + Arrays.toString(getImageFromId(id)) + ", occupied=" + occupied + "}";
    }
}