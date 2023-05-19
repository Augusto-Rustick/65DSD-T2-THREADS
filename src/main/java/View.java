import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class View extends JFrame {
    private final JPanel jp_contentPane;
    private JPanel jp_grid;
    private JButton btn_load;
    private JButton btn_start;
    private JButton btn_stop;
    private JButton btn_show_usage;
    private boolean show_usage = false;
    private JTextField jtf_vehicle_limit;
    private Instance instance;
    private Mesh mesh;
    private CarInserter carInserter;

    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        jp_contentPane = new JPanel();
        BorderLayout layout = new BorderLayout();
        jp_contentPane.setLayout(layout);
        setContentPane(jp_contentPane);
        setTitle("Carregue um nivel para iniciar");
        setLocation(0, 0);
        setDefaultLookAndFeelDecorated(true);

        loadComponents();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        Rectangle bounds = config.getBounds();
        bounds.x += screenInsets.left;
        bounds.y += screenInsets.top;
        bounds.width -= (screenInsets.left + screenInsets.right);
        bounds.height -= (screenInsets.top + screenInsets.bottom);

        setBounds(bounds);
    }

    public static void main(String[] args) throws Exception {
        View view = new View();
        view.setInstance(new Instance("./assets/instances/malha-1.txt"));
        view.setTitle("Fase: " + "malha-1");
        view.setMesh(new Mesh(view.getInstance()));
        view.loadGrid();
        view.drawGrid();
        while (true) {
            Thread.sleep(50);
            view.drawGrid();
        }
    }

    private void loadGrid() {
        try {
            jp_contentPane.remove(jp_grid);
        } catch (Exception ignore) {
        }
        jp_grid = new JPanel(new GridLayout(getInstance().getHeight(), getInstance().getDepth(), 0, 0));
        jp_grid.setBackground(Color.white);
        jp_contentPane.add(BorderLayout.CENTER, jp_grid);
    }

    private void loadComponents() {
        JPanel jp_components = new JPanel();
        btn_load = new JButton("Carregar");
        jp_components.add(btn_load);
        btn_start = new JButton("Iniciar");
        jp_components.add(btn_start);
        btn_stop = new JButton("Parar");
        btn_stop.setEnabled(false);
        jp_components.add(btn_stop);
        btn_show_usage = new JButton("Mostrar Uso");
        jp_components.add(btn_show_usage);
        jtf_vehicle_limit = new JTextField(4);
        jtf_vehicle_limit.setText(String.valueOf(1));
        jp_components.add(jtf_vehicle_limit);
        PlainDocument doc = (PlainDocument) jtf_vehicle_limit.getDocument();
        doc.setDocumentFilter(new IntegerDocumentFilter());
        jp_contentPane.add(BorderLayout.SOUTH, jp_components);
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
        btn_stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onStop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btn_show_usage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (show_usage){
                    btn_show_usage.setText("Mostrar Uso");
                    show_usage = false;
                }else{
                    btn_show_usage.setText("Esconder Uso");
                    show_usage = true;
                }

            }
        });
    }

    public void drawGrid() {
        try {
            jp_grid.removeAll();
            int gridWidth = jp_grid.getWidth();
            int gridHeight = jp_grid.getHeight();
            int tileWidth = gridWidth / getInstance().getDepth();
            int tileHeight = gridHeight / getInstance().getHeight();

            for (int i = 0; i < getInstance().getHeight(); i++) {
                for (int j = 0; j < getInstance().getDepth(); j++) {
                    JPanel jp_block = new JPanel();
                    BorderLayout layout = new BorderLayout();
                    jp_block.setLayout(layout);

                    String[] image = getMesh().getMeshTiles()[i][j].getImage();
                    if(!show_usage){
                        image[image.length-1] = "assets/free.png";
                    }
                    jp_block.add(new ImagePanel(image, tileWidth, tileHeight),
                            BorderLayout.CENTER);

                    jp_grid.add(jp_block, (i * getInstance().getDepth()) + j);
                }
            }
            jp_grid.updateUI();
        } catch (Exception ignored) {
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
        revalidate();
        repaint();
    }

    private void onStart() {
        int vehicle_limit;
        try {
            vehicle_limit = Integer.parseInt(jtf_vehicle_limit.getText());
            btn_stop.setEnabled(true);
            btn_start.setEnabled(false);
            carInserter = new CarInserter(1000, mesh, vehicle_limit);
            carInserter.start();
        } catch (NumberFormatException nfe) {
            showMessageDialog(this, "Você deve inserir um número!");
        }
    }

    private void onStop() {
        carInserter.stopThread();
        btn_stop.setEnabled(false);
        btn_start.setEnabled(true);
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

    static class IntegerDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string,
                                 AttributeSet attr) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            if (test(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            }
        }

        private boolean test(String text) {
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text,
                            AttributeSet attrs) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (test(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
