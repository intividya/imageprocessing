package com.example.actuatorservice;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class GrayscalePicture {

    private BufferedImage image;               // the rasterized image
    private String filename;                   // name of file
    private boolean isOriginUpperLeft = true;  // location of origin
    private final int width, height;           // width and height

    public GrayscalePicture(int width, int height) {
        if (width < 0) throw new IllegalArgumentException("width must be non-negative");
        if (height < 0) throw new IllegalArgumentException("height must be non-negative");
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    }

    private static Color toGray(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int y = (int) (Math.round(0.299 * r + 0.587 * g + 0.114 * b));
        return new Color(y, y, y);
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    private void validateRowIndex(int row) {
        if (row < 0 || row >= height())
            throw new IllegalArgumentException("row index must be between 0 and " + (height() - 1) + ": " + row);
    }

    private void validateColumnIndex(int col) {
        if (col < 0 || col >= width())
            throw new IllegalArgumentException("column index must be between 0 and " + (width() - 1) + ": " + col);
    }

    private void validateGrayscaleValue(int gray) {
        if (gray < 0 || gray >= 256)
            throw new IllegalArgumentException("grayscale value must be between 0 and 255");
    }

    public Color get(int col, int row) {
        validateColumnIndex(col);
        validateRowIndex(row);
        Color color = new Color(image.getRGB(col, row));
        return toGray(color);
    }

    public int getGrayscale(int col, int row) {
        validateColumnIndex(col);
        validateRowIndex(row);
        if (isOriginUpperLeft) return image.getRGB(col, row) & 0xFF;
        else return image.getRGB(col, height - row - 1) & 0xFF;
    }

    public void setGrayscale(int col, int row, int gray) {
        validateColumnIndex(col);
        validateRowIndex(row);
        validateGrayscaleValue(gray);
        int rgb = gray | (gray << 8) | (gray << 16);
        if (isOriginUpperLeft) image.setRGB(col, row, rgb);
        else image.setRGB(col, height - row - 1, rgb);
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        GrayscalePicture that = (GrayscalePicture) other;
        if (this.width() != that.width()) return false;
        if (this.height() != that.height()) return false;
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                if (this.getGrayscale(col, row) != that.getGrayscale(col, row)) return false;
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width + "-by-" + height + " grayscale picture (grayscale values given in hex)\n");
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int gray = 0;
                if (isOriginUpperLeft) gray = 0xFF & image.getRGB(col, row);
                else gray = 0xFF & image.getRGB(col, height - row - 1);
                sb.append(String.format("%3d ", gray));
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported because pictures are mutable");
    }

    public void save(String name) {
        if (name == null) throw new IllegalArgumentException("argument to save() is null");
        save(new File(name));
        filename = name;
    }

    public void save(File file) {
        if (file == null)
            throw new IllegalArgumentException("argument to save() is null");
        filename = file.getName();
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        if ("jpg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix)) {
            try {
                ImageIO.write(image, suffix, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: filename must end in .jpg or .png");
        }
    }

}
