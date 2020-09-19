package com.andedit.badtrucks.glutils;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.graphics.GL20.GL_ARRAY_BUFFER;

import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.FloatArray;

/** An VertexBufferObject. */
public final class VBO implements Vertex
{
	private final VertContext context;
	private final ByteBuffer buffer;
	private final int glDraw;
	private final boolean ownBuffer;
	private int bufferHandle = -1;
	private boolean isDirty, isBound;

	/** Thread-safe constructor, but creates own buffer. */
	public VBO(final FloatArray array, final VertContext context, final int glDraw) {
		this.context = context;
		this.glDraw = glDraw;
		buffer = BufferUtils.newUnsafeByteBuffer(context.getAttrs().vertexSize * (array.size/context.getFloatSize()));
		BufferUtils.copy(array.items, buffer, array.size, 0);
		ownBuffer = true;
	}
	
	/** Not thread-safe constructor, but shares buffer. */
	public VBO(final ByteBuffer buffer, final VertContext context, final int glDraw) {
		this.context = context;
		this.glDraw = glDraw;
		this.buffer = buffer;
		upload();
		gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
		ownBuffer = false;
	}
	
	@Override
	public void setVertices(float[] vertices, int offset, int count) {
		BufferUtils.copy(vertices, buffer, count, offset);
		if (isBound) {
			isDirty = false;
			gl.glBufferData(GL_ARRAY_BUFFER, buffer.limit(), buffer, glDraw);
		} else isDirty = true;
	}

	@Override
	public void bind() {
		if (isUploaded()) 
		gl.glBindBuffer(GL_ARRAY_BUFFER, bufferHandle);
		else upload();
		isBound = true;
		
		if (isDirty) {
			isDirty = false;
			gl.glBufferData(GL_ARRAY_BUFFER, buffer.limit(), buffer, glDraw);
		}
		
		final VertexAttributes attributes = context.getAttrs();
		final ShaderProgram shader = context.getShader();
		final int numAttributes = attributes.size();
		for (int i = 0; i < numAttributes; ++i) {
			final VertexAttribute attribute = attributes.get(i);
			final int location = context.getLocation(i);
			shader.enableVertexAttribute(location);

			shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized,
				attributes.vertexSize, attribute.offset);
		}
	}
	
	@Override
	public void unbind() {
		final VertexAttributes attributes = context.getAttrs();
		final ShaderProgram shader = context.getShader();
		final int numAttributes = attributes.size();
		for (int i = 0; i < numAttributes; ++i) {
			shader.disableVertexAttribute(context.getLocation(i));
		}
		gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
		isBound = false;
	}

	/** Upload to GPU. */
	private void upload() 
	{
		// Create the handle.
		bufferHandle = gl.glGenBuffer();
		
		// Bind the buffer.
		gl.glBindBuffer(GL_ARRAY_BUFFER, bufferHandle);
		
		// Upload the data.
		gl.glBufferData(GL_ARRAY_BUFFER, buffer.limit(), buffer, glDraw);
		
		isDirty = false;
	}
	
	private boolean isUploaded() {
		return bufferHandle != -1;
	}

	@Override
	public void dispose() {
		if (isUploaded()) gl.glDeleteBuffer(bufferHandle);
		if (ownBuffer) BufferUtils.disposeUnsafeByteBuffer(buffer);
	}
}

