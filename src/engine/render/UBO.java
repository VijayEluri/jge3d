//TODO: Maybe add transforms?
package engine.render;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBUniformBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;

import engine.render.ubos.UBOInterface;

public class UBO {
	private boolean hasUBO = false;
	private int uboID;
	private int block_index;
	private int block_size;
	public static enum Type {LIGHT, MATERIAL, PERSPECTIVE};
	private UBOInterface ubo_interface;
	private IntBuffer offsets;
	private IntBuffer indices;
	private int shader;

	public UBO(Shader shader, UBOInterface ubo) {
		createUBO(ubo, shader);
	}

	public boolean getHasUBO() {
		return hasUBO;
	}

	public int getVBOID() {
		return uboID;
	}

	// *******************VBO METHODS**************************
	public void createUBO(UBOInterface ubo, Shader shader) {
		//Make sure not to duplicate UBOs		
		if (hasUBO) {
			//System.out.println("DestroyingVBO: " + modelVBOID + ":" + modelVBOindexID);
			destroyUBO();
		}
		
		ubo_interface = ubo;
		this.shader = shader.getShaderID();
		
		//create a new UBO reference
		uboID = createUBOID(1);
		
		//create a reference to the data definition in the shader
		block_index = ARBUniformBufferObject.glGetUniformBlockIndex(
			shader.getShaderID(),
			ubo.getName()
		);
		
		//get the size of the block you just found
		block_size = ARBUniformBufferObject.glGetActiveUniformBlocki(
			shader.getShaderID(),
			block_index,
			ARBUniformBufferObject.GL_UNIFORM_BLOCK_DATA_SIZE
		);
		
		//put the index of each variable in the block into an array
		indices = BufferUtils.createIntBuffer(ubo.getSize());
		ARBUniformBufferObject.glGetUniformIndices(
			shader.getShaderID(), 
			ubo.getNames(), 
			indices
		);			
		
		//put the offsets for each variable into an intbuffer
		offsets = BufferUtils.createIntBuffer(ubo.getSize());
		ARBUniformBufferObject.glGetActiveUniforms(
			shader.getShaderID(), 
			indices, 
			ARBUniformBufferObject.GL_UNIFORM_OFFSET,
			offsets
		);
		
		//put data into the buffer
		ARBBufferObject.glBindBufferARB(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER,
			uboID
		);

		ARBBufferObject.glBufferDataARB(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER, 
			ubo_interface.createBuffer(block_size, offsets), 
			ARBBufferObject.GL_DYNAMIC_DRAW_ARB
		);
		
		//bind the block to the buffer object
		ARBUniformBufferObject.glBindBufferBase(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER,
			block_index,
			uboID
		);
		
		ARBUniformBufferObject.glUniformBlockBinding(shader.getShaderID(), block_index, block_index);
		
		//debug();

		// Set the notifier
		hasUBO = true;
	}
	/*
	public void buffer_ubo(Shader shader) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(ubo_interface.getSize());
		buffer.put(ubo_interface.createBuffer());

		// NEVER FLIP AGAIN PAST THIS POINT UNLESS YOU'RE LOADING IN
		// COMPLETELY NEW DATA
		buffer.flip();

		// Put data in allocated buffers
		bufferData(uboID, buffer);
	}*/

	private int createUBOID(int i) {
		IntBuffer buffer = BufferUtils.createIntBuffer(i);
		ARBVertexBufferObject.glGenBuffersARB(buffer);
		return buffer.get(0);
	}
	
	public void bufferData() {
		//put data into the buffer
		ARBBufferObject.glBindBufferARB(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER,
			uboID
		);

		ARBBufferObject.glBufferSubDataARB(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER, 
			0, 
			ubo_interface.createBuffer(block_size, offsets)
		);
		/*
		//bind the block to the buffer object
		ARBUniformBufferObject.glBindBufferBase(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER,
			block_index,
			uboID
		);
		
		ARBUniformBufferObject.glUniformBlockBinding(shader, block_index, block_index);
		*/
	}
	
	public void destroyUBO() {
		ARBBufferObject.glDeleteBuffersARB(uboID);
		hasUBO = false;
	}

	public void setInterface(UBOInterface ubo_interface) {
		this.ubo_interface = ubo_interface; 
	}
	
	public UBOInterface getInterface() {
		return ubo_interface; 
	}
	
	public void debug() {
		System.out.println(
			"shader: " + shader + "\n" +
			"uboID: " + uboID + "\n" +
			"block_index: " + block_index + "\n" +
			"block_size: " + block_size + "\n" +
			"block_name: " + ubo_interface.getName()
		);
		System.out.println("indices:");
		for(int i=0; i < indices.capacity(); i++) {
			System.out.println("\t" + indices.get(i));
		}
		System.out.println("\noffsets:");
		for(int i=0; i < offsets.capacity(); i++) {
			System.out.println("\t" + offsets.get(i));
		}
	}
}
