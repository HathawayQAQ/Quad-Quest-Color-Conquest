import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		// My codes start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		if (board == null) {
			return 0;
		}

		int blobSize = 0;

		Color[][] flattened = board.flatten();

		if (flattened == null || flattened.length < 1 || flattened[0].length < 1) {
			return 0;
		}

        boolean[][] visited = new boolean[flattened.length][flattened[0].length];

		for (int i = 0; i < flattened.length; i++){
			for (int j = 0; j < flattened[i].length; j++){
				if (!visited[i][j] && flattened[i][j] == targetGoal){
					int size = undiscoveredBlobSize(i, j, flattened, visited);
					System.out.print("size" + size);
					if (blobSize < size){
						blobSize = size;
					}
				}
			}
		}
		return blobSize;
		// My codes end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//return 0;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		// My codes start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		// if it is already visited (avoid counting twice)
		if (visited[i][j]) {
			return 0;
		}

		visited[i][j] = true;
		// Check if the unitCells array is null
		if (unitCells == null) {
			return 0;
		}

		// if it is not the part of the blob (not the target color)
		if (!(unitCells[i][j].equals(targetGoal))) {
			return 0;
		}

		// if it is out of bounds
		if (i < 0 || i >= unitCells.length || j < 0 || j >= unitCells[i].length) {
		// if (i < 0 || i >= GameColors.BLOCK_COLORS.length || j < 0 || j >= GameColors.BLOCK_COLORS[i].length) {
			return 0;
		}

		//initialize the variables
		
		int size = 1;

		// check the top
		if (i > 0) {
			size += undiscoveredBlobSize(i - 1, j, unitCells, visited);
		}

		// check the bottom
		if (i < unitCells.length - 1) {
			size += undiscoveredBlobSize(i + 1, j, unitCells, visited);
		}

		// check the left
		if (j > 0) {
			size += undiscoveredBlobSize(i, j - 1, unitCells, visited);
		}

		// check the right
		if (j < unitCells[i].length - 1) {
			size += undiscoveredBlobSize(i, j + 1, unitCells, visited);
		}
		return size;

		// My codes end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//return 0;

	}

}
