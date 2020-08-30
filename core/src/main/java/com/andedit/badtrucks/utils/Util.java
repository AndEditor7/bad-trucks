package com.andedit.badtrucks.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Plane.PlaneSide;
import com.badlogic.gdx.utils.Disposable;

/** A useful utilities for this game and LibGDX. */
public final class Util 
{	
	/** A cached screen size. */
	public static final Size screen = new Size();
	/** A cached world screen size. */
	public static final Size world  = new Size();
	
	/** Fast floor for double. */
	public static int floor(final double x) {
		final int xi = (int)x;
		return x < xi ? xi - 1 : xi;
	}
	
	/** Null-safe dispose method for disposable object. */
	public static void disposes(Disposable dis) {
		if (dis != null) dis.dispose();
	}
	
	/** Null-safe disposes method for disposable objects. */
	public static void disposes(Disposable... dis) {
		final int size = dis.length;
		for (int i = 0; i < size; ++i) {
			final Disposable d = dis[i];
			if (d != null) d.dispose();
		}
	}
	
	/** Resets the mouse position to the center of the screen. */
	public static void resetMouse() {
		Gdx.input.setCursorPosition(screen.w/2, screen.h/2);
	}
	
	/** Utility log. */
	public static void log(Object tag, Object obj) {
		if (tag instanceof Class) {
			Gdx.app.log(((Class<?>)tag).getSimpleName(), obj.toString());
		} else {
			Gdx.app.log(tag.toString(), obj.toString());
		}
	}
	
	/** Convenience method that returns a FileType.Internal file handle. */
	public static FileHandle getFile(String path) {
		return Gdx.files.internal(path);
	}
	
	public static int[] locateAttributes(final ShaderProgram shader, final VertexAttributes attributes) {
		final int s = attributes.size();
		final int[] locations = new int[s];
		for (int i = 0; i < s; i++) {
			final VertexAttribute attribute = attributes.get(i);
			locations[i] = shader.getAttributeLocation(attribute.alias);
		}
		return locations;
	}
}
