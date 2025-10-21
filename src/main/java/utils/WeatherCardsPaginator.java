package utils;

import dto.pageable.PageInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WeatherCardsPaginator {

    public static final int LOCATIONS_PER_PAGE = 6;

    private static final int PAGES_PER_BLOCK = 5;

    public static PageInfo getPageInfo(int totalElements, int pageNumber) {
        var totalPages = (int) Math.ceil((double) totalElements / LOCATIONS_PER_PAGE);

        var previousBlockPageNumber = getPreviousBlockPageNumber(pageNumber);
        var nextBlockPageNumber = getNextBlockPageNumber(totalElements, pageNumber);

        var pageNumbers = getPageNumbers(totalElements, pageNumber);

        return new PageInfo(totalPages, pageNumber, previousBlockPageNumber, nextBlockPageNumber, pageNumbers);
    }

    private static int getPreviousBlockPageNumber(int pageNumber) {
        var currentBlockLastPageNumber = getCurrentBlockLastPageNumber(pageNumber);
        var currentBlockNumber = getCurrentBlockNumber(currentBlockLastPageNumber);
        return currentBlockNumber * PAGES_PER_BLOCK - PAGES_PER_BLOCK;
    }

    private static int getNextBlockPageNumber(int totalElements, int pageNumber) {
        var nextBlockPageNumber = 0;

        var currentBlockLastPageNumber = getCurrentBlockLastPageNumber(pageNumber);

        if (currentBlockLastPageNumber * LOCATIONS_PER_PAGE < totalElements) {
            var currentBlockNumber = getCurrentBlockNumber(currentBlockLastPageNumber);

            var nextPage = 1;

            nextBlockPageNumber = currentBlockNumber * PAGES_PER_BLOCK + nextPage;
        }

        return nextBlockPageNumber;
    }

    private static int getCurrentBlockLastPageNumber(int pageNumber) {
        return (int) Math.ceil((double) pageNumber / PAGES_PER_BLOCK) * PAGES_PER_BLOCK;
    }

    private static int getCurrentBlockNumber(int currentBlockLastPageNumber) {
        return currentBlockLastPageNumber / PAGES_PER_BLOCK;
    }

    private static int[] getPageNumbers(int totalElements, int pageNumber) {
        var currentBlockLastPageNumber = getCurrentBlockLastPageNumber(pageNumber);
        var currentBlockFirstPageNumber = currentBlockLastPageNumber - (PAGES_PER_BLOCK - 1);

        var currentBlockNumber = getCurrentBlockNumber(currentBlockLastPageNumber);

        var maxPossibleElements = currentBlockNumber * PAGES_PER_BLOCK * LOCATIONS_PER_PAGE;

        var pagesCount = PAGES_PER_BLOCK;

        if (maxPossibleElements > totalElements) {
            var diffBetweenPossibleAndMax = Math.abs(maxPossibleElements - totalElements);
            pagesCount = PAGES_PER_BLOCK - diffBetweenPossibleAndMax / LOCATIONS_PER_PAGE;
        }

        var pageNumbers = new int[pagesCount];

        for (int i = 0; i < pagesCount; i++) {
            pageNumbers[i] = currentBlockFirstPageNumber + i;
        }

        return pageNumbers;
    }
}