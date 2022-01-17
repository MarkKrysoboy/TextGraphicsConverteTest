package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private int maxHeight;
    private int maxWidth;
    private double maxRatio;
    TextColorSchema schema;

    public int[] newSizes(int width, int height) {
        maxHeight = maxHeight == 0  ? height : maxHeight;
        maxWidth = maxWidth == 0 ? width: maxWidth;
        int[] sizes = new int[2];
        if ((maxHeight < height)||(maxWidth < width)) {
            double widthRalation = (double) maxWidth / width;
            double heightRalation = (double) maxHeight / height;

            if (widthRalation > heightRalation) {
                sizes[0] = (int) Math.round(width * heightRalation);
                sizes[1] = maxHeight;
            } else {
                sizes[0] = maxWidth;
                sizes[1] = (int) Math.round(height * widthRalation);
            }
            return sizes;
        } else {
            return sizes = new int[]{width, height};
        }
    }

    public void checkRatio(double width, double height) throws BadImageSizeException {
        if (maxRatio != 0) {
            double currentRatio = width/height;
            double currentRatio2 = height/width;
            if (maxRatio < currentRatio || maxRatio < currentRatio2) {
                throw new BadImageSizeException(Math.max(currentRatio, currentRatio2), maxRatio);
            }
        }
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        checkRatio(img.getWidth(), img.getHeight());

        int newHeight;
        int newWidth;
        Image scaledImage;
        if (maxHeight != 0 || maxWidth != 0) {
            int[] sizes = newSizes(img.getWidth(), img.getHeight());
            newWidth = sizes[0];
            newHeight = sizes[1];
            scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        } else {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
            scaledImage = img;
        }
        if (schema == null) {
            schema = new TextColorSchemaImpl();
        }

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        int[] tempArray = new int[3];
        char[][] charArray = new char[bwRaster.getHeight()][bwRaster.getWidth()];
        for (int h = 0; h < bwRaster.getHeight(); h++) {
            for (int w = 0; w < bwRaster.getWidth(); w++) {
                int color = bwRaster.getPixel(w, h, tempArray)[0];
                char c = schema.convert(color);
                charArray[h][w] = c;
            }
        }

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            for (int j = 0; j < charArray[i].length; j++) {
                text.append(charArray[i][j]);
                text.append(charArray[i][j]);
            }
            text.append("\n");
        }
        return text.toString();
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
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
