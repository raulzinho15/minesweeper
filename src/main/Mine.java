package main;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Mine {
	
	/** The number of bombs to include in the map. */
	private final int bombAmount;
	
	/** The radius around the first click to not include bombs. */
	private final int noBombRadius;
	
	/** The number of bombs left to indicate. */
	private int bombsLeft;
	
	/** The mine scene's pane. */
	private final AnchorPane pane = new AnchorPane();
	
	/** The map of tiles. */
	private final Tile[][] tileMap;
	
	/** The map of bombs. */
	private final boolean[][] bombMap;

	/**
	 * Constructs a mine of the given dimensions.
	 * @param width The width of the mine.
	 * @param height The height of the mine.
	 * @param bombAmount The number of bombs to include in the mine.
	 * @param noBombRadius The radius around the first click to not include any bombs.
	 */
	public Mine(int width, int height, int bombAmount, int noBombRadius) {
		
		//Initializes the map arrays
		tileMap = new Tile[height][width];
		bombMap = new boolean[height][width];
		
		//Initializes the map parameters
		this.bombAmount = bombAmount;
		bombsLeft = bombAmount;
		this.noBombRadius = noBombRadius;
		
		//Sets up the initial tiles on their coordinates
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				
				//Sets up the tile
				final Tile tile = new Tile(this, x, y);
				tile.setLayoutX(x*Tile.SIZE);
				tile.setLayoutY(y*Tile.SIZE);
				
				//Adds the tile to its collections
				pane.getChildren().add(tile);
				tileMap[y][x] = tile;
		}
	}
	
	/**
	 * Registers the first click.
	 * @param firstX The x-coordinate of the first click.
	 * @param firstY The y-coordinate of the first click.
	 */
	public void registerFirstClick(int firstX, int firstY) {
		generateBombs(firstX, firstY);
		setupTiles();
	}
	
	/**
	 * Generates the map of bombs.
	 * @param firstX The x-coordinate of the first click.
	 * @param firstY The y-coordinate of the first click.
	 */
	private void generateBombs(int firstX, int firstY) {
		//Generates the map of bombs
		for (int i = 0; i < bombAmount; i++) {
			//Parameters for checking
			int randX, randY;
			boolean isBomb;
			Tile tile;
			
			//Chooses a random non-bomb tile away from click
			do {
				//Generates random coordinates
				randX = (int)(tileMap[0].length*Math.random());
				randY = (int)(tileMap.length*Math.random());
				
				//Stores data from random coordinates
				tile = tileMap[randY][randX];
				isBomb = bombMap[randY][randX];
			} while (tile.isWithin(noBombRadius, firstX, firstY) || isBomb);
			
			//Sets this random tile to be a bomb
			bombMap[randY][randX] = true;
		}
	}
	
	/**
	 * Finalizes the setup of the tiles.
	 */
	private void setupTiles() {
		//Runs through each coordinate
		for (int x = 0; x < tileMap[0].length; x++)
			for (int y = 0; y < tileMap.length; y++)
				tileMap[y][x].finalSetup(surroundingBombs(x, y), bombMap[y][x]);
	}
	
	/**
	 * Checks how many bombs surround the tile at these coordinates.
	 * @param x The x-coordinate of the tile to check.
	 * @param y The y-coordinate of the tile to check.
	 * @return How many bombs surround the target tile.
	 */
	private int surroundingBombs(int x, int y) {
		int bombCount = 0;
		
		//Runs through each surrounding tile
		for (int dx = -1; dx <= 1; dx++)
			for (int dy = -1; dy <= 1; dy++) {
				
				//Skips the center tile
				if (dx == 0 && dy == 0)
					continue;
				
				//Checks if current tile is a bomb
				try {
					if (bombMap[y+dy][x+dx])
						bombCount++;
				} catch (ArrayIndexOutOfBoundsException e) {}
		}
		return bombCount;
	}
	
	/**
	 * Reveals the specified tile.
	 * @param x The x-coodinate of the tile.
	 * @param y The y-coodinate of the tile.
	 */
	public void revealTile(int x, int y) {
		//Reveals this tile
		final Tile tile = tileMap[y][x];
		tile.reveal();
		
		//Reveals surrounding tiles if this tile has no bomb surroundings
		if (tile.noSurrounding())
			
			//Runs through each surrounding tile
			for (int dx = -1; dx <= 1; dx++)
				for (int dy = -1; dy <= 1; dy++) try {
					
					//Reveals tile if not already revealed
					if (!tileMap[y+dy][x+dx].isRevealed())
						revealTile(x+dx, y+dy);
					
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	
	/**
	 * Updates the number of bombs left to indicate.
	 * @param indicated Whether the bomb was indicated or unindicated.
	 */
	public void updateIndicatedBombs(boolean indicated) {
		bombsLeft += indicated ? -1 : 1;
		if (bombsLeft == 0)
			MinesweeperMain.resetMine();
	}
	
	/**
	 * @return The Scene containing this mine.
	 */
	public Scene getScene() {
		return new Scene(pane);
	}
}
