# Woods

This is a basic Collision simulator. It will start with various players and the players will randomly move in various directions, until one player hits another.

Main source code is located at: Woods/core/src/game/

You can also view the game on the web: https://redwoodhugger.itch.io/wood-simulator

I used GWT to turn the java code to HTML5 compatible code. And I used the libGDX java framework to create the graphics.

'woods.java' is the main controlling class. The other two main sub classes are 'Boardscreen.java' and 'MainMenu.java'.

'BoardScreen.java' is main screen where the simulation happens. It uses the BoardController class to control the state of the 'players' and to look for collisions and to update player movement.

'BoardController.java' uses a double array structure, from a class called 'Pieces.java'. 'Pieces' uses a double array structure that implements a abstract class called 'blocks.java' and 'Pieces' itself is an ArrayList that can allow for multiple 'blocks' in a single tiled rectangle.

Basically the main data structure for the simulation is a double array using 'Blocks.java'. The double array allows to calculate X and Y values to draw to the screen. Where each X and Y value is calculated according to the width and height of each block.

Now the height/width of each rectangle block is determined by the amount of rows/columns. The more rows/columns, the smaller the height and width will be. Which allows the simulation to scale appropriately depending on the input the user enters.

![Board drawio](https://user-images.githubusercontent.com/37820285/134810709-088773ad-7679-402b-89ea-f42ff017a24d.png)
