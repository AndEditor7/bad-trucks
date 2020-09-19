package com.andedit.badtrucks.mesh;

import java.nio.ByteBuffer;

import com.andedit.badtrucks.glutils.VBO;
import com.andedit.badtrucks.glutils.VertContext;
import com.andedit.badtrucks.glutils.Vertex;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;

public class ChunkMesh implements Disposable
{
	private final Vertex vertex;
	private final int byteSize;
	
	private int count;
	public boolean isDirty;
	
	public ChunkMesh(FloatArray verts, VertContext context, int glDraw) {
		byteSize = context.getAttrs().vertexSize;
		count = (verts.size / byteSize) * 6;
		vertex = new VBO(verts, context, glDraw);
	}
	
	public ChunkMesh(ByteBuffer buffer, FloatArray verts, VertContext context, int glDraw) {
		byteSize = context.getAttrs().vertexSize;
		count = (verts.size / byteSize) * 6;
		BufferUtils.copy(verts.items, buffer, verts.size, 0);
		vertex = new VBO(buffer, context, glDraw);
	}
	
	public void render() {
		vertex.bind();
		Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, count, GL20.GL_UNSIGNED_SHORT, 0);
		vertex.unbind();
	}
	
	// TODO: Use the VBOsubData. Since the x and z won't needed modify.
	public void setVertices(FloatArray verts) {
		vertex.setVertices(verts.items, 0, verts.size);
		count = (verts.size / byteSize) * 6;
	}

	@Override
	public void dispose() {
		vertex.dispose();
	}
}
