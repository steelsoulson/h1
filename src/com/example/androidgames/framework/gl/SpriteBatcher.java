package com.example.androidgames.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.Vector2;

public class SpriteBatcher {
	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices vertices;
	int numSprites;
	
	public SpriteBatcher(GLGraphics glGraphics, int maxSprites) {
		this.verticesBuffer = new float[maxSprites * 4 * 4];
		this.vertices = new Vertices(glGraphics, maxSprites * 4, maxSprites * 6, false, true);
		
		this.bufferIndex = 0;
		this.numSprites = 0;
		
		short[] indices = new short[maxSprites * 6];

		for (int i = 0, j = 0; i < indices.length;  i += 6, j += 4) {
			indices[i + 0] = (short)(j + 0);
			indices[i + 1] = (short)(j + 1);
			indices[i + 2] = (short)(j + 2);
			indices[i + 3] = (short)(j + 2);
			indices[i + 4] = (short)(j + 3);
			indices[i + 5] = (short)(j + 0);
		}
		vertices.setIndices(indices, 0, indices.length);
	}
	
	public void beginBatch(Texture texture) {
		texture.bind();
		numSprites = 0;
		bufferIndex = 0;
	}
	
	public void endBatch() {
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
		vertices.unbind();
	}
	
	public void drawSprite(float x, float y, float width, float height, TextureRegion region) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;
		
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;
		
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		
		numSprites++;
	}
	
	public void drawSprite(float x, float y, float width, float height, 
			float angle, TextureRegion region) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		
		Vector2 point1 = new Vector2(-halfWidth, -halfHeight);
		point1.rotate(angle).add(x, y);
		Vector2 point2 = new Vector2(halfWidth, -halfHeight);
		point2.rotate(angle).add(x, y);
		Vector2 point3 = new Vector2(halfWidth, halfHeight);
		point3.rotate(angle).add(x, y);
		Vector2 point4 = new Vector2(-halfWidth, halfHeight);
		point4.rotate(angle).add(x, y);
		
		verticesBuffer[bufferIndex++] = point1.x;
		verticesBuffer[bufferIndex++] = point1.y;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = point2.x;
		verticesBuffer[bufferIndex++] = point2.y;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = point3.x;
		verticesBuffer[bufferIndex++] = point3.y;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;
		
		verticesBuffer[bufferIndex++] = point4.x;
		verticesBuffer[bufferIndex++] = point4.y;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		
		numSprites++;
	}
}
