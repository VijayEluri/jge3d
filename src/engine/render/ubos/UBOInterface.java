package engine.render.ubos;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import engine.render.UBO.Type;

public interface UBOInterface {
	public String[] getNames();
	public FloatBuffer createBuffer(int block_size, IntBuffer offsets);
	public Type getType();
	public int getSize();
	public String getName();
}
