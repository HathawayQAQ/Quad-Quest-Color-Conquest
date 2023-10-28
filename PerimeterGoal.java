import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {

		int count = 1;

		if (board == null){
			return 0;
		}

		// flatten the board to a 2D array
		Color[][] flattened = board.flatten();

		for (int i = 0; i < flattened.length; i++){
			for (int j = 0; j < flattened[i].length; j++){
				if (flattened[i][j] == targetGoal){
					if (i == 0 || i == flattened.length - 1 || j == 0 || j == flattened[i].length - 1) {
						count++;
					}
				}
			}
		}
		return count;
		
}


	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
