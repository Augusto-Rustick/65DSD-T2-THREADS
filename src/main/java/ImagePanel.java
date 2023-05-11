


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel{

    private Image[] images;

    public ImagePanel(String path) {
        images = new Image[1];
        try {
            Image bufferedImage = ImageIO.read(new File(path));
            images[0] = bufferedImage;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ImagePanel(String path, int x, int y) {
        images = new BufferedImage[1];
        try {
            Image bufferedImage = ImageIO.read(new File(path)).getScaledInstance(x, y, Image.SCALE_DEFAULT);;
            images[0] = bufferedImage;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ImagePanel(String[] paths,  int x, int y) {
        images = new Image[paths.length];
        int i = 0;
        try {
            for (String s: paths
            ) {
                if(s != null){
                    Image bufferedImage = ImageIO.read(new File(paths[i])).getScaledInstance(x, y, Image.SCALE_DEFAULT);;
                    images[i] = bufferedImage;
                }
                i++;
            }
        } catch (IOException e) {
            System.out.println("javax.imageio.IIOException: Can't read input at file "+i+"!");
        }
    }

    public ImagePanel(String[] paths) {
        images = new Image[paths.length];
        int i = 0;
        try {
            for (String s: paths
            ) {
                if(s != null){
                    Image bufferedImage = ImageIO.read(new File(paths[i]));
                    images[i] = bufferedImage;
                }
                i++;
            }
        } catch (IOException e) {
            System.out.println("javax.imageio.IIOException: Can't read input at file "+i+"!");
        }
    }

    public void setBuffer(String[] paths){
        images = new Image[paths.length];
        int i = 0;
        try {
            for (String s: paths
            ) {
                if(s != null){
                    Image bufferedImage = ImageIO.read(new File(paths[i]));
                    images[i] = bufferedImage;
                }
                i++;
            }
        } catch (IOException e) {
            System.out.println("javax.imageio.IIOException: Can't read input at file "+i+"!");
        }
    }

    public Image[] getImages() {
        return images;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Image image: images
        ) {
            g.drawImage(image, 0, 0, this);
        }
    }

}