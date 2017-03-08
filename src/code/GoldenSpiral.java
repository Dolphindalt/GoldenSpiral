package code;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

public class GoldenSpiral extends JFrame implements Runnable
{

	private enum RectPos
	{
		Left, Top, Right, Down;
	}
	
	private static final long serialVersionUID = 1L;
	
	private boolean running;
	private Thread thread;
	
	private Canvas canvas;
	private Random r;
	
	private BufferStrategy bs;
	private Graphics2D g;
	
	private Color[] colors;
	private int cp = 0;
	
	private List<Point> points;
	
	private static final float PHI = (float) (1 + Math.sqrt(5))/2;
	
	public GoldenSpiral()
	{
		this.r = new Random();
		this.colors = new Color[20];
		for(int i = 0; i < colors.length; i++)
			this.colors[i] = new Color(r.nextInt(255)+1, r.nextInt(255)+1, r.nextInt(255)+1);
		this.points = new ArrayList<Point>();
		
		this.setName("Golden Spiral");
		this.setTitle(getName());
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.canvas = new Canvas();
		this.canvas.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.add(canvas);
		
		this.pack();
		this.setVisible(true);
	}
	
	public void render()
	{
		if(bs == null)
		{
			canvas.createBufferStrategy(3);
			bs = canvas.getBufferStrategy();
		}
		g = (Graphics2D) bs.getDrawGraphics();
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		RectPos rp;
		float rw, rh;
		if (this.getWidth() > this.getHeight())
		{
			rp = RectPos.Left;
			if(this.getWidth() / this.getHeight() > PHI)
			{
				rh = this.getHeight();
				rw = rh * PHI;
			} else
			{
				rw = this.getWidth();
				rh = rw / PHI;
			}
		}
		else
		{
			rp = RectPos.Top;
			if (this.getHeight() / this.getWidth() > PHI)
			{
				rw = this.getWidth();
				rh = rw * PHI;
			} else
			{
				rh = this.getHeight();
				rw = rh / PHI;
			}
		}
		
		float x = (this.getWidth() - rw) / 2;
		float y = (this.getHeight() - rh) / 2;
		
		drawRectangles(g, new ArrayList<Point>(), x, y, rw, rh, rp);
		drawTrueSpiral(g);
		
		g.dispose();
		bs.show();
	}
	
	private void drawRectangles(Graphics2D g, List<Point> points, float x, 
			float y, float w, float h, RectPos rp)
	{
		if ((w < 0.1) || (h < 0.1)) 
		{
			this.points = points;
			return;
		}
		g.setColor(colors[cp]);
		g.fillRect((int)x, (int)y, (int)w, (int)h);
		g.setColor(Color.BLACK);
		g.draw(new Rectangle(x, y, w, h));
		switch(rp)
		{
		case Left:
			rp = RectPos.Top;
			points.add(new Point(x, y + h));
			x += h;
			w -= h;
			break;
		case Top:
			rp = RectPos.Right;
			points.add(new Point(x, y));
			y += w;
			h -= w;
			break;
		case Right:
			rp = RectPos.Down;
			points.add(new Point(x + w, y));
			w -= h;
			break;
		case Down:
			rp = RectPos.Left;
			points.add(new Point(x + w, y + h));
			h -= w;
			break;
		}
		cp++;
		drawRectangles(g, points, x, y, w, h, rp);
	}
	
	private void drawTrueSpiral(Graphics2D g)
	{
		cp = 0;
		g.setColor(Color.BLACK);
		if (points.size() > 1)
		{
			Point start = points.get(0);
			Point origin = points.get(points.size()-1);
			float dx = start.getX() - origin.getX();
			float dy = start.getY() - origin.getY();
			double radius = Math.sqrt(dx * dx + dy * dy);
			
			double theta = Math.atan2(dy, dx);
			final int num_slices = 1000;
			double dtheta = Math.PI / 2 / num_slices;
			double factor = 1 - (1 / PHI) / num_slices * 0.78;
			List<Point> points = new ArrayList<Point>();
			
			while(radius > 0.1)
			{
				Point np = new Point((float)(origin.getX() + radius * Math.cos(theta)),
			            (float)(origin.getY() + radius * Math.sin(theta)));
				points.add(np);
				theta += dtheta;
				radius *= factor;
			}
			for(int i = 0; i < points.size()-1; i++)
			{
				g.drawLine((int)points.get(i).getX(), (int)points.get(i).getY(), 
						(int)points.get(i+1).getX(), (int)points.get(i+1).getY());
			}
		}
	}
	
	@Override
	public void run() 
	{
		while(running)
		{
			render();
		}
		stop();
	}
	
	public synchronized void start()
	{
		if(!running)
		{
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public synchronized void stop()
	{
		if(running)
		{
			running = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
