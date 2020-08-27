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
		vertex = new VBO(verts, context);
		count = verts.size/Float.BYTES;
	}
	
	public void render() {
		vertex.bind();
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_STRIP, 0, count);
		vertex.unbind();
	}

	@Override
	public void dispose() {
		vertex.dispose();
	}
}
