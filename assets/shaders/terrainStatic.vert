#version 100
#ifdef GL_ES
precision highp float;
#endif

attribute vec4 a_position;

uniform mat4 u_projTrans;

varying vec2 v_texCoords;

void main()
{
	v_texCoords = vec2(a_position.x, a_position.z) / 16.0; // 16.0
	gl_Position = u_projTrans * a_position;
}

/* dead codes:
	v_light = mix(max(dot(a_normal, u_lightDir), 0.1), 1.0, 0.2);
*/