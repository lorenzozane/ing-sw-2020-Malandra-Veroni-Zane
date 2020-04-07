package it.polimi.ingsw.model;

import static org.junit.Assert.*;

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

  }

  @Test
  public void buildConstruction() {

  }

}