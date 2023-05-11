import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Instance {

    private final int height, depth;
    private final int[][] mesh;

    public Instance(String path) throws IOException {
        HashMap<String, Object> values = read(path);
        this.height = (int) values.get("height");
        this.depth = (int) values.get("depth");
        this.mesh = (int[][]) values.get("mesh");
    }

    public int getHeight() {
        return this.height;
    }

    public int getDepth() {
        return this.depth;
    }

    public int[][] getMesh() {
        return mesh;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "height=" + getHeight() +
                ", depth=" + getDepth() +
                ", mesh=" + Arrays.deepToString(mesh) +
                '}';
    }

    public HashMap<String, Object> read(String path) throws IOException {
        HashMap<String, Object> returnValues = new HashMap<>(3);
        BufferedReader reader = new BufferedReader(new FileReader(path));
        int height = Integer.parseInt(reader.readLine());
        int depth = Integer.parseInt(reader.readLine());
        returnValues.put("height", height);
        returnValues.put("depth", depth);

        int[][] mesh = new int[height][depth];

        String nextLine = reader.readLine();
        int line = 0;
        while(nextLine != null){
            int collum = 0;
            for (String tile: nextLine.split("\t")) {
                mesh[line][collum] = Integer.parseInt(tile);
                collum++;
            }
            line++;
            nextLine = reader.readLine();
        }

        reader.close();

        returnValues.put("mesh", mesh);
        return returnValues;
    }

}
