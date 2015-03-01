package com.boondog.imports.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;

public class BodyBuilderBody {
	Array<Vector2> vertices;
	ShortArray triangulation;
	
	public BodyBuilderBody(Array<Vector2> vertices, ShortArray triangulation) {
		this.vertices = vertices;
		this.triangulation = triangulation;
	}
	
	public BodyBuilderBody(Array<Vector2> vertices) {
		this(vertices,null);
	}

	public Array<Vector2> getVertices() {
		return vertices;
	}

	public void setVertices(Array<Vector2> vertices) {
		this.vertices = vertices;
	}

	public ShortArray getTriangulation() {
		return triangulation;
	}

	public void setTriangulation(ShortArray triangulation) {
		this.triangulation = triangulation;
	}
	
	
}
