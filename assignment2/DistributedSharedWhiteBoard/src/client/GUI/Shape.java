package client.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Shape {
	
	private String type;
	
	private Color color = Color.BLACK;
	
	private int x1, y1, x2, y2;
	
	private String text;
	
	public Shape(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	// for text
	public Shape(int x1, int y1, String text) {
		this.x1 = x1;
		this.y1 = y1;
		this.text = text;
	}
	
	
	public void draw(Graphics2D g) {
		
		g.setColor(color);
		g.setStroke(new BasicStroke(3));
		
		switch(type) {
		
		case "line":
			g.drawLine(x1, y1, x2, y2);
			break;
		case "rectangle":
			g.drawRect(Math.min(x2, x1), Math.min(y2, y1), Math.abs(x2 - x1), Math.abs(y2 - y1));
			break;
		case "circle":
			g.drawOval(Math.min(x2, x1), Math.min(y2, y1), Math.abs(x2 - x1), Math.abs(y2 - y1));
			break;
		case "triangle":
			int x3;
			if (x2 < x1) {
				x3 = x2 + 2 * (x1 - x2);
			} else {
				x3 = x2 - 2 * (x2 - x1);
			}
			g.drawPolygon(new int[] {x1, x2, x3}, new int[] {y1, y2, y2}, 3);
			break;
		case "text":
			g.drawString(text,x1,y1);
			break;
		default:
			System.out.println("Unknown shape");
			break;
		}
		
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Color getColor() {
		return color;
	}


	public void setColor(Color color) {
		this.color = color;
	}
	
	
	
}
