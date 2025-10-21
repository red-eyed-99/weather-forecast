package dto.pageable;

public record PageableResult<T>(T content, PageInfo pageInfo) {
}