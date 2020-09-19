package com.andedit.badtrucks.glutils;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public interface VertContext 
{
	public VertexAttributes getAttrs();
	public ShaderProgram getShader();
	public int getLocation(int i);
	
	/** @return vertexSize/Float.BYTE */
	public default int getFloatSize() {
		return getAttrs().vertexSize>>2;
	}
}
