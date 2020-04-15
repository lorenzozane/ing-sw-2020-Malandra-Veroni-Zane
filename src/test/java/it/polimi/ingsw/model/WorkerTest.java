package it.polimi.ingsw.model;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.Building.BuildingLevel;
import org.junit.Test;

public class WorkerTest {

  @Test
  public void setWorkerSlot() {
    Worker worker = new Worker("test");
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
    Worker worker = new Worker("test");
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
    Worker worker = new Worker("test");
    Position buildPosition = new Position(0,0);
    Slot buildingInSlot = new Slot(buildPosition);
    Building buildingNew = new Building(BuildingLevel.LEVEL1);

    worker.buildConstruction(buildingInSlot, BuildingLevel.LEVEL1);

    //assertEquals(buildingNew.getLevel(), buildingInSlot.getBuildingStatus()[1].getLevel());

  }

}