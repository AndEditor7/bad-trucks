#version 100
#ifdef GL_ES
precision highp float;
#endif

uniform sampler2D u_grassTex;

varying vec2 v_texCoords;

void main()
{
	gl_FragColor = texture2D(u_grassTex, v_texCoords);
}
