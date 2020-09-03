#version 100
#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

//varying vec4 v_color;
varying float v_light;
varying vec2 v_texCoords;

void main()
{
	gl_FragColor = texture2D(u_texture, v_texCoords) * v_light;
}
