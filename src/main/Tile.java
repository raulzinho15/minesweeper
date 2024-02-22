package main;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Handles tiles in the Minesweeper game.
 * @author Raul Hernandez
 */
public class Tile extends Group {
	
	/** The width/height of a tile. */
	public static final int SIZE = 50;
	
	/** This tile's x-coordinate. */
	private final int x;
	
	/** This tile's y-coordinate. */
	private final int y;
	
	/** The number of bombs surrounding this tile. */
	private int number = -1;
	
	/** Whether this tile has been revealed. */
	private boolean isRevealed;
	
	/** Whether this tile has a bomb. */
	private boolean isBomb;
	
	/** Whether the tile is indicated as a bomb. */
	private boolean isIndicated;
	
	/** The tile's covering. */
	private Group covering = new Group();
	
	/** The tile's number indicator. */
	final Label numberIndicator = new Label();
	
	/**
	 * Constructs a Tile.
	 * @param x The x-coordinate of this tile.
	 * @param y The y-coordinate of this tile.
	 */
	public Tile(Mine mine, int x, int y) {
		super();
		
		//Sets up the coordinates
		this.x = x;
		this.y = y;
		
		//The tile's background
		final Rectangle background = new Rectangle(SIZE, SIZE, Color.web("#d0d0d0"));
		getChildren().add(background);
		
		//The tile's number indicator
		numberIndicator.setFont(new Font("Arial Bold", SIZE*4/5/12*12));
		numberIndicator.setAlignment(Pos.CENTER);
		numberIndicator.setPrefSize(SIZE, SIZE);
		getChildren().add(numberIndicator);
		
		//The border of the tile's covering
		final Rectangle coveringBorder = new Rectangle(SIZE, SIZE, Color.web("#808080"));
		covering.getChildren().add(coveringBorder);
		
		//The border of the tile's covering
		final Rectangle coveringCenter = new Rectangle(SIZE*4/5, SIZE*4/5, Color.web("#a0a0a0"));
		coveringCenter.setLayoutX(SIZE/10);
		coveringCenter.setLayoutY(SIZE/10);
		covering.getChildren().add(coveringCenter);
		
		//The tile's bomb indicator
		final Rectangle bombIndicator = new Rectangle(SIZE/5, SIZE/5, Color.RED);
		bombIndicator.setVisible(false);
		bombIndicator.setLayoutX(SIZE*2/5);
		bombIndicator.setLayoutY(SIZE*2/5);
		covering.getChildren().add(bombIndicator);
		
		//The hover indicator of the tile's covering
		final Rectangle hoverOverlay = new Rectangle(SIZE, SIZE, Color.WHITE);
		hoverOverlay.setOpacity(0.2);
		hoverOverlay.setVisible(false);
		covering.getChildren().add(hoverOverlay);
		
		//Sets up the covering Group
		covering.setOnMouseEntered(m -> {
			covering.setCursor(Cursor.HAND);
			hoverOverlay.setVisible(true);
		});
		covering.setOnMouseExited(m -> {
			covering.setCursor(Cursor.DEFAULT);
			hoverOverlay.setVisible(false);
		});
		covering.setOnMouseClicked(m -> {
			switch (m.getButton()) {
			case PRIMARY:
				//Resets the mine if a bomb is clicked
				if (isBomb) {
					MinesweeperMain.resetMine();
					return;
				}
				
				//Checks if first click
				if (number == -1)
					mine.registerFirstClick(x, y);
				
				//Reveals the tile
				mine.revealTile(x, y);
				break;
			case SECONDARY:
				//Updates bomb indication
				bombIndicator.setVisible(!bombIndicator.isVisible());
				isIndicated = !isIndicated;
				
				//Updates bombs left if this tile is a bomb
				if (isBomb)
					mine.updateIndicatedBombs(isIndicated);
				break;
			default:
				break;
			}
		});
		getChildren().add(covering);
	}
	
	/**
	 * Checks whether this tile is in the specified region
	 * @param radius The radius around the tile.
	 * @param x The target tile's x-coordinate.
	 * @param y The target tile's y-coordinate.
	 * @return Whether this tile is in the specified region.
	 */
	public boolean isWithin(int radius, int x, int y) {
		return Math.abs(this.x-x) <= radius && Math.abs(this.y-y) <= radius;
	}
	
	/**
	 * Reveals this tile.
	 */
	public void reveal() {
		isRevealed = true;
		covering.setVisible(false);
	}
	
	/**
	 * @return Whether this tile has been revealed.
	 */
	public boolean isRevealed() {
		return isRevealed;
	}
	
	/**
	 * @return Whether this tile has no surrounding bombs.
	 */
	public boolean noSurrounding() {
		return number == 0;
	}
	
	/**
	 * Sets how many bombs are surrounding this tile.
	 * @param number The number of bombs surrounding this tile.
	 * @param isBomb Whether this tile is a bomb.
	 */
	public void finalSetup(int number, boolean isBomb) {
		this.number = number;
		if (number != 0)
			numberIndicator.setText("" + number);
		this.isBomb = isBomb;
	}
}
