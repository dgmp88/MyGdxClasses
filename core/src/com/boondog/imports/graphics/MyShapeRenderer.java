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
	
	//Position attribute - (x, y) 
	public static final int POSITION_COMPONENTS = 2;
	
	//Color attribute - (r, g, b, a)
	public static final int COLOR_COMPONENTS = 4;
	
	//Total number of components for all attributes
	public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;
		
	//The maximum number of vertices our mesh will hold
	public static int MAX_VERTS = 50000;
	
	public static int RENDER_TYPE;
	
	private int n_verts;
	
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

	Color tmpCol1 = new Color(), tmpCol2 = new Color(), zeroAlpha = new Color();
	
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

	public void flush() {
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

	
	private void uncheckedTriangle(float aX, float aY, float bX, float bY, float cX, float cY, 
			Color aC, Color bC, Color cC) {
		putVertex(aX, aY, aC);
		putVertex(bX, bY, bC);
		putVertex(cX, cY, cC);
	}

	public void fillTriangle(float aX, float aY, float bX, float bY, float cX, float cY, 
			Color aC, Color bC, Color cC) {
		setType(GL20.GL_TRIANGLES);
		checkMaxVerts(3);
		putVertex(aX, aY, aC);
		putVertex(bX, bY, bC);
		putVertex(cX, cY, cC);
	}
	
	private void uncheckedRect(float x0, float y0, float x1, float y1,
			Color bottomLeft,
			Color bottomRight,
			Color topLeft, 
			Color topRight
			) {
		uncheckedTriangle(x0, y0, x0+x1, y0, x0, y0+y1, bottomLeft, bottomRight, topLeft);
		uncheckedTriangle(x0+x1, y0, x0, y0+y1, x0+x1, y0+y1, bottomRight, topLeft, topRight);
	}
	
	public void fillRect(float x0, float y0, float x1, float y1,
			Color bottomLeft,
			Color bottomRight,
			Color topLeft, 
			Color topRight
			) {
		setType(GL20.GL_TRIANGLES);
		checkMaxVerts(6);
		uncheckedRect(x0, y0, x1, y1, bottomLeft, bottomRight, topLeft, topRight);
	}
	
	
	public void fillRect(float x0, float y0, float x1, float y1,
			Color color
			) {
		
		fillRect(x0, y0, x1, y1, color, color, color, color);
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
	}


	public void drawLine(Vector2 a, Vector2 b, float width, float feather,
			Color color) {
		drawFullLine(a, b,width,feather,color);
	}
	
	public void drawLine(float x1, float y1, float x2, float y2, float width, float feather,
			Color color) {
		a.set(x1, y1);
		b.set(x2, y2);
		drawFullLine(a, b,width,feather,color);
	}

	public void drawLines(Array<Vector2> points, float width, float feather,
			Color color) {
		for (int i = 0; i < points.size - 1; i ++) {
			drawFullLine(points.get(i), points.get(i+1),width,feather, color);
		}		
	}
	
	
	private void drawFeatheredFullLine(Vector2 a, Vector2 b, float width, float feather, Color color) {
		checkMaxVerts(3*6);

		// Calculate the normal that defines the center rectangle
		norm.set(b);
		norm.sub(a);
		norm.rotate(90);
		norm.setLength(width/2); // Scale to line width
				
		// Calculate the normal that defines feathering
		feath.set(norm);
		feath.setLength(feather/2);
		
		zeroAlpha.set(color);
		zeroAlpha.a = 0;

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
		// Top feather  (1-2-3)
		uncheckedTriangle(tL.x, tL.y, tR.x, tR.y, bL.x, bL.y, zeroAlpha, zeroAlpha, color);
		uncheckedTriangle(tR.x, tR.y, bL.x, bL.y, bR.x, bR.y, zeroAlpha, color, color);

		// Center line 
		tL.set(a).add(norm);
		tR.set(b).add(norm);
		bL.set(a).sub(norm);
		bR.set(b).sub(norm);
		
		// Draw it
		uncheckedTriangle(tL.x, tL.y, tR.x, tR.y, bL.x, bL.y, color, color, color);
		uncheckedTriangle(tR.x, tR.y, bL.x, bL.y, bR.x, bR.y, color, color, color);


		// Bottom feather 
		tL.set(a).sub(norm);
		tR.set(b).sub(norm);
		bL.set(a).sub(norm).sub(feath);
		bR.set(b).sub(norm).sub(feath);
		
		// Draw it
		uncheckedTriangle(tL.x, tL.y, tR.x, tR.y, bL.x, bL.y, color, color, zeroAlpha);
		uncheckedTriangle(tR.x, tR.y, bL.x, bL.y, bR.x, bR.y, color, zeroAlpha, zeroAlpha);
	}
	
	
	private void drawFullLine(Vector2 a, Vector2 b, float width, float feather, Color color) {
		setType(GL20.GL_TRIANGLES);			
		if (feather > 0) {
			drawFeatheredFullLine(a, b, width, feather, color);
		} else {
			 drawLine(a, b, width, color);
		}
	}
	
	public void drawLine(Vector2 a, Vector2 b, float width, Color color) {
		setType(GL20.GL_TRIANGLES);			

		// Calculate the normal that defines the center rectangle
		norm.set(b);
		norm.sub(a);
		norm.rotate(90);
		norm.setLength(width/2); // Scale to line width
		
		// 1                    2
				//top feather
		// 3                    4

		// Top feather 
		tL.set(a).add(norm);
		tR.set(b).add(norm);
		bL.set(a).sub(norm);
		bR.set(b).sub(norm);
		
		uncheckedTriangle(tL.x, tL.y, tR.x, tR.y, bL.x, bL.y, color, color, color);
		uncheckedTriangle(tR.x, tR.y, bL.x, bL.y, bR.x, bR.y, color, color, color);	
	}
	
	public void drawLineShapePart(Vector2 center, int verts, float innerPos, float outerPos, float rotation, Color innerCol, Color outerCol) {
		setType(GL20.GL_TRIANGLE_STRIP);			
		
		n_verts = (verts+1)*2 + 2;
		checkMaxVerts(n_verts);		
						
		a.set(CircleLogic.findPos(center, innerPos, 0*(360f/verts)+rotation, 0f));
		
		// We place the vertex twice at the start and at the end to ensure no crosover from the last shape
		putVertex(a.x,a.y,innerCol);
		putVertex(a.x,a.y,innerCol);
		for (int i = 0; i < verts+1; i++) {			
			a.set(CircleLogic.findPos(center, outerPos, i*(360f/verts)+rotation, 0f));
			putVertex(a.x,a.y,outerCol);
			a.set(CircleLogic.findPos(center, innerPos, i*(360f/verts)+rotation, 0f));
			putVertex(a.x,a.y,innerCol);
		}		
		putVertex(a.x,a.y,innerCol);		
	}
	
	public void drawLineShape(Vector2 center, int verts, float size, float lineWidth, float fadeWidth, float rotation, Color color) {
		setType(GL20.GL_TRIANGLE_STRIP);			
		
		zeroAlpha.set(color);
		zeroAlpha.a = 0f;
		
		float innerPos = size - lineWidth/2f, outerPos = size + lineWidth/2f;
		drawLineShapePart(center, verts, innerPos, outerPos, rotation, color, color);
		
		
		float innerFadePosInner = innerPos - fadeWidth, innerFadePosOuter = innerPos;
		drawLineShapePart(center, verts, innerFadePosInner, innerFadePosOuter, rotation, zeroAlpha, color);
		
		float outerFadePosInner = outerPos, outerFadePosOuter = outerPos + fadeWidth;
		drawLineShapePart(center, verts, outerFadePosInner, outerFadePosOuter, rotation, color, zeroAlpha);
	}
		

	public void drawCircle(float x, float y, float rad, int segs,
			Color col) {
		a.set(x,y);
		drawCircle(a, rad, segs, col);
	}
	

	public void drawCircle(Vector2 center, float rad, int segs, Color col) {
		if (segs < 3) {
			throw new IllegalArgumentException("Segs must be > 3");
		}
		drawArc(center, 0.001f, rad, segs, col, col);
	}
	
	public void drawCircle(Vector2 center, float rad, float feath, int segs, Color col) {
		if (segs < 3) {
			throw new IllegalArgumentException("Segs must be > 3");
		}
		
		if (rad == 0) {
			return;
		}
		
		drawCircle(center, rad, segs, col);
		
		tmpCol1.set(col);
		tmpCol1.a = 0;
		
		drawArc(center,rad,rad+feath,segs,col,tmpCol1);
		
	}
	
	/**
	 * Complete arc
	 * @param center
	 * @param radInner
	 * @param radOuter
	 * @param segs
	 * @param innerCol
	 * @param outerCol
	 */
	public void drawArc(Vector2 center, float radInner, float radOuter, int segs, Color innerCol, Color outerCol) {		
		setType(GL20.GL_TRIANGLE_STRIP);
		n_verts = (segs+1)*2 + 2;
		checkMaxVerts(n_verts);
		a.set(CircleLogic.findPos(center, radOuter, 0*(360f/segs), 0f));
		putVertex(a.x,a.y,outerCol);
		putVertex(a.x,a.y,outerCol);
		for (int i = 0; i < segs+1; i++) {			
			a.set(CircleLogic.findPos(center, radOuter, i*(360f/segs), 0f));
			putVertex(a.x,a.y,outerCol);
			a.set(CircleLogic.findPos(center, radInner, i*(360f/segs), 0f));
			putVertex(a.x,a.y,innerCol);
		}		
		putVertex(a.x,a.y,innerCol);
	}
	
	private void checkMaxVerts(int n_verts) {
		if (idx+n_verts>=MAX_VERTS) {
			flush();
		}		
	}


	/**
	 * Everything calls this one. Draw an arc, most specific way.
	 * @param center
	 * @param radInner
	 * @param radOuter
	 * @param fromDeg
	 * @param toDeg
	 * @param segs
	 * @param innerCol
	 * @param outerCol
	 */
	public void drawPartialArc(Vector2 center, float radInner, float radOuter, float fromDeg, float toDeg, int segs, Color innerCol, Color outerCol) {
		setType(GL20.GL_TRIANGLE_STRIP);
		
		n_verts = (segs+1)*2 + 1;
		checkMaxVerts(n_verts);

		segs = (int) (segs * (toDeg-fromDeg)/360f);
		if (segs < 5) { // Otherwise the rounding draws nothing
			segs = 5;
		}
		float d = (toDeg - fromDeg)/segs;
		
		tmpCol1.set(outerCol);
		tmpCol1.a = 0;
		a.set(CircleLogic.findPos(center, radOuter, fromDeg, 0f));
		putVertex(a.x,a.y,tmpCol1);
		putVertex(a.x,a.y,tmpCol1);
		
		for (int i = 0; i < segs+1; i++) {
			if (toDeg < 359 && i == (segs)) { // set the alpha to 0 for nice smooth starts and ends.
				tmpCol1.set(outerCol);
				tmpCol1.a = 0;
				a.set(CircleLogic.findPos(center, radOuter, i*(d) + fromDeg, 0f));
				putVertex(a.x,a.y,tmpCol1);
				
				tmpCol1.set(innerCol);
				tmpCol1.a = 0;
				a.set(CircleLogic.findPos(center, radInner, i*(d) + fromDeg, 0f));
				putVertex(a.x,a.y,tmpCol1);
			} else {
				a.set(CircleLogic.findPos(center, radOuter, i*(d) + fromDeg, 0f));
				putVertex(a.x,a.y,outerCol);
				a.set(CircleLogic.findPos(center, radInner, i*(d) + fromDeg, 0f));
				putVertex(a.x,a.y,innerCol);
			}
		}
		putVertex(a.x,a.y,innerCol); // Prevent drawing again.
		putVertex(a.x,a.y,innerCol); // Prevent drawing again.
	}
	
	public void drawSmoothPartialArc(Vector2 center, float radInner, float radOuter, float fromDeg, float toDeg, int segs, float feath, Color col) {
		tmpCol1.set(col);
		tmpCol1.a = 0;
		
		// Draw an center bit.
		drawPartialArc(center,radInner,radOuter,fromDeg,toDeg,segs,col, col);

		// Draw outer bit
		drawPartialArc(center,radOuter,radOuter+feath,fromDeg,toDeg,segs,col, tmpCol1);

		// Draw inner bit
		drawPartialArc(center,radInner-feath,radInner,fromDeg,toDeg,segs,tmpCol1, col);
	}
	
	private void setType(int type) {
		if (RENDER_TYPE!=type){
			flush();
			RENDER_TYPE = type;
			begin();
		}
		
	}
	
	public void begin() {
		idx = 0;
	}
	
	public void end() {
		flush();
	}


}