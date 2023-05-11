import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


/**
 * @author Denis
 */
public class View extends JFrame {
    private final JPanel jp_contentPane;
    private JPanel jp_grid;
    private JButton btn_load;
    private JButton btn_start;
    private Instance instance;
    private Mesh mesh;

    public View(){

        jp_contentPane = new JPanel();
        BorderLayout layout = new BorderLayout();
        jp_contentPane.setLayout(layout);
        setContentPane(jp_contentPane);
        setTitle("Carregue um nivel para iniciar");
        setLocation(0, 0);
        setSize(600, 600);

        loadButtons();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) throws Exception {
        View view = new View();
        while(true){
            Thread.sleep(50);
            view.drawGrid();
        }
    }

    private void loadGrid() throws IOException {
        try {
            jp_contentPane.remove(jp_grid);
        } catch (Exception ignore) {
        }
        jp_grid = new JPanel(new GridLayout(getInstance().getHeight(), getInstance().getDepth(), 0, 0));
        jp_grid.setBackground(Color.white);
        jp_grid.setSize(800, 800);
        jp_contentPane.add(BorderLayout.CENTER, jp_grid);
    }

    private void loadButtons() {
        JPanel jp_buttons = new JPanel();
        btn_load = new JButton("Carregar");
        jp_buttons.add(btn_load);
        btn_start = new JButton("Iniciar");
        jp_buttons.add(btn_start);
        jp_contentPane.add(BorderLayout.SOUTH, jp_buttons);
        loadEvents();
    }

    public void loadEvents() {
        btn_load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onLoad();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btn_start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onStart();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void drawGrid(){
        try {
            jp_grid.removeAll();
            for (int i = 0; i < getInstance().getHeight(); i++) {
                for (int j = 0; j < getInstance().getDepth(); j++) {
                    JPanel jp_block = new JPanel();
                    BorderLayout layout = new BorderLayout();
                    jp_block.setLayout(layout);
                    jp_block.add(new ImagePanel(getMesh().getMeshTiles()[i][j].getImage(), 50, 50), BorderLayout.CENTER);
                    jp_grid.add(jp_block, (i * getInstance().getDepth()) + j);
                }
            }
            jp_grid.updateUI();
        }catch (Exception ignored){

        }
    }

    private void onLoad() throws Exception {
        FileDialog fileDialog = new FileDialog((Frame) null);
        fileDialog.setVisible(true);
        String path = fileDialog.getDirectory() + fileDialog.getFile();
        setTitle("Fase: " + fileDialog.getFile().replace(".txt", ""));
        setInstance(new Instance(path));
        setMesh(new Mesh(getInstance()));
        this.loadGrid();
        this.drawGrid();
    }

    private void onStart(){
        CarInserter carInserter = new CarInserter(1000, mesh, 10);
        carInserter.start();
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}