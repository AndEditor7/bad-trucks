#ifdef GL_ES
#define LOWP lowp
precision lowp float; // mediump
#else
#define LOWP
#endif

uniform LOWP sampler2D u_defaultTex;
uniform LOWP sampler2D u_rTexture; // r
uniform LOWP sampler2D u_gTexture; // g

varying LOWP vec4 v_data;
varying LOWP vec2 v_texCoords;

void main()
{
	float backTextureArmout = 1.0 - (v_data.r + v_data.g);
	vec4 defaultCol = texture2D(u_defaultTex, v_texCoords) * backTextureArmout;
	
	vec4 rColor = texture2D(u_rTexture, v_texCoords) *  v_data.r;
	vec4 gColor = texture2D(u_gTexture, v_texCoords) *  v_data.g;
	// TODO: Add one more texture.
	
	vec4 finalCol = defaultCol + rColor + gColor;
	gl_FragColor = finalCol * v_data.a;
}
