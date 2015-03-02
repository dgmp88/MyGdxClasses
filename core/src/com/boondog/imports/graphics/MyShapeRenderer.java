package com.boondog.imports.graphics;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.boondog.imports.math.CircleLogic;


/**
 * This draws anti-aliased lines using the shader.
 * 
 * It's heavily based on the tutorial at the location below
 * 
 * @author george, most of the heavy lifting by mattdesl
 * Derived from
 * https://github.com/mattdesl/lwjgl-basics/wiki/LibGDX-Meshes-Lesson-1
 * https://gist.github.com/mattdesl/5793041
 */
public class MyShapeRenderer implements Disposable {
	Mesh mesh;
	Camera cam;
	ShaderProgram shader;
	
	enum LineType {
		top,
		bottom,
		center
	}
	
	
	//Position attribute - (x, y) 
	public static final int POSITION_COMPONENTS = 2;
	
	//Color attribute - (r, g, b, a)
	public static final int COLOR_COMPONENTS = 4;
	
	//Total number of components for all attributes
	public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;
		
	//The maximum number of vertices our mesh will hold
	public static int MAX_VERTS = 5000; // We define the first square with 4 points, then 2 each for the next two
	
	public static int RENDER_TYPE;
	
	
	//The array which holds all the data, interleaved like so:
	//    x, y, r, g, b, a
	//    x, y, r, g, b, a, 
	//    x, y, r, g, b, a, 
	//    x, y, r, g, b, a, 
	//    ... etc ...
	private float[] verts = new float[MAX_VERTS];
	
	//The index position
	private int idx = 0;
	
	
	// Some useful variables we don't need to allocate every time
	Vector2 a= new Vector2(),b= new Vector2(),c= new Vector2(),d= new Vector2(),e= new Vector2();
	Vector2 dir = new Vector2(), amount = new Vector2(), left = new Vector2(), right = new Vector2();;
	Vector2 norm = new Vector2(),feath = new Vector2();
	Vector2 bL= new Vector2(), bR= new Vector2(), tL= new Vector2(), tR= new Vector2();

	Color tmpCol = new Color();
	
	public MyShapeRenderer () {
		mesh = new Mesh(true, MAX_VERTS, 0, 
				new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
				new VertexAttribute(Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
		shader = createMeshShader();
	}
	
	
	public void setCamera(Camera cam) {
		this.cam = cam;
	}
	
	
	
	protected static ShaderProgram createMeshShader() {
		
		final String VERT_SHADER =  
				"attribute vec2 a_position;\n" +
				"attribute vec4 a_color;\n" +			
				"uniform mat4 u_projTrans;\n" + 
				"varying vec4 vColor;\n" +			
				"void main() {\n" +  
				"	vColor = a_color;\n" +
				"	gl_Position =  u_projTrans * vec4(a_position.xy, 0.0, 1.0);\n" +
				"}";
		
		final String FRAG_SHADER = 
	            "#ifdef GL_ES\n" +
	            "precision mediump float;\n" +
	            "#endif\n" +
				"varying vec4 vColor;\n" + 			
				"void main() {\n" +  
				"	gl_FragColor = vColor;\n" + 
				"}";
		
		ShaderProgram.pedantic = false;
		ShaderProgram shader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);
		String log = shader.getLog();
		if (!shader.isCompiled())
			throw new GdxRuntimeException(log);		
		if (log!=null && log.length()!=0)
			System.out.println("Shader Log: "+log);
		return shader;
	}

	void flush() {
		//if we've already flushed
		if (idx==0)
			return;
		
		//sends our vertex data to the mesh
		mesh.setVertices(verts);
		
		//no need for depth...
		Gdx.gl.glDepthMask(false);
		
		//enable blending, for alpha
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		//number of vertices we need to render
		int vertexCount = (idx/NUM_COMPONENTS);
		
		cam.update();
		
		//start the shader before setting any uniforms
		shader.begin();
		
		//update the projection matrix so our triangles are rendered in 2D
		shader.setUniformMatrix("u_projTrans", cam.combined);
				
		//render the mesh
		mesh.render(shader, RENDER_TYPE, 0, vertexCount);
		
		shader.end();
		
		//re-enable depth to reset states to their default
		Gdx.gl.glDepthMask(true);
		
		//reset index to zero
		idx = 0;
		
		//reset verts
		Arrays.fill(verts, 0);
	}
	
	/** 
	 * Draw lines from points[0] -> points[1], points[1] -> points[2] ... points[n-1] -> points[n]
	 * 
	 * @param points
	 * @param width
	 * @param color
	 */
	public void drawLines(Array<Vector2> points, float width, Color color) {
		float feather = 0.3f * width;
		width = width - (feather);
		for (int i = 0; i < points.size - 1; i ++) {
			drawFullLine(points.get(i), points.get(i+1),width,feather,color);
		}
		flush();
	}
	
	public void drawLine(Vector2 a, Vector2 b, float width, Color color) {
		float feather = 0.3f * width;
		width = width - (feather);
		drawFullLine(a, b,width,feather,color);
		flush();
	}
	
	
	public void drawLines(Array<Vector2> points, float width, float feather, Color color) {
		for (int i = 0; i < points.size - 1; i ++) {
			drawFullLine(points.get(i), points.get(i+1),width,feather,color);
		}
		flush();
	}
	
	public void drawLine(Vector2 a, Vector2 b, float width, float feather, Color color) {
		drawFullLine(a, b,width,feather,color);
		flush();
	}
	
	private void drawFullLine(Vector2 a, Vector2 b, float width, float feather, Color color) {
		RENDER_TYPE = GL20.GL_TRIANGLE_STRIP;

		// Calculate the normal that defines the center rectangle
		norm.set(b);
		norm.sub(a);
		norm.rotate(90);
		norm.setLength(width/2); // Scale to line width
				
		// Calculate the normal that defines feathering
		feath.set(norm);
		feath.setLength(feather/2);

		// ORDER OF RENDERING
		
		
		// 1                    2
				//top feather
		// 3                    4
	
				// center
		
		// 5                    6
				//bottom feather
		// 7                    8
		
		
		// Top feather 
		tL.set(a).add(norm).add(feath);
		tR.set(b).add(norm).add(feath);
		bL.set(a).add(norm);
		bR.set(b).add(norm);
		
		// Draw it
		// The first triangle  (1-2-3)
		putVertex(tL,0, color);
		putVertex(tR,0, color);
		putVertex(bL,1, color);
		
		// The second triangle (2-3-4);
		putVertex(bR,1, color);

		// Center line 
		bL.set(a).sub(norm);
		bR.set(b).sub(norm);
		
		// Draw it
		putVertex(bL,1, color);// 3-4-5
		putVertex(bR,1, color);

		// Bottom feather 
		bL.set(a).sub(norm).sub(feath);
		bR.set(b).sub(norm).sub(feath);
		
		// Draw it
		putVertex(bL,0, color);
		putVertex(bR,0, color);
	}
	
	
	public void drawTriangle(Vector2 a, Vector2 b, Vector2 c, Color color) {
		
		RENDER_TYPE = GL20.GL_TRIANGLES;

		// Draw it
		// The first triangle  (1-2-3)
		putVertex(a,1, color);
		putVertex(b,1, color);
		putVertex(c,1, color);
		
		flush();
	}
	
	public void drawTriangleFeathered(Vector2 a, Vector2 b, Vector2 c, Color color, float feather) {
		
		RENDER_TYPE = GL20.GL_TRIANGLE_FAN;

		
		// a, b, c = outer points 
		// ai, bi, ci = inner points
		
		Vector2 ai, bi, ci;
		Vector2 ab, bc, ca; 
		
		ab = b.cpy().sub(a).rotate(90);
		bc = c.cpy().sub(b).rotate(90);
		ca = a.cpy().sub(c).rotate(90);
		
		ab.scl((1/ab.len())*feather);
		bc.scl((1/bc.len())*feather);
		ca.scl((1/ca.len())*feather);
		
		
		ai = a.cpy().sub(ab);
		ai.sub(ca);
		
		bi = b.cpy().sub(ab);
		bi.sub(bc);
		
		ci = c.cpy().sub(ca);
		ci.sub(bc);

		drawTriangle(a,b,c,color);
	//	drawTriangle(ai,bi,ci,Color.RED);

		flush();
	}
	
	private void putVertex(Vector2 v1, 
			float a1, 
			Color color) {
		if (idx>verts.length - 6) {
			flush();
		}

		// v1
		verts[idx++] = v1.x;
		verts[idx++] = v1.y;
		verts[idx++] = color.r; 	//Color(r, g, b, a)
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = a1;
			
	}
	
	private void putVertex(float x, float y, 
			Color color) {
		if (idx>verts.length - 6) {
			flush();
		}

		// v1
		verts[idx++] = x;
		verts[idx++] = y;
		verts[idx++] = color.r; 	//Color(r, g, b, a)
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;
			
	}
	
	@Override
	public void dispose() {
		mesh.dispose();
		shader.dispose();
	}



	public void emptyRect(float x, float y, float w, float h, Color color, float lineWidth) {
		// Draw a rectangle.
		
		// NOTE: the diagram below accurately depicts edging on the lines to stop odd looking rectangles
		// side lines are shorter than they should be, top/bottoms are longer
		///  h	----- 1 -----
		/// 	|			|
		/// 	2			3		
		/// 	|			|
		///  xy ---- 4 -----w
		
		drawLine(new Vector2(x-lineWidth/2,y+h), new Vector2(x+w+lineWidth/2,y+h), lineWidth, color);
		drawLine(new Vector2(x,y), new Vector2(x,y+h), lineWidth, color);
		drawLine(new Vector2(x+w,y), new Vector2(x+w,y+h), lineWidth, color);
		drawLine(new Vector2(x-lineWidth/2,y), new Vector2(x+w+lineWidth/2,y), lineWidth, color);
	}



	public void fillRect(float x0, float y0, float x1, float y1,
			Color bottomLeft,
			Color bottomRight,
			Color topLeft, 
			Color topRight
			) {		
		RENDER_TYPE = GL20.GL_TRIANGLE_STRIP;
		if (idx > 0) {
			flush();
		}
		putVertex(x0,y0,bottomLeft);
		putVertex(x0+x1,y0,bottomRight);
		putVertex(x0,y0+y1,topLeft);
		putVertex(x0+x1,y0+y1,topRight);
		flush();
	}

	public void fillRect(float x0, float y0, float x1, float y1,
			Color color
			) {		
		RENDER_TYPE = GL20.GL_TRIANGLE_STRIP;
		if (idx > 0) {
			flush();
		}
		putVertex(x0,y0,color);
		putVertex(x0+x1,y0,color);
		putVertex(x0,y0+y1,color);
		putVertex(x0+x1,y0+y1,color);
		flush();
	}
	
	
	public void drawArrow(Vector2 a, Vector2 b, float width, float feather, float headDist, float headWidth, Color color) {
		// Firstly, draw the body of the arrow. B is the front.
		drawLine(a,b,width,feather,color);
		
		// Get the direction vector a->b
		dir.set(a);
		dir.sub(b);
		dir.scl(1/dir.len());
		
		// Get the point down the line that the arrow head lines get to
		d.set(dir);
		d.scl(headDist);
		d.add(b);

		// Now, move d out to the sides 
		amount.set(dir);
		amount.rotate(90);
		amount.scl(headWidth);
		
		right.set(d).add(amount);
		left.set(d).sub(amount);
		
		// Draw the arrow heads to not-quite-b to make it prettier
		c.set(b);
		c.sub(dir.scl(-width*0.2f));
		drawLine(c,left,width,feather,color);
		drawLine(c,right,width,feather,color);
		flush();
	}


	public void drawCircle(Vector2 center, float rad, int segs, Color col) {
		if (segs < 3) {
			throw new IllegalArgumentException("Segs must be > 3");
		}
		RENDER_TYPE = GL20.GL_TRIANGLE_FAN;
		if (idx>0) {
			flush();
		}
		putVertex(center.x,center.y,col);
		for (int i = 0; i < segs+1; i++) {
			a.set(CircleLogic.findPos(center, rad, i*(360f/segs), 0f));
			putVertex(a.x,a.y,col);
		}
		flush();
	}
	
	public void drawCircle(Vector2 center, float rad, float feath, int segs, Color col) {
		if (segs < 3) {
			throw new IllegalArgumentException("Segs must be > 3");
		}
		
		if (rad == 0) {
			return;
		}
		
		drawCircle(center, rad, segs, col);
		
		tmpCol.set(col);
		tmpCol.a = 0;
		
		drawArc(center,rad,rad+feath,segs,col,tmpCol);
		
	}
	
	public void drawArc(Vector2 center, float radInner, float radOuter, int segs, Color innerCol, Color outerCol) {
		RENDER_TYPE = GL20.GL_TRIANGLE_STRIP;
		if (idx>0) {
			flush();
		}

		for (int i = 0; i < segs+1; i++) {			
			a.set(CircleLogic.findPos(center, radOuter, i*(360f/segs), 0f));
			putVertex(a.x,a.y,outerCol);
			a.set(CircleLogic.findPos(center, radInner, i*(360f/segs), 0f));
			putVertex(a.x,a.y,innerCol);
		}
		
		flush();
	}
	
	public void drawArc(Vector2 center, float radInner, float radOuter, float fromDeg, float toDeg, int segs, Color innerCol, Color outerCol) {
		RENDER_TYPE = GL20.GL_TRIANGLE_STRIP;
		if (idx>0) {
			flush();
		}
		
		segs = (int) (segs * (toDeg-fromDeg)/360f);
		float d = (toDeg - fromDeg)/segs;
		
		for (int i = 0; i < segs+1; i++) {			
			a.set(CircleLogic.findPos(center, radOuter, i*(d) + fromDeg, 0f));
			putVertex(a.x,a.y,outerCol);
			a.set(CircleLogic.findPos(center, radInner, i*(d) + fromDeg, 0f));
			putVertex(a.x,a.y,innerCol);
		}
		
		flush();
	}
	
	
}