package net.iamaprogrammer.util;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class ImageOutputUtil {
    public static void exportGLFrame(int width, int height) {
        BufferedImage imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics image = imageBuffer.getGraphics();

        ByteBuffer buffer = BufferUtils.createByteBuffer(width*height*4);
        glReadBuffer(GL_BACK);
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                image.setColor(new Color(
                        (buffer.get() & 0xff),
                        (buffer.get() & 0xff),
                        (buffer.get() & 0xff)
                ));
                buffer.get();
                image.drawRect(w, height - h, 1, 1);
            }
        }
        try {
            File outputfile = new File("texture.png");
            ImageIO.write(imageBuffer, "png", outputfile);
        } catch (Exception e) {
            System.out.println("Failed");
        }
    }
}
