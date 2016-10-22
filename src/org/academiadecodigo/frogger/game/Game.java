package org.academiadecodigo.frogger.game;

import org.academiadecodigo.frogger.display.Direction;
import org.academiadecodigo.frogger.display.Field;
import org.academiadecodigo.frogger.display.FieldPosition;
import org.academiadecodigo.frogger.gameobjects.*;

/**
 * Created by codecadet on 21/10/16.
 */
public class Game {

    public static int os;

    private Field field;
    private Player player;
    private int delay;
    private Moveable[] moveables;
    private Collidable[] collidables;
    private Padawan[] padawans;
    private int carrierIndex;

    public Game(int delay) {
        this.field = new Field(18, 15);
        this.delay = delay;
    }

    public void init(int os) {
        this.os = os;

        player = new Player(field);
        GameObjectFactory gameObjectFactory = new GameObjectFactory();
        gameObjectFactory.fieldMapper(field);

        moveables = gameObjectFactory.getMoveables();
        collidables = gameObjectFactory.getCollidables();
        padawans = gameObjectFactory.getPadawans();

    }

    public void start() throws InterruptedException {


        while (!player.isDead()) {

            field.blackLimitsRedraw(); // Gives color to the vertical borders of the Field.
            Thread.sleep(delay);

            moveAll();
        }

    }

    private void moveAll() {

        if (player.getWillMove()) {
            player.move();

            if (!checkCarried()) {
                checkCollisions();

                if (player.isDead()){
                 return;
                }
            }
        }


        for (int i = 0; i < moveables.length; i++) {
            moveables[i].move();
            if(checkCarried()){
                player.getPos().setPos(padawans[carrierIndex].getPos().getCol(),padawans[carrierIndex].getPos().getRow());
                //reavaliar para o interface draggable
            }
        }

        if (!checkCarried()) {
            checkCollisions();
        }

    }

    private void checkCollisions() {
        for (int i = 0; i < collidables.length; i++) {
            if (player.getPos().equals(collidables[i].getPos())) {
                player.setDead();
                return;
            }
        }
    }

    private boolean checkCarried() {
        for (int i = 0; i < padawans.length; i++) {
            if (player.getPos().equals(padawans[i].getPos())) {
                carrierIndex = i;
                return true;
            }
        }
        return false;
    }

}
