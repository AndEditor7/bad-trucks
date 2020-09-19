package com.andedit.badtrucks.glutils;

import com.badlogic.gdx.utils.Disposable;

public interface Vertex extends Disposable 
{
	public void setVertices(float[] vertices, int offset, int count);
	public void bind();
	public void unbind();
}
