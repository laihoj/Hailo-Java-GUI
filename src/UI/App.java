package UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;

//import Widgets.Buttons.Button;

public class App {
	public PApplet parent;
	boolean mousePressed, keyPressed;
	public int width, height, CORNER, CENTER;
	int mouseX, mouseY, pmouseX, pmouseY;
	int BACKSPACE, DELETE, ENTER, RETURN, TAB, ESC, DOWN, UP, RIGHT, LEFT;
	char key;

	public Point TOP_LEFT;
	public Point[] TOP_THIRDS;
	public Point[] TOP_FOURTHS;
	public Point[] TOP_FIFTHS;
	public Point[] TOP_SIXTHS;
	public Dimensions BUTTON_DEFAULT_DIMENSIONS;
	public Dimensions FULL_SCREEN;
	public Point A_BIT_TO_THE_LEFT;
	public Point A_BIT_TO_THE_RIGHT;
	public Point A_BIT_UPWARDS;
	public int DEFAULT_TEXT_SIZE;

	
	public void draw() {
		
		mousePressed = parent.mousePressed;
		keyPressed = parent.keyPressed;
		mouseX = parent.mouseX;
		mouseY = parent.mouseY;
		pmouseX = parent.pmouseX;
		pmouseY = parent.pmouseY;
		key = parent.key;
		next();
	}
	
	public void keyEvent(KeyEvent e) {
		switch(e.getAction()) {
			case KeyEvent.PRESS:
				keyPressed();
				break;
			case KeyEvent.RELEASE:
				keyReleased();
				break;

		}
	}
	
	
	static public ArrayList<View> views = new ArrayList<View>();
	static public ArrayList<Command> commands = new ArrayList<Command>();
	static public View active_view = null;
	static public Container navigation_bar = null;

	public App(PApplet parent, boolean initing) {
		this.parent = parent;
		parent.registerMethod("draw", this);
		parent.registerMethod("keyEvent", this);

		width = parent.width;
		height = parent.height;
		CORNER = parent.CORNER;
		CENTER = parent.CENTER;
		mousePressed = parent.mousePressed;
		mouseX = parent.mouseX;
		mouseY = parent.mouseY;
		pmouseX = parent.pmouseX;
		pmouseY = parent.pmouseY;
		BACKSPACE = parent.BACKSPACE;
		DELETE = parent.DELETE;
		ENTER = parent.ENTER;
		RETURN = parent.RETURN;
		TAB = parent.TAB;
		ESC = parent.ESC;
		DOWN = parent.DOWN;
		UP = parent.UP;
		RIGHT = parent.RIGHT;
		LEFT = parent.LEFT;
		keyPressed = parent.keyPressed;
		key = parent.key;

		TOP_LEFT = new Point(0, 0);
		TOP_THIRDS = new Point[] { new Point(TOP_LEFT), new Point(width / 3, 0), new Point(width * 2 / 3, 0) };
		TOP_FOURTHS = new Point[] { new Point(TOP_LEFT), new Point(width / 4, 0), new Point(width * 2 / 4, 0),
				new Point(width * 3 / 4, 0) };
		TOP_FIFTHS = new Point[] { new Point(TOP_LEFT), new Point(width / 5, 0), new Point(width * 2 / 5, 0),
				new Point(width * 3 / 5, 0), new Point(width * 4 / 5, 0) };
		TOP_SIXTHS = new Point[] { new Point(TOP_LEFT), new Point(width / 6, 0), new Point(width * 2 / 6, 0),
				new Point(width * 3 / 6, 0), new Point(width * 4 / 6, 0), new Point(width * 5 / 6, 0) };
		
		BUTTON_DEFAULT_DIMENSIONS = new Dimensions(width / 6 - 1, 100);
		FULL_SCREEN = new Dimensions(width, height);
		A_BIT_TO_THE_LEFT = new Point(-100, 0);
		A_BIT_TO_THE_RIGHT = new Point(100, 0);
		A_BIT_UPWARDS = new Point(0, -100);
		DEFAULT_TEXT_SIZE = 15;
	}
	
	public App(PApplet parent) {
		this.parent = parent;
	}

	
	public View getActiveView() {
		return this.active_view;
	}
	
	public View getNavigationBar() {
		return this.navigation_bar;
	}
	
	static public void add(View view) {
		views.add(view);
	}

	static public void remove(View view) {
		views.remove(view);
	}

	static public void activate(View view) {
		active_view = view;
	}

	public void display() {
		parent.background(255);
		if (getNavigationBar() != null) {
			getNavigationBar().display();
		}
		if (getActiveView() != null) {
			getActiveView().display();
		}
	}

	public void listen() {
		if (navigation_bar != null) {
			navigation_bar.listen();
		}
		if (active_view != null) {
			active_view.listen();
		}
	}

	public void execute() {
		for (Command command : commands) {
			command.execute();
		}
		commands = new ArrayList<Command>();
	}

	 public void next() {
		listen();
		display();
		execute();
	}

	

	

/////////////////////////////////////////////////////////////////////////////////////////////////
	public class View {
		public String name;
		ArrayList<Container> containers = new ArrayList<Container>();
		ArrayList<Widget> widgets = new ArrayList<Widget>();
		MouseListener mouseListener;
		KeyboardListener keyboardListener;

		public View() {
			this("ANONYMOUS_VIEW");
		}

		public View(String name) {
			mouseListener = new MouseListener();
			keyboardListener = new KeyboardListener();
			App.add(this);
			if(App.active_view == null) {
				App.activate(this);
			}
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}

		public String toString() {
			return this.getName();
		}

		public View add(Widget widget) {
			this.widgets.add(widget);
			this.mouseListener.add(widget);
			this.keyboardListener.add(widget);
			return this;
		}

		public Container add(Container container) {
			this.containers.add(container);
			return container;
		}

		public void remove(Widget widget) {
			this.widgets.remove(widget);
		}

		public void display() {
			for (Container container : this.containers) {
				container.display();
			}
			for (Widget widget : this.widgets) {
				if (widget != null) {
					widget.display();
				}
			}
		}

		public void listen() {
			this.mouseListener.listen();
			this.keyboardListener.listen();
			for (Container container : this.containers) {
				container.listen();
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////////
	public class Container extends View {
		public Point point;
		public Dimensions dimensions;

		public Container() {
			this(new Point(TOP_LEFT), new Dimensions(FULL_SCREEN), "ANONYMOUS CONTAINER");
		}

		public Container(Dimensions dimensions) {
			this(new Point(TOP_LEFT), dimensions, "ANONYMOUS CONTAINER");
		}

		public Container(Point point, Dimensions dimensions) {
			this(point, dimensions, "ANONYMOUS CONTAINER");
		}

		public Container(Point point, Dimensions dimensions, String name) {
			this.name = name;
			this.point = point;
			this.dimensions = dimensions;
		}


		public Container add(Widget widget) {
			super.add(widget);
			int margin = 4;
			int Height = this.dimensions.dims[1] - margin * 2;
			int Width = this.dimensions.dims[0] / this.widgets.size() - margin * 2;
			
			widget.setHeight(Height);
			widget.setY(this.point.y + margin); 
			
			for (int i = 0; i < this.widgets.size(); i++) {
				Widget b = this.widgets.get(i);
				b.setWidth(Width);
				b.setX(margin + this.point.x + (Width + margin * 2) * i);
			}
			return this;
		}

		public void display() {
			super.display();
			parent.noFill();
			parent.stroke(0);
			rect(this.point, this.dimensions);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////

	
/////////////////////////////////////////////////////////////////////////////////////////////////
	public class Grid extends Container {
		public Container[][] cells;
	//indices
		public int x, y;
		public Grid(Point point, Dimensions dimensions, int x, int y) {
			this.point = point;
			this.dimensions = dimensions;
			this.x = x;
			this.y = y;
			this.cells = new Container[x][y];
	//Have all cells use this dimensions object, that way changing one changes all
			Dimensions cellDims = new Dimensions(this.dimensions.dims[0] / x, this.dimensions.dims[1] / y);
			for(int j = 0; j < this.y; j++) {
				for(int i = 0; i < this.x; i++) {
					cells[i][j] = new Container(new Point(this.point.x + cellDims.dims[0] * i, this.point.y + cellDims.dims[1] * j), cellDims);
				}
			}
		}
		public Grid(int x, int y) {
			this(TOP_LEFT, FULL_SCREEN, x, y);
		}
		public void display() {
			for(Container[] row: this.cells) {
				for(Container container: row) {
					container.display();
					parent.noFill();
					parent.stroke(0);
					rect(container.point, container.dimensions);
				}
			}
		}
		public void listen() {
			super.listen();
			for(Container[] row: this.cells) {
				for(Container container: row) {
					container.listen();
				}
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	abstract public class Widget extends Observer implements Displayable {
		public Command onMouseHover = null;
		public Command onMouseHoverOver = null;
		public Command onMousePress = null;
		public Command onMouseDrag = null;
		public Command onMouseHold = null;
		public Command onMouseRelease = null;
		public Command onKeyDown = null;
		public Command onKeyHold = null;
		public Command onKeyUp = null;
		
		public Point point;
		public Dimensions dimensions;

		public Widget(Point point, Dimensions dimensions) {
			this.point = point;
			this.dimensions = dimensions;
		}
		public Widget() {
			this.point = null;
			this.dimensions = null;
		}
		public Widget setOnMouseHover(Command command) {
			this.onMouseHover = command;
			return this;
		}
		public Widget setOnMouseHoverOver(Command command) {
			this.onMouseHoverOver = command;
			return this;
		}
		public Widget setOnMousePress(Command command) {
			this.onMousePress = command;
			return this;
		}
		public Widget setOnMouseDrag(Command command) {
			this.onMouseDrag = command;
			return this;
		}
		public Widget setOnMouseHold(Command command) {
			this.onMouseHold = command;
			return this;
		}
		public Widget setOnMouseRelease(Command command) {
			this.onMouseRelease = command;
			return this;
		}
		public Widget setOnKeyDown(Command command) {
			this.onKeyDown = command;
			return this;
		}
		public Widget setOnKeyHold(Command command) {
			this.onKeyHold = command;
			return this;
		}
		public Widget setOnKeyUp(Command command) {
			this.onKeyUp = command;
			return this;
		}
		
		public void onMouseHover() {
			if(onMouseHover != null) {
				onMouseHover.execute();
			}
		}

		public void onMouseHoverOver() {
			if(onMouseHoverOver != null) {
				onMouseHoverOver.execute();
			}
		}

		public void onMousePress() {
			if(onMousePress != null) {
				onMousePress.execute();
			}
		}

		public void onMouseDrag(PVector mouse) {
			if(onMouseDrag != null) {
				onMouseDrag.execute();
			}
		}

		public void onMouseHold() {
			if(onMouseHold != null) {
				onMouseHold.execute();
			}
		}

		public void onMouseRelease() {
			if(onMouseRelease != null) {
				onMouseRelease.execute();
			}
		}

		public void onKeyDown(char c) {
			if(onKeyDown != null) {
				onKeyDown.execute();
			}
		}

		public void onKeyHold(char c) {
			if(onKeyHold != null) {
				onKeyHold.execute();
			}
		}

		public void onKeyUp(char c) {
			if(onKeyUp != null) {
				onKeyUp.execute();
			}
		}

		public boolean isSelected() {
			return selected;
		}

		public Widget setWidth(int Width) {
			this.dimensions.dims[0] = Width;
			return this;
		}
		
		public Widget setHeight(int Height) {
			this.dimensions.dims[1] = Height;
			return this;
		}
		
		public Widget setDiameter(int diameter) {
			this.dimensions.dims[0] = diameter;
			return this;
		}

		public Widget setX(float x) {
			this.point = new Point(x, this.point.y);
			return this;
		}
		
		public Widget setY(float y) {
			this.point = new Point(this.point.x, y);
			return this;
		}
		
		public Widget setPoint(Point point) {
			this.point = point;
			return this;
		}
		public Widget setDimensions(Dimensions dimensions) {
			this.dimensions = dimensions;
			return this;
		}
	}

	abstract public class EllipseWidget extends Widget {
		public EllipseWidget(Point point, Dimensions dimensions) {
			super(new Point(point), new Dimensions(dimensions));
		}
		public boolean isTarget() {
			return isTargetEllipse(this.point, this.dimensions);
		}
	}
	
	abstract public class RectangleWidget extends Widget {
		public RectangleWidget(Point point, Dimensions dimensions) {
			super(new Point(point), new Dimensions(dimensions));
		}
		public RectangleWidget() {
			super();
		}
		public boolean isTarget() {
			return isTargetRect(this.point, this.dimensions);
		}
	}


/***********************************************************************************************/
	
	
	/***********************************************************************************************/
	public class Button extends RectangleWidget implements Command {
		Command command = null;
		String text = "";
		int c = 0;
		int color_pressed = 0;
		int color_base = 0;
		int color_hovering = 0;

		public Button(Command command, String text, int c, Point point, Dimensions dimensions) {
			super(point, dimensions);
			this.command = command;
			this.text = text;
			this.c = c;
		}
		
		public Button(Command command, String text) {
			super(new Point(), new Dimensions());
			this.command = command;
			this.text = text;
			this.c = 255;
		}
		public Button() {
			super();
		}
		public Button setCommand(Command command) {
			this.command = command;
			return this;
		}
		public Button setText(String text) {
			this.text = text;
			return this;
		}
		public Button setColor(int c) {
			this.c = c;
			return this;
		}
		public void display() {

			if (pressed) {
				parent.fill(c);
				parent.stroke(0);
			} else if (hovering) {
				parent.fill(0, 200, 180);
				parent.noStroke();
			} else {
				parent.fill(0, 132, 180);
				parent.noStroke();
			}
			parent.rectMode(CORNER);
			//throwing nullpointer exceptions is probably fine
//			if(point != null && dimensions != null) {
				parent.rect(point.x, point.y, dimensions.dims[0], dimensions.dims[1]);
//			}
			parent.fill(0);
			parent.textAlign(CENTER);
			parent.textSize(15);
//			if(point != null && dimensions != null) {
				parent.text(text, point.x + dimensions.dims[0] / 2, point.y + dimensions.dims[1] / 2);
//			}
		}

		public void execute() {
			this.command.execute();
		}

		// unlike normal commands (which extend AbstractCommand {button extends
		// widget}), queue needs to be implemented
		public void queue() {
			App.commands.add(this);
		}

		public void onMouseRelease() {
			this.queue();
		}
	}

	public class Navigation_Button extends Button {
		public Navigation_Button(Point point, View target) {
			super(new ChangeView(target), target.name, 255, point, BUTTON_DEFAULT_DIMENSIONS);
		}

		public Navigation_Button(View target) {
			super(new ChangeView(target), target.name, 255, TOP_LEFT, BUTTON_DEFAULT_DIMENSIONS);
		}
	}

	/***********************************************************************************************/

	
	public class StateBox extends Widget {
	  //boolean checked = false;
		public Integer[][] states;
		public int state = 0;
		public StateBox(Point point, Dimensions dimensions) {
			super(point,dimensions);
			
		}
		public StateBox(Point point, Dimensions dimensions, Integer[]... states) {
			super(point, dimensions);
			this.states = states;
		}
		public void next() {
			this.state = (this.state + 1) % (states.length) ;
		}
		public void display() {
			parent.noStroke();
			if(state != 0) {
				parent.fill(states[state][0],states[state][1],states[state][2]);
	    	} else if(isTarget()) {
	    		parent.fill(200);
	    		parent.stroke(0);
	    	} else {
	    		parent.fill(127);
	    	}
	    	parent.rectMode(CORNER);
	    	parent.rect(point.x,point.y,dimensions.dims[0],dimensions.dims[1]);
		}
		public boolean isTarget() {
			return isTargetRect(this.point, this.dimensions);
		}
		public void onMouseRelease() {
			this.next();
		}
	}
	
	
	/***********************************************************************************************/
	public class Label extends Widget {
		public String name;
		public int font_size = 15;
		public int modifier = parent.LEFT;
	  //color textColor
		public Label(Point point, String name) {
			super(point,null);
			this.name = name;
		}
		public void display() {
			parent.textSize(font_size);
			parent.textAlign(modifier);
			parent.fill(0);
			parent.text(name, point.x, point.y + font_size);
		}
		public boolean isTarget() {
			return false;
		}
	}
	/***********************************************************************************************/
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	public interface Displayable {
		public void display();
	}
/////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////////////
	interface Listener {
		void listen();
	}
/////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////////
	abstract public class EventListener implements Listener {
		ArrayList<Observer> observers = new ArrayList<Observer>();

		void add(Observer observer) {
			this.observers.add(observer);
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////
	/***********************************************************************************************/

	/***********************************************************************************************/
/////////////////////////////////////////////////////////////////////////////////////////////////
	interface MouseActivities {
		boolean isTarget();

		void onMouseHover();

		void onMouseHoverOver();

		void onMousePress();

		void onMouseHold();

		void onMouseDrag(PVector mouse);

		void onMouseRelease();
	}
/////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////////
	public class MouseListener extends EventListener {
		boolean wasMousePressed = false;
		PVector mousePos = new PVector(0, 0);
		PVector prevMousePos = new PVector(0, 0);
		PVector dragment = new PVector(0, 0);

		MouseListener() {
		}

		public void listen() {
			for (Observer observer : this.observers) {
				observer.hoverMouse(this.mousePos);
			}
			if (mousePressed) {
				this.mousePos = new PVector(mouseX, mouseY);

				if (!wasMousePressed) {
					this.dragment.setMag(0);
					this.prevMousePos = new PVector(mouseX, mouseY);
					for (Observer observer : this.observers) {
						observer.pressMouse(this.mousePos);
					}
				} else {
					for (Observer observer : this.observers) {
						observer.holdMouse();
					}
				}
				if (this.mousePos != this.prevMousePos) {
					this.dragment = PVector.sub(this.prevMousePos, this.mousePos);
					for (Observer observer : this.observers) {
						observer.dragMouse(this.dragment);
					}
				}
			}
			if (wasMousePressed && !mousePressed) {
				for (Observer observer : this.observers) {
					observer.releaseMouse(mousePos, dragment);
				}
			}

			this.prevMousePos = new PVector(mousePos.x, mousePos.y);
			this.wasMousePressed = mousePressed;
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////
	/***********************************************************************************************/

	/***********************************************************************************************/
/////////////////////////////////////////////////////////////////////////////////////////////////
	interface KeyboardActivities {
		void onKeyDown(char c);

		void onKeyHold(char c);

		void onKeyUp(char c);

		boolean isSelected();
	}
/////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////////
	public class KeyboardListener extends EventListener {
		boolean wasKeyPressed = false;
		HashMap<Character, Boolean> keys = new HashMap<Character, Boolean>();
		ArrayList<Character> keysPressed;
		ArrayList<Character> keysReleased;
		String alphabet;

		KeyboardListener() {
			this.alphabet = "";
			for (int i = 32; i < 127; i++) {
				this.alphabet += (char) i;
			}
			// this.alphabet = "1234567890 abcdefghijklmnopqrstuvwxyz";
			this.alphabet += BACKSPACE;
			this.alphabet += DELETE;
			this.alphabet += ENTER;
			this.alphabet += RETURN;
			this.alphabet += TAB;
			this.alphabet += ESC;
			this.alphabet += DOWN;
			this.alphabet += UP;
			this.alphabet += RIGHT;
			this.alphabet += LEFT;
			keysPressed = new ArrayList<Character>();
			keysReleased = new ArrayList<Character>();
		}

		void display() {
			String res = "\nkeys";
			for (char c : keys.keySet()) {
				res += "\n" + c + ": " + keys.get(c);
			}
			parent.stroke(0);
			parent.fill(0);

			String res2 = "\nkeysPressed";
			for (char c : keysPressed) {
				res2 += "\n" + c;
			}

			String res3 = "\nkeysReleased";
			for (char c : keysReleased) {
				res3 += "\n" + c;
			}

			parent.text(res, 0, 0);
			parent.text(res2, 100, 0);
			parent.text(res3, 200, 0);

		}

		public String toString() {
			String res = "\nkeys";
			for (char c : keys.keySet()) {
				res += "\n" + c + ": " + keys.get(c);
			}
			parent.stroke(0);
			parent.fill(0);

			String res2 = "\nkeysPressed";
			for (char c : keysPressed) {
				res2 += "\n" + c;
			}

			String res3 = "\nkeysReleased";
			for (char c : keysReleased) {
				res3 += "\n" + c;
			}
			return res + "\n" + res2 + "\n" + res3;
		}

		void add(char[] cs) {
			for (char c : cs) {
				this.keys.put(c, false);
			}
		}

		void add(char c) {
			this.keys.put(c, false);
		}
		
		Set<Character> getKeys() {
			return this.keys.keySet();
		}

		void updateKeyMap() {
			if (!keyPressed) {
				for (char c : alphabet.toCharArray()) {
					this.keys.put(c, false);
				}
			}
		}

		public void listen() {
			// Figure out which keys are (newly) pressed or released
			updateKeyMap();
			for (char c : this.keysPressed) {
				for (Observer observer : this.observers) {
					observer.pressKey(c);
				}
			}
			this.keysPressed.clear();
			for (char c : this.keysReleased) {
				for (Observer observer : this.observers) {
					observer.releaseKey(c);
				}
			}
			this.keysReleased.clear();
			for (char c : this.keys.keySet()) {
				for (Observer observer : this.observers) {
					if (keys.get(c)) {
						observer.holdKey(c);
					}
				}
			}
		}
	}


	/*
	 * Because there is no boolean keyReleased like there is keyPressed, in order to
	 * keep things consistent, lets utilise these functions
	 */
	public void keyPressed() {
		KeyboardListener listener = App.active_view.keyboardListener;
		for (char c : listener.keys.keySet()) {
			if (key == c) {
				if (!listener.keys.get(c)) {
					listener.keysPressed.add(c);
				}
				listener.keys.put(c, true);
			}
		}
	}

	public void keyReleased() {
		KeyboardListener listener = App.active_view.keyboardListener;
		for (char c : listener.keys.keySet()) {
			if (key == c) {
				listener.keys.put(c, false);
				listener.keysReleased.add(c);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////

//some handy keyboard related methods
/////////////////////////////////////////////////////////////////////////////////////////////////
	interface Spacebar {
		void onSpacebar();
	}

	interface Enter {
		void onEnter();
	}

	interface Backspace {
		void onBackspace();
	}

/////////////////////////////////////////////////////////////////////////////////////////////////
	/***********************************************************************************************/

	/***********************************************************************************************/
/////////////////////////////////////////////////////////////////////////////////////////////////
	abstract public class Observer implements MouseActivities, KeyboardActivities, Selectable {
		public boolean pressed, hovering, selected;
		public PVector pos = new PVector(0, 0);
		public String key;
		
		/* Mouse stuff */
		public void hoverMouse(PVector mouse) {
			if (this.isTarget()) {
				this.hovering = true;
				this.onMouseHover();
			} else {
				if (this.hovering) {
					this.onMouseHoverOver();
				}
				this.hovering = false;
			}
		}

		public void pressMouse(PVector mouse) {
			if (this.isTarget()) {
				this.pressed = true;

			}
			if (this.isSelected()) {
				this.onMousePress();
			}
		}

		public void dragMouse(PVector mouse) {
			if (this.pressed && PVector.dist(new PVector(pmouseX, pmouseY), new PVector(mouseX, mouseY)) > 0) {
				this.onMouseDrag(mouse);
			}
		}

		public void holdMouse() {
			if (this.isSelected()) {
				this.onMouseHold();
			}
		}

		public void releaseMouse(PVector mouse, PVector dragment) {
			if (!this.isTarget() && this.selected) {
				this.deselect();
			}
			if (this.pressed && this.isTarget()) {
				this.onMouseRelease();
				this.select();
			}
			this.pressed = false;
		}

		/* Keyboard stuff */
		public void pressKey(char c) {
			if (this.isSelected()) {
				this.onKeyDown(c);
			}
		}

		public void holdKey(char c) {
			if (this.isSelected()) {
				this.onKeyHold(c);
			}
		}

		public void releaseKey(char c) {
			if (this.isSelected()) {
				this.onKeyUp(c);
			}
		}

		/* Selectable implementations */
		public void select() {
			this.selected = true;
		}

		public void deselect() {
			this.selected = false;
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////
	/***********************************************************************************************/

	/***********************************************************************************************/
//Activates another view
	public class ChangeView extends AbstractCommand {
		public View target;

		public ChangeView(View target) {
			this.target = target;
		}

		public void execute() {
			System.out.println("View changing from " + App.active_view.toString() + " to " + this.target);
			App.activate(target);
		}
	}

	/***********************************************************************************************/

/////////////////////////////////////////////////////////////////////////////////////////////////
	public interface Command {
		boolean executed = false;

		void execute();

		void queue();
	}

	abstract public class AbstractCommand implements Command {
		public void queue() {
			App.commands.add(this);
		}
	}

	public class DoNothing extends AbstractCommand {
		public DoNothing() {
		}

		public void execute() {
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////////
	interface Selectable {
		public void select();

		public void deselect();
	}
/////////////////////////////////////////////////////////////////////////////////////////////////

	public float limit(float value, float min, float max) {
		return parent.min(max, parent.max(min, value));
	}

	public boolean isTargetRect(Point point, Dimensions dimensions) {
		return mouseX > point.x && mouseX < point.x + dimensions.dims[0] && mouseY > point.y
				&& mouseY < point.y + dimensions.dims[1];
	}

//untested
	public boolean isTargetEllipse(Point point, Dimensions dimensions) {
		return dimensions.dims[0] / 2 > parent.sqrt((float) (Math.pow(point.x + dimensions.dims[0] / 2 - mouseX, 2)
				+ Math.pow(point.y + dimensions.dims[0] / 2 - mouseY, 2)));
	}

	public void ellipse(Point point, Dimensions dimensions) {
		parent.ellipseMode(CORNER);
		parent.ellipse(point.x, point.y, dimensions.dims[0], dimensions.dims[0]);
//ellipse(point.x - dimensions.dims[0]/2, point.y - dimensions.dims[0]/2, dimensions.dims[0], dimensions.dims[0]);
	}

	public void rect(Point point, Dimensions dimensions) {
		parent.rect(point.x, point.y, dimensions.dims[0], dimensions.dims[1]);
	}
}