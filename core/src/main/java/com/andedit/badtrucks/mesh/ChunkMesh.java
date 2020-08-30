package com.andedit.badtrucks.mesh;

import com.andedit.badtrucks.glutils.VBO;
import com.andedit.badtrucks.glutils.VertContext;
import com.andedit.badtrucks.glutils.Vertex;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;

public class ChunkMesh implements Disposable
{
	private final Vertex vertex;
	
	private final int count;
	
	public ChunkMesh(FloatArray verts, VertContext context) {
		count = (verts.size / context.getAttrs().vertexSize) * 6;
		vertex = new VBO(verts, verts.size/context.getAttrsSize(), context);
	}
	
	public void render() {
		vertex.bind();
		Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, count, GL20.GL_UNSIGNED_SHORT, 0);
		vertex.unbind();
	}

	@Override
	public void dispose() {
		vertex.dispose();
	}
}
