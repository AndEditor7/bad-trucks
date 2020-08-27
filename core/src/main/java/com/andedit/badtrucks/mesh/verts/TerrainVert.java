package com.andedit.badtrucks.mesh.verts;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

// Needs update comments after attribute change.
public final class TerrainVert 
{
	/** 3 Position, 1 Color */
	public static final VertexAttributes attributes = new VertexAttributes(
				VertexAttribute.Position(),
				VertexAttribute.ColorPacked()
			);
	
	/** 16 bytes in a single vertex with 4 float components. */ 
	public static final int byteSize = attributes.vertexSize;
	
	/** 4 floats in a single vertex. */ 
	public static final int floatSize = byteSize/Float.BYTES;
}
