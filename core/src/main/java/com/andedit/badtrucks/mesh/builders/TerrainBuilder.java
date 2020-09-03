package com.andedit.badtrucks.mesh.builders;

import com.andedit.badtrucks.glutils.VertContext;
import com.andedit.badtrucks.handles.Shaders;
import com.andedit.badtrucks.mesh.ChunkMesh;
import com.andedit.badtrucks.mesh.verts.TerrainVert;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class TerrainBuilder extends MeshBuilder
{
	private static VertContext context = new VertContext() {
		public VertexAttributes getAttrs() {
			return TerrainVert.attributes;
		}
		public ShaderProgram getShader() {
			return Shaders.shader;
		}
		public int getLocation(int i) {
			return Shaders.locations[i];
		}
	};
	
	/*
	v1.pos.set(x+1, y, z);
	v2.pos.set(x, y, z);
	v3.pos.set(x, y, z+1);
	v4.pos.set(x+1, y, z+1);
	
		v1----v4  // new
		|      |
		|      |
		v2----v3
	*/
	public void rect(VertInfo v1, VertInfo v2, VertInfo v3, VertInfo v4) {
		vertexs.add(v1.xPos, v1.yPos, v1.zPos);
		vertexs.add(v1.xNor, v1.yNor, v1.zNor);
		
		vertexs.add(v2.xPos, v2.yPos, v2.zPos);
		vertexs.add(v2.xNor, v2.yNor, v2.zNor);
		
		vertexs.add(v3.xPos, v3.yPos, v3.zPos);
		vertexs.add(v3.xNor, v3.yNor, v3.zNor);
		
		vertexs.add(v4.xPos, v4.yPos, v4.zPos);
		vertexs.add(v4.xNor, v4.yNor, v4.zNor);
	}
	
	@Override
	public ChunkMesh create() {
		return new ChunkMesh(vertexs, context);
	}
	
	public static class VertInfo {
		public float xPos, yPos, zPos;
		public float xNor, yNor, zNor;
		//public float c;
		
		public void set(final VertInfo vert) {
			xPos = vert.xPos;
			yPos = vert.yPos;
			zPos = vert.zPos;
			xNor = vert.xNor;
			yNor = vert.yNor;
			zNor = vert.zNor;
			//c = vert.c;
		}
	}
}
