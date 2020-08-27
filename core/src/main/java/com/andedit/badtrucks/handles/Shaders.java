package com.andedit.badtrucks.handles;

import com.andedit.badtrucks.mesh.verts.TerrainVert;
import com.andedit.badtrucks.utils.Util;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

/** Static shader for terrains. */
public final class Shaders 
{
	public static final String projTran = "u_projTrans";
	
	public static ShaderProgram shader;
	public static int[] locations;
	
	public static void bind(Matrix4 mat) {
		shader.bind();
		shader.setUniformMatrix(projTran, mat);
	}
	
	public static void getShader(AssetManager asset) {
		shader = asset.get("shaders/terrain.vert", ShaderProgram.class);
		locations = Util.locateAttributes(shader, TerrainVert.attributes);
	}
}
