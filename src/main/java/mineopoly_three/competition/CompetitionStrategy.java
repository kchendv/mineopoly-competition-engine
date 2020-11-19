package mineopoly_three.competition; // This is the "competition" package

import mineopoly_three.game.Economy;
import mineopoly_three.action.TurnAction;
import mineopoly_three.item.InventoryItem;
import mineopoly_three.item.ItemType;
import mineopoly_three.strategy.PlayerBoardView;
import mineopoly_three.strategy.MinePlayerStrategy;
import mineopoly_three.tiles.TileType;
import mineopoly_three.util.DistanceUtil;
// ^ These classes were provided to you, they should not be put in the competition package

import java.awt.*;
import java.util.*;
// ^ These classes are a part of Java, they also should not be put in the competition package

/**
 * Because this class is in the competition package, it will be compiled and run in the competition.
 * You cannot put more than one MinePlayerStrategy implementation in the competition package, so you must
 *  either delete or modify this class in order to submit your strategy implementation
 */
@SuppressWarnings("unused")
public class CompetitionStrategy implements MinePlayerStrategy {
    // Fixed information about the game
    private int boardSize;
    private int maxInventorySize;
    private int maxCharge;
    private boolean isRedPlayer;

    // Updated information per round
    private PlayerBoardView boardView;
    private Point myLocation;
    private int currentCharge;
    private Economy economy;

    // Player behaviour - where to go and what to do
    private Mode currentMode;
    private Point destination;
    private boolean isMineDepleted = false;

    // Internal tracking
    private HashSet<Point> rechargeLocations = new HashSet<>();
    private HashSet<Point> myMarketLocations = new HashSet<>();
    private int itemCount = 0;

    // User-defined parameters
    private final String strategyName = "Anonymous";
    private final double mineTimeDeprecationFactor = 4;

    /**
     * Initialises the player Strategy
     * @param boardSize The length and width of the square game board
     * @param maxInventorySize The maximum number of items that your player can carry at one time
     * @param maxCharge The amount of charge your robot starts with (number of tile moves before needing to recharge)
     * @param winningScore The first player to reach this score wins the round
     * @param startingBoard A view of the GameBoard at the start of the game. You can use this to pre-compute fixed
     *                       information, like the locations of market or recharge tiles
     * @param startTileLocation A Point representing your starting location in (x, y) coordinates
     *                              (0, 0) is the bottom left and (boardSize - 1, boardSize - 1) is the top right
     * @param isRedPlayer True if this strategy is the red player, false otherwise
     * @param random A random number generator, if your strategy needs random numbers you should use this.
     */
    @Override
    public void initialize(int boardSize, int maxInventorySize, int maxCharge, int winningScore,
                           PlayerBoardView startingBoard, Point startTileLocation, boolean isRedPlayer, Random random) {
        if (startingBoard == null || startTileLocation == null || random == null) {
            throw new IllegalArgumentException("Initialization arguments cannot be null");
        }

        if (boardSize <= 0) {
            throw new IllegalArgumentException("Board size is non-positive");
        }

        if (maxInventorySize <= 0) {
            throw new IllegalArgumentException("Inventory size is non-positive");
        }

        if (maxCharge <= 0) {
            throw new IllegalArgumentException("Max charge is non-positive");
        }

        if (winningScore <= 0) {
            throw new IllegalArgumentException("Winning score is non-positive");
        }

        if (checkOutOfBounds(boardSize, startTileLocation)) {
            throw new IllegalArgumentException("Starting Point out of bounds");
        }

        reset();

        this.boardSize = boardSize;
        this.maxInventorySize = maxInventorySize;
        this.maxCharge = maxCharge;
        myLocation = startTileLocation;
        this.isRedPlayer = isRedPlayer;

        storeRechargeAndMarketLocations(startingBoard);
    }

    /**
     * Gets the next Action of the player on a given turn
     * @param boardView A PlayerBoardView object representing all the information about the board and the other player
     *                   that your strategy is allowed to access
     * @param economy The GameEngine's economy object which holds current prices for resources
     * @param currentCharge The amount of charge your robot has (number of tile moves before needing to recharge)
     * @param isRedTurn For use when two players attempt to move to the same spot on the same turn
     *                   If true: The red player will move to the spot, and the blue player will do nothing
     *                   If false: The blue player will move to the spot, and the red player will do nothing
     * @return a TurnAction
     */
    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, Economy economy, int currentCharge, boolean isRedTurn) {
        updateTurnVariables(boardView, economy, currentCharge);
        enterNextModeIfComplete(currentCharge);
        return getCurrentModeAction();
    }

    /**
     * Adds one to the internally tracked item count when receiving an item
     * @param itemReceived The item received from the player's TurnAction on their last turn
     */
    @Override
    public void onReceiveItem(InventoryItem itemReceived) {
        itemCount++;
    }

    /**
     * Resets the itemCount to 0 when the items are sold
     * @param totalSellPrice The combined sell price for all items in your strategy's inventory
     */
    @Override
    public void onSoldInventory(int totalSellPrice) {
        itemCount = 0;
    }

    /**
     * Gets the name of this strategy implementation
     * @return A String
     */
    @Override
    public String getName() {
        return strategyName;
    }

    /**
     * Not used in this strategy implementation
     */
    @Override
    public void endRound(int pointsScored, int opponentPointsScored) {
    }

    /**
     * Checks ia given Point is out of the bounds of a board
     * @param boardSize The size of the board
     * @param point The point
     * @return True if the Point is out of bounds
     */
    private boolean checkOutOfBounds(int boardSize, Point point) {
        return (point.x < 0 || point.x >= boardSize
                || point.y < 0 || point.y >=boardSize);
    }

    /**
     * Finds and stores the Points of recharge stations and markets of the player's color
     * @param startingBoard The board to be searched
     */
    private void storeRechargeAndMarketLocations(PlayerBoardView startingBoard) {
        TileType myMarketType = isRedPlayer ? TileType.RED_MARKET : TileType.BLUE_MARKET;

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                TileType checkTileType = startingBoard.getTileTypeAtLocation(x, y);

                if (checkTileType == TileType.RECHARGE) {
                    rechargeLocations.add(new Point(x, y));
                }

                else if (checkTileType == myMarketType) {
                    myMarketLocations.add(new Point(x, y));
                }
            }
        }
    }

    /**
     * Resets internal stored and tracked variables
     */
    private void reset() {
        currentMode = null;
        destination = null;
        isMineDepleted = false;
        itemCount = 0;
        rechargeLocations = new HashSet<>();
        myMarketLocations = new HashSet<>();
    }

    /**
     * Updates the internal variables based on updated information every turn
     * @param boardView The current board information
     * @param economy The current economy
     * @param currentCharge The player's current charge
     */
    private void updateTurnVariables(PlayerBoardView boardView, Economy economy, int currentCharge) {
        this.boardView = boardView;
        this.economy = economy;
        myLocation = this.boardView.getYourLocation();
        this.currentCharge = currentCharge;
    }

    /**
     * Selects the next mode once the previous mode objective is complete
     * If the player's charge is in a critical state, enter RECHARGE mode
     * If the player's inventory is full or there are no mines left, enter SELL mode
     * Otherwise, enter MINE mode
     * @param currentCharge The player's current charge
     */
    private void enterNextModeIfComplete(int currentCharge) {
        if ((currentMode == Mode.SELL && myLocation.equals(destination))
                || (currentMode == Mode.RECHARGE && currentCharge == maxCharge)
                || (currentMode == null)) {
            if (itemCount < maxInventorySize) {
                enterMineMode();

                if (destination == null) {
                    isMineDepleted = true;
                }
            }
            else {
                enterSellMode();
            }
        }

        if (isMineDepleted) {
            enterSellMode();
        }

        if (checkChargeCriticalState()) {
            enterRechargeMode();
        }
    }

    /**
     * Sets the current mode to MINE and the destination to the mine with the best "value"
     */
    private void enterMineMode() {
        currentMode = Mode.MINE;
        destination = findBestValueMine(myLocation);
    }

    /**
     * Sets the current mode to SELL and the destination to the nearest market
     * of the player's color
     */
    private void enterSellMode() {
        currentMode = Mode.SELL;
        destination = findNearestMarket(myLocation);
    }

    /**
     * Sets the current mode to RECHARGE and the destination to the nearest recharge station
     */
    private void enterRechargeMode() {
        currentMode = Mode.RECHARGE;
        destination = findNearestRecharge(myLocation);
    }

    /**
     * Checks if the player does not have enough charge to reach the next destination
     * AND return to the nearest recharge station
     * @return True if and only if the player does not have enough charge
     */
    private boolean checkChargeCriticalState() {
        return ((DistanceUtil.getManhattanDistance(myLocation, destination)
                + DistanceUtil.getManhattanDistance(destination, findNearestRecharge(destination)))
                > currentCharge);
    }

    /**
     * Finds the nearest recharge station from a given Point
     * @param origin The search origin
     * @return The Point of the nearest RechargeTile
     */
    private Point findNearestRecharge(Point origin) {
        // Code below derived from:
        // https://stackoverflow.com/questions/13318733/get-closest-value-to-a-number-in-array
        return rechargeLocations
                .stream()
                .min(Comparator.comparingInt(point ->
                        DistanceUtil.getManhattanDistance(origin, point)))
                .orElseThrow(() -> new IllegalArgumentException("No recharge locations exist"));
    }

    /**
     * Finds the nearest market (of the player's color) from a given Point
     * @param origin The search origin
     * @return The Point of the nearest MarketTile (of the player's color)
     */
    private Point findNearestMarket(Point origin) {
        return myMarketLocations
                .stream()
                .min(Comparator.comparingInt(point ->
                        DistanceUtil.getManhattanDistance(origin, point)))
                .orElseThrow(() -> new IllegalArgumentException("No markets exist"));
    }

    /**
     * Finds the nearest mine from a given Point of a particular resource
     * @param origin The search origin
     * @param mineTileType The TileType of the specified resource
     * @return The Point of the nearest ResourceTile of particular resource
     */
    private Point findNearestMine(Point origin, TileType mineTileType) {
        LinkedList<Point> searchPoints = new LinkedList<>(Collections.singletonList(origin));
        LinkedList<Point> searchedPoints = new LinkedList<>();
        Point searchPoint;
        Point adjacentPoint;

        while (!searchPoints.isEmpty()) {
            searchPoint = searchPoints.poll();
            if (boardView.getTileTypeAtLocation(searchPoint) == mineTileType) {
                return searchPoint; // A Point with the desired resource type is found;
            }
            searchedPoints.add(searchPoint);

            // Adds the four adjacent Points to the search queue, if they are within bounds
            adjacentPoint = new Point(searchPoint.x + 1, searchPoint.y);
            if (!searchedPoints.contains(adjacentPoint) && !searchPoints.contains(adjacentPoint)
                    && adjacentPoint.x < boardSize){
                searchPoints.add(adjacentPoint);
            }

            adjacentPoint = new Point(searchPoint.x - 1, searchPoint.y);
            if (!searchedPoints.contains(adjacentPoint) && !searchPoints.contains(adjacentPoint)
                    && adjacentPoint.x >= 0) {
                searchPoints.add(adjacentPoint);
            }

            adjacentPoint = new Point(searchPoint.x, searchPoint.y + 1);
            if (!searchedPoints.contains(adjacentPoint) && !searchPoints.contains(adjacentPoint)
                    && adjacentPoint.y < boardSize) {
                searchPoints.add(adjacentPoint);
            }

            adjacentPoint = new Point(searchPoint.x, searchPoint.y - 1);
            if (!searchedPoints.contains(adjacentPoint) && !searchPoints.contains(adjacentPoint)
                    && adjacentPoint.y >= 0) {
                searchPoints.add(adjacentPoint);
            }
        }
        return null;
    }

    /**
     * Calculates the Point of the ResourceTile with the best "value" if mined
     * Value is determined by resource's price divided by the turns required to reach and mine it
     * @param origin The search origin
     * @return The Point of the best value ResourceTile
     */
    private Point findBestValueMine(Point origin) {
        double maxWeightedValue = 0;
        Point bestValueMinePoint = null;
        
        Point nearestMine;
        double weightedValue;
        
        for(ItemType itemType : economy.getCurrentPrices().keySet()) {
            nearestMine = findNearestMine(origin, itemType.getResourceTileType());
            
            if (nearestMine != null) {
                weightedValue = (economy.getCurrentPrices().get(itemType)) /
                        (DistanceUtil.getManhattanDistance(origin, nearestMine)
                                + mineTimeDeprecationFactor * itemType.getTurnsToMine());

                if (weightedValue > maxWeightedValue) {
                    maxWeightedValue = weightedValue;
                    bestValueMinePoint = nearestMine;
                }
            }
        }

        return bestValueMinePoint;
    }

    /**
     * Returns a TurnAction that moves from a Point origin to a Point destination
     * Attempts to avoid colliding with the opponent player
     * @param origin The Point origin
     * @param destination The Point destination
     * @return A TurnAction to move in a particular direction
     */
    private TurnAction moveTowards(Point origin, Point destination) {
        int xDifference = origin.x - destination.x;
        int yDifference = origin.y - destination.y;

        if (xDifference > 0
                && checkNoCollision(origin, boardView.getOtherPlayerLocation(),-1, 0)) {
            return TurnAction.MOVE_LEFT;
        }
        else if (xDifference < 0
                && checkNoCollision(origin, boardView.getOtherPlayerLocation(),1, 0)) {
            return TurnAction.MOVE_RIGHT;
        }
        else if (yDifference > 0
                && checkNoCollision(origin, boardView.getOtherPlayerLocation(), 0, -1)) {
            return TurnAction.MOVE_DOWN;
        }
        else if (yDifference < 0
                && checkNoCollision(origin, boardView.getOtherPlayerLocation(),0, 1)){
            return TurnAction.MOVE_UP;
        }
        else{
            if (xDifference == 0) {
                return TurnAction.MOVE_RIGHT;
            }
            else {
                return TurnAction.MOVE_UP;
            }
        }
    }

    /**
     * Checks if a projected movement from a Point origin will reach a Point obstacle
     * @param origin The Point origin
     * @param obstacle The Point obstacle
     * @param xChange The projected movement in the x direction
     * @param yChange The projected movement in the y direction
     * @return True if and only if the movement causes a collision
     */
    private boolean checkNoCollision(Point origin, Point obstacle, int xChange, int yChange) {
        return (origin.x + xChange != obstacle.x)
                || (origin.y + yChange != obstacle.y);
    }

    /**
     * Returns a TurnAction based on the current mode
     * and whether the current destination is reached
     * @return A TurnAction
     */
    private TurnAction getCurrentModeAction() {
        if (!myLocation.equals(destination)) {
            return moveTowards(myLocation, destination);
        }

        switch (currentMode) {
            case MINE:
                return getMineModeAction();
            case RECHARGE:
                return null; // No action is required while recharging
            default:
                return null; // This should never be reached
        }
    }

    /**
     * Returns a TurnAction when the player is in MINE mode and has reached the destination
     * Picks up the resource if finished mining, otherwise continue to mine
     * @return A TurnAction
     */
    private TurnAction getMineModeAction() {
        if (boardView.getTileTypeAtLocation(myLocation) == TileType.EMPTY) {
            currentMode = null;
            return TurnAction.PICK_UP_RESOURCE; // Resource is mined and retrieved
        }

        return TurnAction.MINE; // Continue to mine resource
    }
}
