package dto.pageable;

public record PageInfo(

        int totalPages,
        int currentPage,
        int previousBlockPageNumber,
        int nextBlockPageNumber,
        int[] pageNumbers
) {
}