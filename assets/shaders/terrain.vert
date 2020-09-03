#version 100
attribute vec4 a_position;
attribute vec3 a_normal;
//attribute vec4 a_color;

uniform mat4 u_projTrans;
uniform vec3 u_lightDir;

//varying vec4 v_color;
varying float v_light;
varying vec2 v_texCoords;

void main()
{
	//v_color = a_color;
	v_light = max(dot(a_normal, u_lightDir), 0.0);
	v_texCoords = vec2(a_position.x, a_position.z) / 16.0;
	gl_Position = u_projTrans * a_position;
}
