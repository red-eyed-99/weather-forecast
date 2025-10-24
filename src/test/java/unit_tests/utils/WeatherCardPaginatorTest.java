package unit_tests.utils;

import dto.pageable.PageInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.WeatherCardsPaginator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherCardPaginatorTest {

    @ParameterizedTest
    @MethodSource("getCorrectPageInfoTestArguments")
    void shouldReturnCorrectPageInfo(int totalElements, int pageNumber, PageInfo expectedPageInfo) {
        var actualPageInfo = WeatherCardsPaginator.getPageInfo(totalElements, pageNumber);

        assertAll(
                () -> assertEquals(expectedPageInfo.totalPages(), actualPageInfo.totalPages()),
                () -> assertEquals(expectedPageInfo.currentPage(), actualPageInfo.currentPage()),
                () -> assertEquals(expectedPageInfo.previousBlockPageNumber(), actualPageInfo.previousBlockPageNumber()),
                () -> assertEquals(expectedPageInfo.nextBlockPageNumber(), actualPageInfo.nextBlockPageNumber()),
                () -> assertArrayEquals(expectedPageInfo.pageNumbers(), actualPageInfo.pageNumbers())
        );
    }

    static Stream<Arguments> getCorrectPageInfoTestArguments() {
        return Stream.of(
                Arguments.of(1, 1, new PageInfo(1, 1, 0, 0, new int[]{1})),
                Arguments.of(6, 1, new PageInfo(1, 1, 0, 0, new int[]{1})),
                Arguments.of(7, 2, new PageInfo(2, 2, 0, 0, new int[]{1, 2})),
                Arguments.of(30, 5, new PageInfo(5, 5, 0, 0, new int[]{1, 2, 3, 4, 5})),
                Arguments.of(31, 6, new PageInfo(6, 6, 5, 0, new int[]{6})),
                Arguments.of(31, 5, new PageInfo(6, 5, 0, 6, new int[]{1, 2, 3, 4, 5})),
                Arguments.of(60, 8, new PageInfo(10, 8, 5, 0, new int[]{6, 7, 8, 9, 10})),
                Arguments.of(66, 9, new PageInfo(11, 9, 5, 11, new int[]{6, 7, 8, 9, 10})),
                Arguments.of(66, 11, new PageInfo(11, 11, 10, 0, new int[]{11}))
        );
    }
}