package spw4.game2048;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameImplTest {

    Game sut;

    @Mock
    Random randomTileSelector;

    @Mock
    Random randomPositionSelector;

    @BeforeEach
    void setup() {
        sut = new GameImpl();
    }

    @Test
    void game_notInitialized_methodsThrowException() {
        sut = new GameImpl(); //reset sut to not initialized game
        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> sut.isOver()),
                () -> assertThrows(IllegalStateException.class, () -> sut.isWon()),
                () -> assertThrows(IllegalStateException.class, () -> sut.getValueAt(1, 1)),
                () -> assertThrows(IllegalStateException.class, () -> sut.getScore()),
                () -> assertThrows(IllegalStateException.class, () -> sut.getMoves()),
                () -> assertThrows(IllegalStateException.class, () -> sut.move(Direction.down)),
                () -> assertThrows(IllegalStateException.class, () -> sut.toString())
        );
    }

    @Test
    void newGame_withoutMoves_scoreAndMovesZero() {
        sut.initialize();
        assertAll(
                () -> assertEquals(0, sut.getMoves()),
                () -> assertEquals(0, sut.getScore())
        );
    }

    @Test
    void initialize_randomPositionForTilesGenerated() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0)
                .thenReturn(0);

        when(randomTileSelector.nextDouble())
                .thenReturn(0.0)
                .thenReturn(0.8);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        assertAll(
                () -> assertEquals(2, sut.getValueAt(0, 0)),
                () -> assertEquals(2, sut.getValueAt(0, 1))
        );
    }

    @Test
    void initialize_randomValueForIsFourForGeneratedTile() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0)
                .thenReturn(0);

        when(randomTileSelector.nextDouble())
                .thenReturn(0.0)
                .thenReturn(0.9);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        assertAll(
                () -> assertEquals(2, sut.getValueAt(0, 0)),
                () -> assertEquals(4, sut.getValueAt(0, 1))
        );
    }


    @Test
    void getValueAt_emptyTileReturnZero() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(5)
                .thenReturn(9);

        when(randomTileSelector.nextDouble())
                .thenReturn(0.0)
                .thenReturn(0.9);
        GameImpl.randomTileSelector = randomTileSelector;
        GameImpl.randomPositionSelector = randomPositionSelector;

        sut.initialize();

        var result = sut.getValueAt(0, 0);

        assertEquals(0, result);
    }


   @Test
   void isOver_gameInitialized_isFalse() {
        sut.initialize();
        assertFalse(sut.isOver());
   }


   @Test
   void isOver_boardIsFull_isTrue() {
        when(randomTileSelector.nextDouble())
                .thenReturn(
                        0.2, 1.0, 0.2, 1.0,
                        1.0, 0.2, 1.0, 0.2,
                        0.2, 1.0, 0.2, 1.0,
                        1.0, 0.2, 1.0, 0.2);

        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0);

       GameImpl.randomTileSelector = randomTileSelector;
       GameImpl.randomPositionSelector = randomPositionSelector;
       sut.initialize();

       for (int i = 0; i < 14; i++) {
           sut.move(Direction.up);
       }

       assertAll(
               () -> assertTrue(sut.isOver()),
               () -> assertFalse(sut.isWon())
       );
   }

   @Test
   void move_mergeTwoTiles_direction_right() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0);
        when(randomTileSelector.nextDouble())
                .thenReturn(0.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        sut.move(Direction.right);

        assertEquals(4, sut.getValueAt(0, 3));
   }

    @Test
    void move_mergeMultipleTiles_direction_right() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0);
        when(randomTileSelector.nextDouble())
                .thenReturn(0.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        sut.move(Direction.up);
        sut.move(Direction.up);

        sut.move(Direction.right);
        assertEquals(4, sut.getValueAt(0, 3));
    }

    @Test
    void move_mergeTwoTiles_direction_left() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0);
        when(randomTileSelector.nextDouble())
                .thenReturn(0.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        sut.move(Direction.left);

        assertEquals(4, sut.getValueAt(0, 0));
    }

    @Test
    void move_mergeMultipleTiles_direction_left() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0);
        when(randomTileSelector.nextDouble())
                .thenReturn(0.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        sut.move(Direction.up);
        sut.move(Direction.up);

        sut.move(Direction.left);
        assertEquals(4, sut.getValueAt(0, 0));
    }

    @Test
    void move_mergeTwoTiles_direction_up() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0, 3);
        when(randomTileSelector.nextDouble())
                .thenReturn(0.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        sut.move(Direction.up);

        assertEquals(4, sut.getValueAt(0, 0));
    }

    @Test
    void move_mergeMultipleTiles_direction_up() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0, 3, 6, 9);
        when(randomTileSelector.nextDouble())
                .thenReturn(0.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        sut.move(Direction.left);
        sut.move(Direction.left);

        sut.move(Direction.up);
        assertEquals(4, sut.getValueAt(0, 0));
    }

    @Test
    void move_mergeTwoTiles_direction_down() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0, 3);
        when(randomTileSelector.nextDouble())
                .thenReturn(0.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        sut.move(Direction.down);

        assertEquals(4, sut.getValueAt(3, 0));
    }

    @Test
    void move_mergeMultipleTiles_direction_down() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0, 3, 6, 9);
        when(randomTileSelector.nextDouble())
                .thenReturn(0.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        sut.move(Direction.left);
        sut.move(Direction.left);

        sut.move(Direction.down);
        assertEquals(4, sut.getValueAt(3, 0));
    }

    @Test
    void getScore_mergeMultipleTiles_increaseScore() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0);
        when(randomTileSelector.nextDouble())
                .thenReturn(0.0, 0.0, 0.0, 1.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        sut.move(Direction.up);
        sut.move(Direction.up);

        sut.move(Direction.right);
        sut.move(Direction.right);
        assertEquals(12, sut.getScore());
    }

    @Test
    void toString_gameInitialized_returnsString() {
        sut.initialize();
        assertFalse(sut.toString().isBlank());
    }

    @Test
    void isWon_2048available_returnsTrue() {
        when(randomPositionSelector.nextInt(anyInt(), anyInt()))
                .thenReturn(0);
        when(randomTileSelector.nextDouble())
                .thenReturn(1.0);

        GameImpl.randomPositionSelector = randomPositionSelector;
        GameImpl.randomTileSelector = randomTileSelector;
        sut.initialize();

        ((GameImpl) sut).setTileAt(3, 0, 2048);
        assertTrue(sut.isWon());
    }
}
