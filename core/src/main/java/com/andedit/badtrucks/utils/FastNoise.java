package com.andedit.badtrucks.utils;

import static com.badlogic.gdx.math.Interpolation.smoother;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class FastNoise {
	
	private static final Vector2[] GRAD_2D = {
			new Vector2(-1, -1), new Vector2(1, -1), new Vector2(-1, 1), new Vector2(1, 1),
			new Vector2(0, -1),  new Vector2(-1, 0), new Vector2(0, 1),  new Vector2(1, 0),
	};
	
	private static float GradCoord2D(int seed, int x, int y, float xd, float yd) {
		int hash = seed;
		hash ^= 1619 * x;
		hash ^= 31337 * y;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		Vector2 g = GRAD_2D[hash & 7];

		return xd * g.x + yd * g.y;
	}
	
	public static float getPerlin(int seed, float x, float y) 
	{
		final int x0 = MathUtils.floor(x);
		final int y0 = MathUtils.floor(y);
		final int x1 = x0 + 1;
		final int y1 = y0 + 1;

		final float xs, ys;
		xs = smoother.apply(x - x0);
		ys = smoother.apply(y - y0);

		final float xd0 = x - x0;
		final float yd0 = y - y0;
		final float xd1 = xd0 - 1f;
		final float yd1 = yd0 - 1f;

		final float xf0 = MathUtils.lerp(GradCoord2D(seed, x0, y0, xd0, yd0), GradCoord2D(seed, x1, y0, xd1, yd0), xs);
		final float xf1 = MathUtils.lerp(GradCoord2D(seed, x0, y1, xd0, yd1), GradCoord2D(seed, x1, y1, xd1, yd1), xs);

		return MathUtils.lerp(xf0, xf1, ys);
	}
}
