package it.polimi.ingsw.model;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.Building.BuildingLevel;
import org.junit.Test;

public class WorkerTest {

  Game gameInstance = new Game();

  @Test
  public void setWorkerSlot() {
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
    Player player = new Player("test");
    Position positionBefore = new Position(0,0);
    Position positionAfter = new Position(1,1);
    Slot slotBefore = new Slot(positionBefore);
    Slot slotAfter = new Slot(positionAfter);

    Worker worker = player.getWorkers().get(0);
    worker.move(slotBefore);
    worker.move(slotAfter);

    assertEquals(worker, slotAfter.getWorkerInSlot());
    assertEquals(worker.getWorkerSlot(), slotAfter);
    assertNull(slotBefore.getWorkerInSlot());

  }

  @Test
  public void buildConstruction() {
    Player player = new Player("test");
    Worker worker = player.getWorkers().get(0);
    Position buildPosition = new Position(0,0);
    Slot buildingInSlot = new Slot(buildPosition);
    Building buildingNew = new Building(BuildingLevel.LEVEL1);

    worker.build(buildingInSlot);

    assertEquals(buildingNew.getLevel(), buildingInSlot.getConstructionTopLevel());

  }

}