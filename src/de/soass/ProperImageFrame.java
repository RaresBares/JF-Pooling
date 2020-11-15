package de.soass;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class ProperImageFrame {


    public static String picpath = "src/picture.jpg";
    public static void main(String args[]) {
        createAndShowGui();
    }

    private static void createAndShowGui() {
        tryToSetLookAndFeel();

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.getContentPane().setLayout(new BorderLayout());

        ImagePanel imagePanel = new ImagePanel();
        f.getContentPane().add(imagePanel, BorderLayout.CENTER);

        JButton importButton = new JButton("Import");
        importButton.addActionListener(e -> loadAndShowImage(imagePanel));

        JButton GreyScaleButton = new JButton("GreyScale");
        GreyScaleButton.addActionListener(e -> greyScale(imagePanel));

        JButton maxButton = new JButton("MaxPool");
        maxButton.addActionListener(e -> maxPool(imagePanel,true));

        JButton avgButton = new JButton("AvgPool");
        avgButton.addActionListener(e -> avgPool(imagePanel,true));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(importButton);
        buttonPanel.add(maxButton);
        buttonPanel.add(GreyScaleButton);
        buttonPanel.add(avgButton);
        f.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        f.setSize(1000, 700);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    public static BufferedImage image;
    public static BufferedImage buffer;

    private static void avgPool(ImagePanel imagePanel,boolean override) {

        try {
            BufferedImage  localimage = ImageIO.read(new File(picpath));
            System.out.println(buffer == null);
            if(override && buffer != null){
                System.out.println("bug");
                localimage = buffer;
            }
            int width;
            int height;

            width = localimage.getWidth();
            height = localimage.getHeight();

            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    Color c = new Color(localimage.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);
                    Color newColor = new Color(red + green + blue,

                            red + green + blue, red + green + blue);

                    localimage.setRGB(j, i, newColor.getRGB());
                }
            }


            BufferedImage feature = copyImage(localimage);
            int fheight = 3;
            int fwidth = 3;

            for (int x = 0; (x + fwidth - 1) <= localimage.getWidth(); x++) {

                for (int y = 0; (y + fheight - 1) <= localimage.getHeight(); y++) {
                    int px = (int) (fheight / 2) + x;
                    int py = (int) (fwidth / 2) + y;
                    int sum = 0;
                    int nums = 0;
                    int maxx = x + fwidth - 1;
                    int maxy = y + fheight - 1;

                    for (int lx = x; lx < maxx; lx++) {
                        for (int ly = y; ly < maxy; ly++) {
                            System.out.println(lx  + " | " + ly);
                            Color c = new Color(localimage.getRGB(lx, ly));
                            int gray = (c.getRed() + c.getBlue() + c.getGreen()) / 3;
                            sum += gray;
                            nums += 1;

                        }
                    }
                    int avg = (int)sum/nums;
                    Color c = new Color(avg,avg,avg);
                    feature.setRGB(px,py,c.getRGB());
                }

            }

            buffer = feature;
            imagePanel.setImage(feature);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void maxPool(ImagePanel imagePanel,boolean override) {

        try {
            BufferedImage  localimage = ImageIO.read(new File(picpath));
            System.out.println(buffer == null);
            if(override && buffer != null){
                System.out.println("bug");
                localimage = buffer;
            }
            int width;
            int height;

            width = localimage.getWidth();
            height = localimage.getHeight();

            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    Color c = new Color(localimage.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);
                    Color newColor = new Color(red + green + blue,

                            red + green + blue, red + green + blue);

                    localimage.setRGB(j, i, newColor.getRGB());
                }
            }


            BufferedImage feature = copyImage(localimage);
            int fheight = 3;
            int fwidth = 3;

            for (int x = 0; (x + fwidth - 1) <= localimage.getWidth(); x++) {

                for (int y = 0; (y + fheight - 1) <= localimage.getHeight(); y++) {
                    int px = (int) (fheight / 2) + x;
                    int py = (int) (fwidth / 2) + y;
                    ArrayList<Integer> ints = new ArrayList<>();
                    int maxx = x + fwidth - 1;
                    int maxy = y + fheight - 1;

                    for (int lx = x; lx < maxx; lx++) {
                        for (int ly = y; ly < maxy; ly++) {
                            System.out.println(lx  + " | " + ly);
                            Color c = new Color(localimage.getRGB(lx, ly));
                            int gray = (c.getRed() + c.getBlue() + c.getGreen()) / 3;
                            ints.add(gray);

                        }
                    }
                    int max = Collections.max(ints);
                    Color c = new Color(max, max, max);
                    feature.setRGB(px,py,c.getRGB());
                }

            }

            buffer = feature;
            imagePanel.setImage(feature);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

        return b;
    }

    private static void loadAndShowImage(ImagePanel imagePanel) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(picpath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        imagePanel.setImage(image);

    }

    private static void greyScale(ImagePanel imagePanel) {


        try {
            BufferedImage image = ImageIO.read(new File(picpath));
            int width;
            int height;

            width = image.getWidth();
            height = image.getHeight();

            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    Color c = new Color(image.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);
                    Color newColor = new Color(red + green + blue,

                            red + green + blue, red + green + blue);

                    image.setRGB(j, i, newColor.getRGB());
                }
            }


            imagePanel.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void tryToSetLookAndFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException
                | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private static class ImagePanel extends JPanel {
        private BufferedImage image;

        void setImage(BufferedImage image) {
            this.image = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, null);
            }
        }

    }
}