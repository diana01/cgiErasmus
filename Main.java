import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.nativewindow.WindowClosingProtocol.WindowClosingMode;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;

public class Main implements GLEventListener{
	
	protected static final int DEFAULT_WIDTH = 800;
	protected static final int DEFAULT_HEIGHT = 560;
	
	private static GLWindow window;
	private static Animator animator;
	
	public Main(){
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Display display = NewtFactory.createDisplay(null);
		Screen screen = NewtFactory.createScreen(display, 0);
		GLProfile glp = GLProfile.get(GLProfile.GL3);
		GLCapabilities caps = new GLCapabilities(glp);
		window = GLWindow.create(screen, caps);
		window.setTitle("Computer Graphics and Image Processing Project");
		window.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		window.setUndecorated(false);
        window.setAlwaysOnTop(false);
        window.setFullscreen(false);
        window.setPointerVisible(true);
        window.confinePointer(false);
        window.setVisible(true);
		window.setDefaultCloseOperation(WindowClosingMode.DISPOSE_ON_CLOSE);
		
		Main event = new Main();
	    window.addGLEventListener(event);
	    animator = new Animator(window);
	    animator.start();  
	    
	    window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(final WindowEvent e) {
                System.out.println("GLWindow.destroyNotify");
            }

            @Override
            public void windowDestroyed(final WindowEvent e) {
                System.out.println("GLWindow.destroyed");
                animator.stop();
            }
        });
	    
	        
	        
//		JPanel toolbarPanel = new JPanel();
//		ImageIcon addObjectIcon = new ImageIcon("icons/addObjectIcon.png");
//		JButton addObjectButton = new JButton(addObjectIcon);
//		toolbarPanel.add(addObjectButton);
//		
//		
//		JPanel mainPanel = new JPanel();
//		mainPanel.setBackground(Color.WHITE);
//		frame.add(toolbarPanel, BorderLayout.NORTH);
//		frame.add(mainPanel, BorderLayout.CENTER);

	       
	}
	
	float vertices[] = {
		     0.0f,  0.5f, // Vertex 1 (X, Y)
		     0.5f, -0.5f, // Vertex 2 (X, Y)
		    -0.5f, -0.5f  // Vertex 3 (X, Y)
		};
	private FloatBuffer verticesBuffer = FloatBuffer.wrap(vertices);
	String[] fragmentShaderSource = new String[1];
	private IntBuffer vbo = IntBuffer.allocate(1);
	private IntBuffer vao = IntBuffer.allocate(1);

	@Override
	public void display(GLAutoDrawable drawable) {
		System.out.println("display");
		GL3 gl = drawable.getGL().getGL3();
		gl.glClearColor(0.827f, 0.827f, 0.827f, 1f);
		gl.glClearDepthf(1f);
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		gl.glDrawArrays(GL3.GL_TRIANGLES, 0, 3);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		gl.glGenBuffers(1, vbo); // Generate 1 buffer
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo.get(0)); //To upload the actual data to it you first have to make it the active object
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, 2*3*Float.BYTES , verticesBuffer, GL3.GL_STATIC_DRAW);
		int vertexShader = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
		String[] vertexShaderSource = new String[1];
		vertexShaderSource[0] = "#version 150/n"
				+ "in vec2 position;/n"
				+ "void main()/n"
				+ "{/n"
				+ "gl_Position = vec4(position, 0.0, 1.0);/n"
				+ "}";
		gl.glShaderSource(vertexShader, 1, vertexShaderSource, null);
		gl.glCompileShader(vertexShader);
		IntBuffer status = IntBuffer.allocate(1);
		gl.glGetShaderiv(vertexShader, GL3.GL_COMPILE_STATUS, status);
		System.out.println(status.get(0)==GL3.GL_TRUE);
		int fragmentShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
		String[] fragmentShaderSource = new String[1];
		fragmentShaderSource[0] = "#version 150/n"
				+ "out vec4 outColor;/n"
				+ "void main()/n"
				+ "{/n"
				+ "outColor = vec4(1.0, 1.0, 1.0, 1.0);/n"
				+ "}";
		gl.glShaderSource(fragmentShader, 1, fragmentShaderSource , null);
		gl.glCompileShader(fragmentShader);
		int shaderProgram = gl.glCreateProgram();
		gl.glAttachShader(shaderProgram, vertexShader);
		gl.glAttachShader(shaderProgram, fragmentShader);
		gl.glBindFragDataLocation(shaderProgram, 0, "outColor");
		gl.glLinkProgram(shaderProgram);
		gl.glUseProgram(shaderProgram);
		int posAttrib = gl.glGetAttribLocation(shaderProgram, "position");
//		gl.glVertexAttribPointer(posAttrib, 2, GL3.GL_FLOAT, GL3.GL_FALSE, 0, 0);
		gl.glEnableVertexAttribArray(posAttrib);
		gl.glGenVertexArrays(1, vao);
		gl.glBindVertexArray(vao.get(0));
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

}
