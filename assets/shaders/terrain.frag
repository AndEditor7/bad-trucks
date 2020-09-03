#version 100
#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

//varying vec4 v_color;
varying vec3 v_normal;
varying vec3 v_lightDir;
varying vec2 v_texCoords;

void main()
{
	gl_FragColor = texture2D(u_texture, v_texCoords) * max(dot(v_normal, v_lightDir), 0.0);
}
