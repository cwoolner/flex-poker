package com.flexpoker.table.query.repository.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.flexpoker.web.dto.outgoing.TableDTO;

public class InMemoryTableRepositoryTest {

    @Test
    void testFetchNonExistentTable() {
        var inMemoryTableRepository = new InMemoryTableRepository();
        assertThrows(NullPointerException.class,
                () -> inMemoryTableRepository.fetchById(UUID.randomUUID()));
    }

    @Test
    void testFetchExistingTable() {
        var inMemoryTableRepository = new InMemoryTableRepository();
        var tableId = UUID.randomUUID();
        inMemoryTableRepository.save(new TableDTO(tableId, 1, null, 0, null, null, 0, UUID.randomUUID()));
        assertNotNull(inMemoryTableRepository.fetchById(tableId));
    }

    @Test
    void testSaveIncrementingVersions() {
        var inMemoryTableRepository = new InMemoryTableRepository();
        var tableId = UUID.randomUUID();
        inMemoryTableRepository.save(new TableDTO(tableId, 1, null, 0, null, null, 0, UUID.randomUUID()));
        inMemoryTableRepository.save(new TableDTO(tableId, 2, null, 0, null, null, 0, UUID.randomUUID()));
        assertEquals(2, inMemoryTableRepository.fetchById(tableId).getVersion());
    }

    @Test
    void testSaveDecrementingVersions() {
        var inMemoryTableRepository = new InMemoryTableRepository();
        var tableId = UUID.randomUUID();
        inMemoryTableRepository.save(new TableDTO(tableId, 2, null, 0, null, null, 0, UUID.randomUUID()));
        inMemoryTableRepository.save(new TableDTO(tableId, 1, null, 0, null, null, 0, UUID.randomUUID()));
        assertEquals(2, inMemoryTableRepository.fetchById(tableId).getVersion());
    }

    @Test
    void testSaveDuplicateVersionsDoNotOverwrite() {
        var inMemoryTableRepository = new InMemoryTableRepository();
        var tableId = UUID.randomUUID();
        inMemoryTableRepository.save(new TableDTO(tableId, 2, null, 10, null, null, 0, UUID.randomUUID()));
        inMemoryTableRepository.save(new TableDTO(tableId, 2, null, 20, null, null, 0, UUID.randomUUID()));
        assertEquals(10, inMemoryTableRepository.fetchById(tableId).getTotalPot());
    }

    @Test
    void testSaveMultithreadVersions() throws InterruptedException {
        var inMemoryTableRepository = new InMemoryTableRepository();
        var tableId = UUID.randomUUID();

        // creating a small number of threads/versions purposefully. a higher
        // number makes the invalid ordering/over-writing occur less often since
        // the odds of the max version thread running during the disorderly
        // begin time less likely
        var saveThreads = IntStream.rangeClosed(1, 5).boxed()
                .map(x -> new TableDTO(tableId, x, null, 0, null, null, 0, UUID.randomUUID()))
                .map(x -> new Thread(() -> inMemoryTableRepository.save(x)))
                .collect(Collectors.toSet());

        saveThreads.forEach(x -> x.start());
        // using a foreach cause of the checked exception
        for (var thread : saveThreads) {
            thread.join();
        }

        assertEquals(5, inMemoryTableRepository.fetchById(tableId).getVersion());
    }

    @Test
    void testFetchMultithreaded() throws InterruptedException {
        var inMemoryTableRepository = new InMemoryTableRepository();
        var tableId = UUID.randomUUID();
        inMemoryTableRepository.save(new TableDTO(tableId, 1, null, 0, null, null, 0, UUID.randomUUID()));

        var testAssertsPassed1 = new CopyOnWriteArrayList<>();
        var readThreads1 = IntStream.rangeClosed(1, 5).boxed()
                .map(x -> new Thread(() -> {
                    try {
                        assertEquals(1, inMemoryTableRepository.fetchById(tableId).getVersion());
                        testAssertsPassed1.add(true);
                    } catch (AssertionError e) {
                        testAssertsPassed1.add(false);
                    }
                }))
                .collect(Collectors.toSet());

        readThreads1.forEach(x -> x.start());
        // using a foreach cause of the checked exception
        for (var thread : readThreads1) {
            thread.join();
        }

        assertArrayEquals(new Boolean[]{true, true, true, true, true}, testAssertsPassed1.toArray());

        var save2Thread = new Thread(() -> inMemoryTableRepository
                .save(new TableDTO(tableId, 2, null, 0, null, null, 0, UUID.randomUUID())));
        save2Thread.start();

        var testAssertsPassed2 = new CopyOnWriteArrayList<>();
        var readThreads2 = IntStream.rangeClosed(1, 5).boxed()
                .map(x -> new Thread(() -> {
                    try {
                        assertEquals(2, inMemoryTableRepository.fetchById(tableId).getVersion());
                        testAssertsPassed2.add(true);
                    } catch (AssertionError e) {
                        testAssertsPassed2.add(false);
                    }
                }))
                .collect(Collectors.toSet());

        readThreads2.forEach(x -> x.start());

        // using a foreach cause of the checked exception
        for (var thread : readThreads2) {
            thread.join();
        }

        save2Thread.join();

        assertArrayEquals(new Boolean[]{true, true, true, true, true}, testAssertsPassed2.toArray());
    }

}
