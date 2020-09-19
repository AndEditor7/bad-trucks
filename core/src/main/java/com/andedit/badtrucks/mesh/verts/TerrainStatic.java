package com.andedit.badtrucks.mesh.verts;

import com.andedit.badtrucks.glutils.VertContext;
import com.andedit.badtrucks.handles.TexLib;
import com.andedit.badtrucks.utils.Camera;
import com.andedit.badtrucks.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

// Needs update comments after attribute change.
/** The static class contains vertex attributes and shader */
public final class TerrainStatic
{
	/** 3 Position. */
	public static final VertexAttributes attributes = new VertexAttributes(
				VertexAttribute.Position()
			);
	
	/** 12 bytes in a single vertex with 3 float components. */ 
	public static final int byteSize = attributes.vertexSize;
	
	/** 3 floats in a single vertex. */ 
	public static final int floatSize = byteSize/Float.BYTES;
	
	public static ShaderProgram shader;
	public static int[] locations;
	
	public static void ints(AssetManager asset, String path) {
		shader = asset.get(path, ShaderProgram.class);
		locations = Util.locateAttributes(shader, attributes);
		shader.bind();
		TexLib.grass.bind(1);
		shader.setUniformi("u_grassTex", 1);
		Gdx.gl.glUseProgram(0);
	}
	
	public static void bind(Camera cam) {
		shader.bind();
		shader.setUniformMatrix("u_projTrans", cam.combined);
	}
	
	public final static VertContext context = new VertContext() {
		public VertexAttributes getAttrs() {
			return attributes;
		}
		public ShaderProgram getShader() {
			return shader;
		}
		public int getLocation(int i) {
			return locations[i];
		}
	};
}
