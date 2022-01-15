package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatioByMethod;

    private void validImageSize(BufferedImage img) throws BadImageSizeException {
        double ratio = img.getHeight() / img.getWidth();
        if (ratio > getMaxRatioByMethod()) {
            throw new BadImageSizeException(ratio, getMaxRatioByMethod());
        }
    }

    private Image resizeImage(BufferedImage img) {
        if (Math.max(img.getWidth(), img.getHeight()) == img.getHeight() && img.getHeight() > getMaxHeight()) {
            int newHeight = getMaxHeight();
            int newWidth = img.getWidth() / (img.getHeight() / getMaxHeight());
            return img.getScaledInstance(newWidth, newHeight , BufferedImage.SCALE_SMOOTH);
        }
        if (Math.max(img.getWidth(), img.getHeight()) == img.getWidth() && img.getWidth() > getMaxWidth()) {
            int newWidth = getMaxWidth();
            int newHeight = img.getHeight() / (img.getWidth() / getMaxWidth());
            return img.getScaledInstance(newWidth, newHeight , BufferedImage.SCALE_SMOOTH);
        }
        return img;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        Schema schema = new Schema();
        BufferedImage img = ImageIO.read(new URL(url));
        validImageSize(img);
        Image scaledImg = resizeImage(img);
        BufferedImage bwImg = new BufferedImage(scaledImg.getWidth(null), scaledImg.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImg, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        StringBuilder txtImage = new StringBuilder();
        for (int h = 0; h < scaledImg.getHeight(null); h++) {
            for (int w = 0; w < scaledImg.getWidth(null); w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                String c = schema.convert(color);
                txtImage.append(c);
                txtImage.append(c);
            }
            txtImage.append("\r\n");
        }
        return txtImage.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatioByMethod = maxRatio;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public double getMaxRatioByMethod() {
        return maxRatioByMethod;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {

    }
}
