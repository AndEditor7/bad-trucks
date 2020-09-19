package com.andedit.badtrucks.mesh.verts;

import com.andedit.badtrucks.glutils.VertContext;
import com.andedit.badtrucks.handles.TexLib;
import com.andedit.badtrucks.utils.Camera;
import com.andedit.badtrucks.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

//Needs update comments after attribute change.
/** The static class contains vertex attributes and shader */
public final class TerrainStream
{
	/** 3 Position and 1 Data. */
	public static final VertexAttributes attributes = new VertexAttributes(
				VertexAttribute.Position(),
				new VertexAttribute(Usage.ColorPacked, 4, GL20.GL_UNSIGNED_BYTE, true, "a_data")
			);
	
	/** 16 bytes in a single vertex with 4 float components. */ 
	public static final int byteSize = attributes.vertexSize;
	
	/** 4 floats in a single vertex. */ 
	public static final int floatSize = byteSize/Float.BYTES;
	
	public static ShaderProgram shader;
	public static int[] locations;
	
	public static void ints(AssetManager asset, String path) {
		shader = asset.get(path, ShaderProgram.class);
		locations = Util.locateAttributes(shader, attributes);
		
		TexLib.grass.bind(2);
		TexLib.path.bind(3);
		TexLib.gravel.bind(4);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		
		shader.bind();
		shader.setUniformi("u_defaultTex", 2);
		shader.setUniformi("u_rTexture", 3);
		shader.setUniformi("u_gTexture", 4);
		Gdx.gl.glUseProgram(0); // shader.unbind()
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
