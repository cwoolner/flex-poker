package com.flexpoker.table.query.repository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import com.flexpoker.web.dto.outgoing.TableDTO;

public class InMemoryTableRepositoryTest {

    @Test(expected = NullPointerException.class)
    public void testFetchNonExistentTable() {
        InMemoryTableRepository inMemoryTableRepository = new InMemoryTableRepository();
        inMemoryTableRepository.fetchById(UUID.randomUUID());
    }

    @Test
    public void testFetchExistingTable() {
        InMemoryTableRepository inMemoryTableRepository = new InMemoryTableRepository();
        UUID tableId = UUID.randomUUID();
        inMemoryTableRepository
                .save(new TableDTO(tableId, 1, null, 0, null, null, 0));
        assertNotNull(inMemoryTableRepository.fetchById(tableId));
    }

    @Test
    public void testSaveIncrementingVersions() {
        InMemoryTableRepository inMemoryTableRepository = new InMemoryTableRepository();
        UUID tableId = UUID.randomUUID();
        inMemoryTableRepository
                .save(new TableDTO(tableId, 1, null, 0, null, null, 0));
        inMemoryTableRepository
                .save(new TableDTO(tableId, 2, null, 0, null, null, 0));
        assertEquals(2,
                inMemoryTableRepository.fetchById(tableId).getVersion());
    }

    @Test
    public void testSaveDecrementingVersions() {
        InMemoryTableRepository inMemoryTableRepository = new InMemoryTableRepository();
        UUID tableId = UUID.randomUUID();
        inMemoryTableRepository
                .save(new TableDTO(tableId, 2, null, 0, null, null, 0));
        inMemoryTableRepository
                .save(new TableDTO(tableId, 1, null, 0, null, null, 0));
        assertEquals(2,
                inMemoryTableRepository.fetchById(tableId).getVersion());
    }

    @Test
    public void testSaveMultithreadVersions() throws InterruptedException {
        InMemoryTableRepository inMemoryTableRepository = new InMemoryTableRepository();
        UUID tableId = UUID.randomUUID();

        // creating a small number of threads/versions purposefully. a higher
        // number makes the invalid ordering/over-writing occur less often since
        // the odds of the max version thread running during the disorderly
        // begin time less likely
        Set<Thread> saveThreads = IntStream.range(1, 6).boxed()
                .map(x -> new TableDTO(tableId, x, null, 0, null, null, 0))
                .map(x -> new Thread(() -> inMemoryTableRepository.save(x)))
                .collect(Collectors.toSet());

        saveThreads.forEach(x -> x.start());
        // using a foreach cause of the checked exception
        for (Thread thread : saveThreads) {
            thread.join();
        }

        assertEquals(5,
                inMemoryTableRepository.fetchById(tableId).getVersion());
    }

}
