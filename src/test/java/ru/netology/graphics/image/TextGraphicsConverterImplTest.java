package ru.netology.graphics.image;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.graphics.image.BadImageSizeException;
import ru.netology.graphics.image.TextGraphicsConverterImpl;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class TextGraphicsConverterImplTest {
    TextGraphicsConverterImpl converterTest;

    @BeforeAll
    public static void startAll() {
        System.out.println("All tests started");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
        converterTest = new TextGraphicsConverterImpl();
    }

    @AfterEach
    public void stopTest() {
        System.out.println("Test finished");
        converterTest = null;
    }

    @AfterAll
    public static void stop() {
        System.out.println("All tests finished");
    }

    @Test
    public void testNewSizesMaxWidth() {
        //arrange
        int width = 1000, height = 1500;
        converterTest.setMaxWidth(500);
        int[] expected = {500, 750};
        //act
        int[] result = converterTest.newSizes(width, height);
        //assert
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNewSizesMaxHeight() {
        //arrange
        int width = 1000, height = 1500;
        converterTest.setMaxHeight(500);
        int[] expected = {333, 500};
        //act
        int[] result = converterTest.newSizes(width, height);
        //assert
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNewSizesLessMaxWidth() {
        //arrange
        int width = 1000, height = 1000;
        converterTest.setMaxWidth(1500);
        int[] expected = {1000, 1000};
        //act
        int[] result = converterTest.newSizes(width, height);
        //assert
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNewSizesLessMaxHeight() {
        //arrange
        int width = 1000, height = 1500;
        converterTest.setMaxHeight(1500);
        int[] expected = {1000, 1500};
        //act
        int[] result = converterTest.newSizes(width, height);
        //assert
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNewSizesBothSizeHeight() {
        //arrange
        int width = 1000, height = 1500;
        converterTest.setMaxHeight(500);
        converterTest.setMaxWidth(500);
        int[] expected = {333, 500};
        //act
        int[] result = converterTest.newSizes(width, height);
        //assert
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNewSizesBothSizeWidth() {
        //arrange
        int width = 1000, height = 1500;
        converterTest.setMaxHeight(500);
        converterTest.setMaxWidth(250);
        int[] expected = {250, 375};
        //act
        int[] result = converterTest.newSizes(width, height);
        //assert
        assertArrayEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    public void testNewSizesMaxWidthParam(int width, int height, int[] expected) {
        //arrange
        converterTest.setMaxWidth(500);
        //act
        int[] result = converterTest.newSizes(width, height);
        //assert
        assertArrayEquals(expected, result);
    }

    private static Stream<Arguments> argumentsStream() {
        return Stream.of(Arguments.of(1000, 1500, new int[]{500, 750}), Arguments.of(400, 500, new int[]{400, 500}));
    }

    @Test
    public void testCheckRatioException() {
        //arrange
        int width = 1000, height = 4500;
        converterTest.setMaxRatio(3);
        //assert
        assertThrows(BadImageSizeException.class,
                //act
                () -> converterTest.checkRatio(width, height));
    }

//    @Test
//    public void testCheckRatioNotException() {
//        //arrange
//        int width = 1000, height = 2500;
//        converterTest.setMaxRatio(3);
//        //assert
//        assertDoesNotThrow(
//                //act
//                () -> converterTest.checkRatio(width, height));
//    }

    @ParameterizedTest
    @MethodSource("argumentsStreamTwo")
    public void testCheckRatioDoesNotException(int width, int height) {
        //arrange
        converterTest.setMaxRatio(3);
        //assert
        assertDoesNotThrow(
                //act
                () -> converterTest.checkRatio(width, height));
    }

    private static Stream<Arguments> argumentsStreamTwo() {
        return Stream.of(Arguments.of(1000, 2500), Arguments.of(2500, 1000));
    }
}
