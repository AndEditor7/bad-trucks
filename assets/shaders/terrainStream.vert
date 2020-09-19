#version 100
#ifdef GL_ES
precision highp float;
#endif

attribute vec4 a_position;
attribute vec4 a_data;

uniform mat4 u_projTrans;

varying vec4 v_data;
varying vec2 v_texCoords;

const float fixAlpha = 255.0/254.0;

void main()
{
	v_data = a_data;
	v_data.a *= fixAlpha;
	v_texCoords = vec2(a_position.x, a_position.z) / 16.0; // 16.0
	gl_Position = u_projTrans * a_position;
}

/* dead codes:
	v_light = mix(max(dot(a_normal, u_lightDir), 0.1), 1.0, 0.2);
*/