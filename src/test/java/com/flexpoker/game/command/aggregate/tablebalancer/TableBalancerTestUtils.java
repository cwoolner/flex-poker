package com.flexpoker.game.command.aggregate.tablebalancer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableBalancerTestUtils {

    /**
     * Helper method to create a set of table to player maps. The subject
     * table/number of players is treated specially in order to keep some
     * control over the tests.
     * 
     * @param subjectTableId
     * @param numberOfPlayersAtEachTable
     * @return
     */
    public static Map<UUID, Set<UUID>> createTableToPlayersMap(
            UUID subjectTableId, int numberOfSubjectTablePlayers,
            int... numberOfPlayersAtOtherTables) {
        Map<UUID, Set<UUID>> tableToPlayersMap = new HashMap<>();
        tableToPlayersMap.put(subjectTableId,
                createRandomSetOfPlayerIds(numberOfSubjectTablePlayers));

        for (int i = 0; i < numberOfPlayersAtOtherTables.length; i++) {
            tableToPlayersMap.put(UUID.randomUUID(), createRandomSetOfPlayerIds(
                    numberOfPlayersAtOtherTables[i]));
        }

        return tableToPlayersMap;
    }

    private static Set<UUID> createRandomSetOfPlayerIds(int numberOfPlayers) {
        return Stream.iterate(0, e -> e + 1).limit(numberOfPlayers)
                .map(x -> UUID.randomUUID()).collect(Collectors.toSet());
    }

}
