package com.andedit.badtrucks.handles;

import com.andedit.badtrucks.mesh.verts.TerrainVert;
import com.andedit.badtrucks.utils.Camera;
import com.andedit.badtrucks.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

/** Static shader for terrains. */
public final class Shaders 
{
	public static final String projTran = "u_projTrans";
	
	public static ShaderProgram shader;
	public static int[] locations;
	
	private static final Vector3 noam = new Vector3();
	
	public static void bind(Camera cam) {
		shader.bind();
		shader.setUniformMatrix(projTran, cam.combined);
		if (Gdx.input.isKeyJustPressed(Keys.P)) {
			shader.setUniformf("u_lightDir", noam.set(cam.direction));
		}
	}
	
	public static void getShader(AssetManager asset) {
		shader = asset.get("shaders/terrain.vert", ShaderProgram.class);
		locations = Util.locateAttributes(shader, TerrainVert.attributes);
	}
}
