import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth; 
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random(); 
 
 
 /*
  * These two constructors are here for testing purposes. 
  */
 public Block() {}
 
 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;

 }
 
 /*
  * Creates a random block given its level and a max depth. 
  * 
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) {

    this.level = lvl;
    this.maxDepth = maxDepth;

    // Set the children of the block
    // not at the max
    if (lvl < maxDepth && gen.nextDouble() < Math.exp(-0.25 * lvl)){
        // create 4 children blocks and assign them
        this.color = null;
        this.children = new Block[4];
        
        for (int i = 0; i < 4; i++){
            // children[i].setSize(subSize);
            children[i] = new Block(lvl + 1, maxDepth);
            // children[i].updateSizeAndPosition(subSize, subSize, i);
        } 
        
    } else {
        this.color = GameColors.BLOCK_COLORS[gen.nextInt(GameColors.BLOCK_COLORS.length)];
        this.children = new Block[0];
    }

 }
 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the 
  * blocks. 
  * 
  *  The size is the height and width of the block. (xCoord, yCoord) are the 
  *  coordinates of the top left corner of the block. 
  */

 public void updateSizeAndPosition (int size, int xCoord, int yCoord) throws IllegalArgumentException{


    // if it is negative or it cannot be evenly divided into 2 integers until the max depth is reached
    this.size = size;
    this.xCoord = xCoord;
    this.yCoord = yCoord;

    if (size <= 0 || (size % 2 != 0 && level < maxDepth)) {
        throw new IllegalArgumentException("Invalid size");
    }

    // Recalculate position and size for sub-blocks

    // this.color = null;
    int halfSize = size / 2;
    if (children == null || children.length == 0) {
        return;
    }

    // top right
    this.children[0].updateSizeAndPosition(halfSize, xCoord + halfSize, yCoord);
    // top left
    this.children[1].updateSizeAndPosition(halfSize, xCoord, yCoord);
    // bottom left
    this.children[2].updateSizeAndPosition(halfSize, xCoord, yCoord + halfSize);
    // bottom right
    this.children[3].updateSizeAndPosition(halfSize, xCoord + halfSize, yCoord + halfSize);
}


  

 
 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  * 
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  * 
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *  
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {

    // My codes start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    ArrayList<BlockToDraw> blocksToDraw = new ArrayList<BlockToDraw>();

    if (this.children.length == 0){

        //add block in its color
        blocksToDraw.add(new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0));

        // add block frame
        blocksToDraw.add(new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3));
    
    } else {
        for (int i = 0; i < 4; i++){
            
            ArrayList<BlockToDraw> subBlocksToDraw = new ArrayList<BlockToDraw>();
            
            // add its color & frame
            this.children[i].updateSizeAndPosition(this.children[i].size, this.children[i].xCoord, this.children[i].yCoord);
            subBlocksToDraw = this.children[i].getBlocksToDraw();

            for (int j = 0; j < subBlocksToDraw.size(); j++){
                blocksToDraw.add(subBlocksToDraw.get(j));
            }
        }
    }
    return blocksToDraw;
    // My codes end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

  // return null;
 }

 /*
  * This method is provided and you should NOT modify it. 
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }
 
 
 
 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than 
  * the lowest block at the specified location, then return the block 
  * at the location with the closest level value.
  * 
  * The location is specified by its (x, y) coordinates. The lvl indicates 
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will 
  * contain the location (x, y) too. This is why we need lvl to identify 
  * which Block should be returned. 

  * 
  * Input validation: 
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
  public Block getSelectedBlock(int x, int y, int level) {
    // My codes start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    //if the level provided is smaller than this Block’s level or larger than its maximum depth, throw error
    Block retVal = new Block();
    if (level < this.level || level > maxDepth) {
        throw new IllegalArgumentException("the level provided is not correct");
    }

    // If the position, (x, y), is not within this Block, then the method should return null.
    // check if the position is within the boundaries of this Block
    if (x < this.xCoord || x >= this.xCoord + this.size || y < this.yCoord || y >= this.yCoord + this.size) {
        return null;
    }

    // Check if the position is within one of the sub-blocks
    //int halfSize = size / 2;
    if (this.children.length != 0 && level > this.level){
        if (x < this.xCoord + this.size && y < this.yCoord + this.size / 2 && x > this.xCoord + this.size / 2) {
            retVal = this.children[0].getSelectedBlock(x, y, level);
        } else if (x < this.xCoord + this.size / 2 && y < this.yCoord + this.size / 2) {
            retVal = this.children[1].getSelectedBlock(x, y, level);
        } else if (x < this.xCoord + this.size / 2 && y < this.yCoord + this.size && y > this.yCoord + this.size / 2) {
            retVal = this.children[2].getSelectedBlock(x, y, level);
        } else if (x < this.xCoord + this.size && y < this.yCoord + this.size && y > this.yCoord + this.size / 2 && x > this.xCoord + this.size / 2) {
            retVal = this.children[3].getSelectedBlock(x, y, level);
        }
    }

    // if no sub-block contains the position, return this block
    if (this.level == level) {
        retVal = this;
    }

    if (this.level < level && this.children.length == 0) {
        retVal = this;
    }
    return retVal;
    // My codes end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

}


 
 

 /*
  * Swaps the child Blocks of this Block. 
  * If input is 1, swap vertically. If 0, swap horizontally. 
  * If this Block has no children, do nothing. The swap 
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  * 
  */
 public void reflect(int direction) {
  // My codes start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  
    if (direction != 0 && direction != 1) {
        throw new IllegalArgumentException("Invalid axis. Must be 0 or 1.");
    }

    // Base case: has no subBlocks
    if (this.children.length == 0){
        // the xCoord and yCoord do not change
        
    } else{

        // Reflect all subBlocks recursively
        for (Block subBlock : this.children){
            subBlock.reflect(direction);
        }

        // if it reflect at the x-axis
        if (direction == 0){

            // swap the first one (top right) and the last one (bottom right)
            Block temp_right = this.children[0];
            this.children[0] = this.children[3];
            this.children[3] = temp_right;

            // swap the second one (top left) and the third one (bottom left)
            Block temp_left = this.children[1];
            this.children[1] = this.children[2];
            this.children[2] = temp_left;
        
        // if it reflect at the x-axis
        } else{
            // swap the first one (top right) and the second one (top left)
            Block temp_top = this.children[0];
            this.children[0] = this.children[1];
            this.children[1] = temp_top;

            // swap the third one (bottom left) and the last one (bottom right)
            Block temp_bottom = this.children[2];
            this.children[2] = this.children[3];
            this.children[3] = temp_bottom;
        }

    }
}


// My codes end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 

 
 /*
  * Rotate this Block and all its descendants. 
  * If the input is 1, rotate clockwise. If 0, rotate 
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  // My codes start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    //takes an int as input representing whether this Block should be rotated 

    //counter-clockwise (if the input is 0) or clockwise (if the input is 1)
    if (direction != 0 && direction != 1) {
        throw new IllegalArgumentException("Invalid direction. Must be 0 or 1.");
    }

    // Base case: has no children
    if (this.children.length == 0) {
        //do nothing

    } else {
        // Reflect all subBlocks recursively
        for (Block subBlock : this.children){
            subBlock.rotate(direction);
        }

        // if it rotates clockwise
        if (direction == 1){

            // 0 -> 1, 1 -> 2, 2 -> 3, 3 -> 0
            Block temp0 = this.children[0];
            this.children[0] = this.children[1];
            this.children[1] = this.children[2];
            this.children[2] = this.children[3];
            this.children[3] = temp0;
        
        // if it rotates counter-clockwise
        } else{
            // 0 -> 3, 1 -> 0, 2 -> 1, 3 -> 2
            Block temp0 = this.children[0];
            this.children[0] = this.children[3];
            this.children[3] = this.children[2];
            this.children[2] = this.children[1];
            this.children[1] = temp0;
        }
    }

    
}
// My codes end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 

 


 /*
  * Smash this Block.
  * 
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.  
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  * 
  * A Block can be smashed iff it is not the top-level Block 
  * and it is not already at the level of the maximum depth.
  * 
  * Return True if this Block was smashed and False otherwise.
  * 
  */
 public boolean smash() {
    // My codes start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    if ((this.level >= this.maxDepth) || (this.level == 0)){
        return false;
    } 

    this.children = new Block[4];

    for (int i = 0; i < 4; i++){
        int tempBlock = this.level + 1;
        this.children[i] = new Block(tempBlock, this.maxDepth);
    }
    return true;
    // My codes end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
    //return false;
 }

/**
     * Return a two-dimensional array representing this Block as rows and columns of unit cells.
     *
     * Return and array arr where, arr[i] represents the unit cells in row i,
     * arr[i][j] is the color of unit cell in row i and column j.
     *
     * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
     */
    public Color[][] flatten() {

        int size = (int) Math.pow(2, this.maxDepth - this.level);

        Color[][] arr = new Color[size][size];
        // return null if the block is empty
        if (this.size == 0) {
            // size = 0;
            return new Color[0][0];
        }   

        if (this.children == null || this.children.length == 0) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    arr[i][j] = this.color;
                }
            }

        } else {

            Color[][] topRight = this.children[0].flatten();
            for (int i = 0; i < size / 2; i++) {
                for (int j = 0; j < size / 2; j++) {
                    int xIndex = i;
                    int yIndex = j + size / 2;
                    arr[xIndex][yIndex] = topRight[i][j];
                }
            }

            Color[][] topLeft = this.children[1].flatten();
            for (int i = 0; i < size / 2; i++) {
                for (int j = 0; j < size / 2; j++) {
                    int xIndex = i;
                    int yIndex = j;
                    arr[xIndex][yIndex] = topLeft[i][j];
                }
            }

            Color[][] bottomLeft = this.children[2].flatten();
            for (int i = 0; i < size / 2; i++) {
                for (int j = 0; j < size / 2; j++) {
                    int xIndex = i + size / 2;
                    int yIndex = j;
                    arr[xIndex][yIndex] = bottomLeft[i][j];
                }
            }

            Color[][] bottomRight = this.children[3].flatten();
            for (int i = 0; i < size / 2; i++) {
                for (int j = 0; j < size / 2; j++) {
                    int xIndex = i + size / 2;
                    int yIndex = j + size / 2;
                    arr[xIndex][yIndex] = bottomRight[i][j];
                }
            }
        }
        return arr;
    }
 
 
 // These two get methods have been provided. Do NOT modify them. 
 public int getMaxDepth() {
  return this.maxDepth;
 }
 
 public int getLevel() {
  return this.level;
 }

 // My private getter!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 public void setSize(int size) {
    this.size = size;
}

public void setXCoord(int xCoord) {
    this.xCoord = xCoord;
}

public void setYCoord(int yCoord) {
    this.yCoord = yCoord;
}

 /*
  * The next 5 methods are needed to get a text representation of a block. 
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);   
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }
 
 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }
 
}
