package com.flexpoker.game.command.aggregate;

public class TableBalancerTest {

    private TableBalancer tableBalancer;
    
    /*
    @Test
    public void testSimpleSingleTable() {
        game = GameGenerator.createGame(9, 9);
        Table table1 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 3, 9);
        game.addTable(table1);
        assertTrue(command.execute(game));
    }
    
    @Test
    public void testImbalanceByTwoUnderMaxThreshold() {
        game = GameGenerator.createGame(9, 9);
        Table table1 = new Table();
        Table table2 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 3, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
        game.addTable(table1);
        game.addTable(table2);
        assertFalse(command.execute(game));
    }
    
    
    @Test
    public void testBalancedByOneUnderMaxThreshold() {
        game = GameGenerator.createGame(18, 9);
        Table table1 = new Table();
        Table table2 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 9);
        game.addTable(table1);
        game.addTable(table2);
        assertTrue(command.execute(game));
    }
    
    @Test
    public void testShouldOnlyBeTwoTablesNotThree() {
        game = GameGenerator.createGame(18, 9);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 4, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 4, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertFalse(command.execute(game));
    }
    
    @Test
    public void testThreeTablesTwoOutOfBalance() {
        game = GameGenerator.createGame(27, 9);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 9, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 8, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertFalse(command.execute(game));
    }
    
    @Test
    public void testThreeTablesPerfectlyInBalance() {
        game = GameGenerator.createGame(27, 9);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 7, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertTrue(command.execute(game));
    }
    
    @Test
    public void testShouldBeExactlyTwoTablesNotThree() {
        game = GameGenerator.createGame(27, 9);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 6, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 6, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertFalse(command.execute(game));
    }
    
    @Test
    public void testShouldBeExactlyThreeTables() {
        game = GameGenerator.createGame(27, 6);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 6, 6);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 6);
        DataUtilsForTests.fillTableWithUsers(table3, 6, 6);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertTrue(command.execute(game));
    }
    */
}
