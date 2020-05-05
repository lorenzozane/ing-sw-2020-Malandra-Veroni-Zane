package it.polimi.ingsw.model;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.Building.BuildingLevel;
import org.junit.Test;

public class WorkerTest {

  @Test
  public void setWorkerSlot() {
    Game gameInstance = new Game();
    Player player = new Player("test");
    Worker worker = new Worker(player, 1);
    Position positionBefore = new Position(0,0);
    Position positionAfter = new Position(1,1);
    Slot slotBefore = new Slot(positionBefore);
    Slot slotAfter = new Slot(positionAfter);

    worker.setWorkerSlot(slotBefore);
    worker.setWorkerSlot(slotAfter);

    assertEquals(worker, slotAfter.getWorkerInSlot());
    assertEquals(worker.getWorkerSlot(), slotAfter);
    assertNull(slotBefore.getWorkerInSlot());

  }

  @Test
  public void move() {
    Game gameInstance = new Game();
    Player player = new Player("test");
    Worker worker = new Worker(player, 1);
    Position positionBefore = new Position(0,0);
    Position positionAfter = new Position(1,1);
    Slot slotBefore = new Slot(positionBefore);
    Slot slotAfter = new Slot(positionAfter);

    worker.move(slotBefore);
    worker.move(slotAfter);

    assertEquals(worker, slotAfter.getWorkerInSlot());
    assertEquals(worker.getWorkerSlot(), slotAfter);
    assertNull(slotBefore.getWorkerInSlot());

  }

  @Test
  public void buildConstruction() {
    Game gameInstance = new Game();
    Player player = new Player("test");
    Worker worker = new Worker(player, 1);
    Position buildPosition = new Position(0,0);
    Slot buildingInSlot = new Slot(buildPosition);
    Building buildingNew = new Building(BuildingLevel.LEVEL1);

    worker.build(buildingInSlot);

    //assertEquals(buildingNew.getLevel(), buildingInSlot.getBuildingStatus()[1].getLevel());

  }

}