package window;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.ImageIcon;

import org.lwjgl.LWJGLException;

import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;

public class TextureMenu extends ResizableFrame {
	private final Label preview;

	public TextureMenu() {
		setTitle("Texture Editor");
		setTheme("texturemenu");
	
		preview = new Label();
		LWJGLRenderer render;
		try {
			//Create a temporary renderer instance
			render = new LWJGLRenderer();
			
			//Read image from file
			ImageIcon icon = new ImageIcon("resources/themes/widgets.png");
		    Image imageicon = icon.getImage();
			
		    // Create empty BufferedImage, sized to Image
		    BufferedImage buffImage = new BufferedImage(
		    	imageicon.getWidth(null), 
		    	imageicon.getHeight(null), 
		        BufferedImage.TYPE_INT_ARGB
		    );

		    // Draw Image into BufferedImage
		    Graphics g = buffImage.getGraphics();
		    g.drawImage(imageicon, 0, 0, null);
		    
			int[] data = ((DataBufferInt)buffImage.getRaster().getDataBuffer()).getData(); 
			ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4); 
			bb.order(ByteOrder.LITTLE_ENDIAN); 
			bb.asIntBuffer().put(data); 
			
			//Create a TWL dynamic image
			DynamicImage image = render.createDynamicImage(
				imageicon.getWidth(null),
				imageicon.getHeight(null)
			);
			
			//Copy contents of bytebuffered image into dynamic image
			image.update(bb,DynamicImage.Format.RGBA);
			
			//Set the label to the image
			preview.setBackground(image);

		} catch (LWJGLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		add(preview);
	}
}