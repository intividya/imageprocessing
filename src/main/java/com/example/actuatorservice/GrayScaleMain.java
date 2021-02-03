package com.example.actuatorservice;

public class GrayScaleMain {

    public static void main(String[] args) {
        GrayscalePicture picture = new GrayscalePicture(100, 100);
        System.out.format("%dx%d\n", picture.width(), picture.height());
        picture.save("target/abc.png");
        int row = 10;
        int col = 10;
        int gray = 0;
        picture.setGrayscale(row, col, gray);
        System.out.println(picture.get(row, col));
        System.out.println(picture.getGrayscale(row, col));
    }

}
