import mineopoly_three.action.TurnAction;
import mineopoly_three.competition.CompetitionStrategy;
import mineopoly_three.game.Economy;
import mineopoly_three.game.WorldGenerator;
import mineopoly_three.item.InventoryItem;
import mineopoly_three.item.ItemType;
import mineopoly_three.strategy.PlayerBoardView;
import mineopoly_three.strategy.RandomStrategy;
import mineopoly_three.tiles.EmptyTile;
import mineopoly_three.tiles.TileType;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;

public class CompetitionStrategyTest {
    private final String strategyName = "Anonymous";
    private int boardLength;
    private int inventorySize;
    private int maxCharge;
    private int winningScore;
    private boolean isRedPlayer;
    private Random random;
    private Point thisPlayerLocation;
    private Point otherPlayerLocation;
    private int otherPlayerScore;

    private Map<Point, List<InventoryItem>> itemsOnGround;
    private TileType[][] tileView;
    private Economy economy;

    private CompetitionStrategy competitionStrategy;

    @Before
    public void setUp() {
        boardLength = 10;
        inventorySize = 5;
        maxCharge = 80;
        winningScore = 10000;
        isRedPlayer = true;
        random = new Random(0);

        tileView = new TileType[boardLength][boardLength];
        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++) {
                setTileAtPoint(new Point(i, j), TileType.EMPTY);
            }
        }
        setTileAtPoint(new Point(2, 2), TileType.RED_MARKET);
        setTileAtPoint(new Point(7, 7), TileType.RED_MARKET);
        setTileAtPoint(new Point(2, 7), TileType.BLUE_MARKET);
        setTileAtPoint(new Point(7, 2), TileType.BLUE_MARKET);
        setTileAtPoint(new Point(4, 4), TileType.RECHARGE);
        setTileAtPoint(new Point(4, 5), TileType.RECHARGE);
        setTileAtPoint(new Point(5, 4), TileType.RECHARGE);
        setTileAtPoint(new Point(5, 5), TileType.RECHARGE);
        itemsOnGround = new HashMap<>();
        thisPlayerLocation = new Point(2,7);
        otherPlayerLocation = new Point (7,7);
        otherPlayerScore = 0;

        ItemType[] sellableResourceTypes = {ItemType.DIAMOND, ItemType.EMERALD, ItemType.RUBY};
        economy = new Economy(sellableResourceTypes);

        competitionStrategy = new CompetitionStrategy();
    }

    @Test
    public void expectedInitializeRedPlayer() {
        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test
    public void expectedInitializeBluePlayer() {
        isRedPlayer = false;
        thisPlayerLocation = new Point (7,7);
        otherPlayerLocation = new Point(2,7);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeNullStartingBoard() {
        PlayerBoardView playerBoardView = null;

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeNullStartTileLocation() {
        thisPlayerLocation = null;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeNullRandom() {
        random = null;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeBoardSizeIsNegative() {
        boardLength = -10;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeBoardSizeIsZero() {
        boardLength = 0;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeInventorySizeIsNegative() {
        inventorySize = -5;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeInventorySizeIsZero() {
        inventorySize = 0;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeMaxChargeIsNegative() {
        maxCharge = -100;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeMaxChargeIsZero() {
        maxCharge = 0;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeWinningScoreIsNegative() {
        winningScore = -10040;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeWinningScoreIsZero() {
        winningScore = 0;

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeStartTileLocationXOutOfUpperBounds() {
        thisPlayerLocation = new Point(10, 7);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeStartTileLocationXOutOfLowerBounds() {
        thisPlayerLocation = new Point(-1, 7);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeStartTileLocationYOutOfUpperBounds() {
        thisPlayerLocation = new Point(2, 10);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidInitializeStartTileLocationYOutOfLowerBounds() {
        thisPlayerLocation = new Point(2, -1);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test
    public void invalidInitializeStartTileLocationXOnUpperBounds() {
        thisPlayerLocation = new Point(9, 7);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test
    public void invalidInitializeStartTileLocationXOnLowerBounds() {
        thisPlayerLocation = new Point(0, 7);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test
    public void invalidInitializeStartTileLocationYOnUpperBounds() {
        thisPlayerLocation = new Point(8, 9);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test
    public void invalidInitializeStartTileLocationYOnLowerBounds() {
        thisPlayerLocation = new Point(3, 0);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);
    }

    @Test
    public void goTowardsMineLeft() {
        thisPlayerLocation = new Point(2, 2);
        Point testMinePoint = new Point(1, 2);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_LEFT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineRight() {
        thisPlayerLocation = new Point(0, 0);
        Point testMinePoint = new Point(1, 0);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_RIGHT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineUp() {
        thisPlayerLocation = new Point(5, 6);
        Point testMinePoint = new Point(5, 9);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineDown() {
        thisPlayerLocation = new Point(9, 9);
        Point testMinePoint = new Point(9, 2);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_DOWN,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMinePreferLeftToUp() {
        thisPlayerLocation = new Point(2, 3);
        Point testMinePoint = new Point(1, 5);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_LEFT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMinePreferLeftToDown() {
        thisPlayerLocation = new Point(2, 1);
        Point testMinePoint = new Point(1, 0);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_LEFT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMinePreferRightToUp() {
        thisPlayerLocation = new Point(3, 6);
        Point testMinePoint = new Point(5, 9);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_RIGHT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMinePreferRightToDown() {
        thisPlayerLocation = new Point(1, 9);
        Point testMinePoint = new Point(9, 2);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_RIGHT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineLeftBlockedGoUp() {
        thisPlayerLocation = new Point(2, 3);
        otherPlayerLocation = new Point(1, 3);
        Point testMinePoint = new Point(1, 6);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineLeftBlockedGoDown() {
        thisPlayerLocation = new Point(2, 1);
        otherPlayerLocation = new Point(1, 1);
        Point testMinePoint = new Point(0, 0);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_DOWN,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineRightBlockedGoUp() {
        thisPlayerLocation = new Point(3, 6);
        otherPlayerLocation = new Point(4, 6);
        Point testMinePoint = new Point(7, 9);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineRightBlockedGoDown() {
        thisPlayerLocation = new Point(1, 9);
        otherPlayerLocation = new Point(2, 9);
        Point testMinePoint = new Point(9, 2);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_DOWN,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineLeftBlockedDirectlyLeft() {
        thisPlayerLocation = new Point(2, 3);
        otherPlayerLocation = new Point(1, 3);
        Point testMinePoint = new Point(0, 3);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineLeftBlockedDirectlyRight() {
        thisPlayerLocation = new Point(2, 1);
        otherPlayerLocation = new Point(3, 1);
        Point testMinePoint = new Point(6, 1);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineRightBlockedDirectlyUp() {
        thisPlayerLocation = new Point(3, 6);
        otherPlayerLocation = new Point(3, 7);
        Point testMinePoint = new Point(3, 9);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_RIGHT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineSameDistancePreferHighPriceLeft() {
        thisPlayerLocation = new Point(3, 7);
        Point testDiamondMinePoint = new Point(1, 7);
        setTileAtPoint(testDiamondMinePoint, TileType.RESOURCE_DIAMOND);
        Point testEmeraldMinePoint = new Point(5, 7);
        setTileAtPoint(testEmeraldMinePoint, TileType.RESOURCE_EMERALD);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_LEFT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineSameDistancePreferHighPriceRight() {
        thisPlayerLocation = new Point(3, 7);
        Point testDiamondMinePoint = new Point(5, 7);
        setTileAtPoint(testDiamondMinePoint, TileType.RESOURCE_DIAMOND);
        Point testEmeraldMinePoint = new Point(1, 7);
        setTileAtPoint(testEmeraldMinePoint, TileType.RESOURCE_EMERALD);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_RIGHT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineSameDistancePreferHighPriceUp() {
        thisPlayerLocation = new Point(1, 7);
        Point testDiamondMinePoint = new Point(1, 8);
        setTileAtPoint(testDiamondMinePoint, TileType.RESOURCE_DIAMOND);
        Point testEmeraldMinePoint = new Point(1, 6);
        setTileAtPoint(testEmeraldMinePoint, TileType.RESOURCE_EMERALD);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineSameDistancePreferHighPriceDown() {
        thisPlayerLocation = new Point(3, 7);
        Point testDiamondMinePoint = new Point(3, 5);
        setTileAtPoint(testDiamondMinePoint, TileType.RESOURCE_DIAMOND);
        Point testEmeraldMinePoint = new Point(3, 9);
        setTileAtPoint(testEmeraldMinePoint, TileType.RESOURCE_EMERALD);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_DOWN,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineSamePricePreferLowDistanceLeft() {
        thisPlayerLocation = new Point(3, 7);
        Point testFarMinePoint = new Point(6, 7);
        setTileAtPoint(testFarMinePoint, TileType.RESOURCE_DIAMOND);
        Point testCloseMinePoint = new Point(1, 7);
        setTileAtPoint(testCloseMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_LEFT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineSamePricePreferLowDistanceRight() {
        thisPlayerLocation = new Point(3, 7);
        Point testFarMinePoint = new Point(5, 7);
        setTileAtPoint(testFarMinePoint, TileType.RESOURCE_DIAMOND);
        Point testCloseMinePoint = new Point(0, 7);
        setTileAtPoint(testCloseMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_RIGHT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineSamePricePreferLowDistanceUp() {
        thisPlayerLocation = new Point(6, 7);
        Point testFarMinePoint = new Point(6, 5);
        setTileAtPoint(testFarMinePoint, TileType.RESOURCE_DIAMOND);
        Point testCloseMinePoint = new Point(6, 9);
        setTileAtPoint(testCloseMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineSamePricePreferLowDistanceDown() {
        thisPlayerLocation = new Point(9, 4);
        Point testFarMinePoint = new Point(9, 2);
        setTileAtPoint(testFarMinePoint, TileType.RESOURCE_DIAMOND);
        Point testCloseMinePoint = new Point(9, 9);
        setTileAtPoint(testCloseMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_DOWN,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineCheaperButBetterValue() {
        thisPlayerLocation = new Point(4, 4);
        Point testBetterValueMinePoint = new Point(3, 3);
        setTileAtPoint(testBetterValueMinePoint, TileType.RESOURCE_RUBY);
        Point testWorseValueMinePoint = new Point(8, 7);
        setTileAtPoint(testWorseValueMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_LEFT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineFurtherButBetterValue() {
        thisPlayerLocation = new Point(6, 3);
        Point testBetterValueMinePoint = new Point(8, 7);
        setTileAtPoint(testBetterValueMinePoint, TileType.RESOURCE_DIAMOND);
        Point testWorseValueMinePoint = new Point(3, 3);
        setTileAtPoint(testWorseValueMinePoint, TileType.RESOURCE_RUBY);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_RIGHT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void mineOnPoint() {
        thisPlayerLocation = new Point(1, 9);
        Point testMinePoint = new Point(1, 9);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MINE,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void mineMultipleTurns() {
        thisPlayerLocation = new Point(1, 9);
        Point testMinePoint = new Point(1, 9);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);
        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);
        assertEquals(TurnAction.MINE,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));

    }

    @Test
    public void pickUpOnceFinishMining() {
        thisPlayerLocation = new Point(1, 9);
        Point testMinePoint = new Point(1, 9);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);
        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);

        setTileAtPoint(testMinePoint, TileType.EMPTY);
        playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        assertEquals(TurnAction.PICK_UP_RESOURCE,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));

    }

    @Test
    public void goTowardsNextMinePointAfterPickUp() {
        thisPlayerLocation = new Point(1, 9);
        Point testMinePoint = new Point(1, 9);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);
        Point testNextMinePoint = new Point(0, 7);
        setTileAtPoint(testNextMinePoint, TileType.RESOURCE_EMERALD);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);
        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);

        setTileAtPoint(testMinePoint, TileType.EMPTY);
        playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);
        assertEquals(TurnAction.MOVE_LEFT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMarketAfterPickUpIfInventoryFull() {
        thisPlayerLocation = new Point(6, 4);
        Point testMinePoint = new Point(6, 6);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);
        Point testMarketPoint = new Point(6, 1);
        setTileAtPoint(testMarketPoint, TileType.RED_MARKET);

        InventoryItem diamondItem = new InventoryItem(ItemType.DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        for (int i = 0; i < inventorySize; i++) {
            competitionStrategy.onReceiveItem(diamondItem);
        }

        assertEquals(TurnAction.MOVE_DOWN,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsPlayerColorMarket() {
        isRedPlayer = false;
        thisPlayerLocation = new Point(6, 4);
        Point testMinePoint = new Point(3, 4);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);
        Point testRedMarketPoint = new Point(6, 1);
        setTileAtPoint(testRedMarketPoint, TileType.RED_MARKET);
        Point testBlueMarketPoint = new Point(6, 7);
        setTileAtPoint(testBlueMarketPoint, TileType.BLUE_MARKET);

        InventoryItem diamondItem = new InventoryItem(ItemType.DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        for (int i = 0; i < inventorySize; i++) {
            competitionStrategy.onReceiveItem(diamondItem);
        }

        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsNextMinePointAfterMarket() {
        thisPlayerLocation = new Point(6, 4);
        Point testMarketPoint = new Point(6, 3);
        setTileAtPoint(testMarketPoint, TileType.RED_MARKET);
        Point testMinePoint = new Point(6, 9);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        InventoryItem diamondItem = new InventoryItem(ItemType.DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        for (int i = 0; i < inventorySize; i++) {
            competitionStrategy.onReceiveItem(diamondItem);
        }

        thisPlayerLocation = new Point(6, 3);
        playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);
        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);
        competitionStrategy.onSoldInventory(1000);
        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMineWithMinimumEnergyForNextTrip() {
        thisPlayerLocation = new Point(0, 1);
        Point testRechargePoint = new Point(0, 0);
        setTileAtPoint(testRechargePoint, TileType.RECHARGE);
        Point testMinePoint = new Point(0, 3);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_UP,
                competitionStrategy.getTurnAction(playerBoardView, economy, 5, true));
    }

    @Test
    public void goTowardsRechargeNotEnoughEnergyForTrip() {
        thisPlayerLocation = new Point(0, 1);
        Point testRechargePoint = new Point(0, 0);
        setTileAtPoint(testRechargePoint, TileType.RECHARGE);
        Point testMinePoint = new Point(0, 3);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_DOWN,
                competitionStrategy.getTurnAction(playerBoardView, economy, 4, true));
    }

    @Test
    public void noActionDuringRecharge() {
        thisPlayerLocation = new Point(3, 3);
        Point testRechargePoint = new Point(3, 3);
        setTileAtPoint(testRechargePoint, TileType.RECHARGE);
        Point testMinePoint = new Point(0, 3);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);


        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertNull(competitionStrategy.getTurnAction(playerBoardView, economy, 4, true));
    }

    @Test
    public void goTowardsNextMinePointAfterFullRecharge() {
        thisPlayerLocation = new Point(3, 3);
        Point testRechargePoint = new Point(3, 3);
        setTileAtPoint(testRechargePoint, TileType.RECHARGE);
        Point testMinePoint = new Point(0, 3);
        setTileAtPoint(testMinePoint, TileType.RESOURCE_DIAMOND);


        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_LEFT,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test
    public void goTowardsMarketWhenNoMinesLeft() {
        thisPlayerLocation = new Point(5, 5);
        Point testRechargePoint = new Point(3, 3);
        setTileAtPoint(testRechargePoint, TileType.RECHARGE);
        Point testMarketPoint = new Point(5, 4);
        setTileAtPoint(testMarketPoint, TileType.RED_MARKET);


        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(TurnAction.MOVE_DOWN,
                competitionStrategy.getTurnAction(playerBoardView, economy, 80, true));
    }

    @Test (expected = IllegalArgumentException.class)
    public void noMarketsOnBoard() {
        thisPlayerLocation = new Point(5, 5);
        setTileAtPoint(new Point(2, 2), TileType.EMPTY);
        setTileAtPoint(new Point(7, 7), TileType.EMPTY);
        setTileAtPoint(new Point(2, 7), TileType.EMPTY);
        setTileAtPoint(new Point(7, 2), TileType.EMPTY);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);

    }

    @Test (expected = IllegalArgumentException.class)
    public void noRechargeStationsOnBoard() {
        thisPlayerLocation = new Point(5, 5);
        setTileAtPoint(new Point(4, 4), TileType.EMPTY);
        setTileAtPoint(new Point(4, 5), TileType.EMPTY);
        setTileAtPoint(new Point(5, 4), TileType.EMPTY);
        setTileAtPoint(new Point(5, 5), TileType.EMPTY);

        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        competitionStrategy.getTurnAction(playerBoardView, economy, 80, true);
    }

    @Test
    public void testGetName() {
        PlayerBoardView playerBoardView = new PlayerBoardView(tileView, itemsOnGround,
                thisPlayerLocation, otherPlayerLocation, otherPlayerScore);

        competitionStrategy.initialize(boardLength, inventorySize, maxCharge, winningScore,
                playerBoardView, thisPlayerLocation, isRedPlayer, random);

        assertEquals(strategyName, competitionStrategy.getName());
    }

    /**
     * Sets an element in tileView to a specific tileType, based its corresponding Point coordinate
     *
     * @param point The Point coordinate of the tile to be changed
     * @param tileType The new type of the tile
     */
    private void setTileAtPoint(Point point, TileType tileType) {
        tileView[boardLength - 1 - point.y][point.x] = tileType;
    }
}