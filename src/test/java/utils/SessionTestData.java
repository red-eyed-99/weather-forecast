package utils;

import lombok.experimental.UtilityClass;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class SessionTestData {

    public static final Set<UUID> EXPIRED_SESSION_IDS = Set.of(
            UUID.fromString("c9df446d-211b-4a55-8e86-20a0479c2b16"),
            UUID.fromString("82f9133c-e74e-43a2-a6cc-094ab39c6ba6"),
            UUID.fromString("1a9eb72e-27e4-4b10-aab3-ca8cab761b04")
    );

    public static final Set<UUID> ACTIVE_SESSION_IDS = Set.of(
            UUID.fromString("04f96dd2-5cc1-40af-b219-b0abfbd0a1df"),
            UUID.fromString("450c3bda-ac47-47a9-8bf9-7b23e1f51d8a"),
            UUID.fromString("5588cf8b-0074-4df9-ade2-c8bdb2da0c02")
    );
}