package com.dubraver.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.dubraver.game.utils.Constants;

public class Worm {

    public Array<WormPart> wormParts;
    WormPart head;
    public enum Directions {LEFT, RIGHT, UP, DOWN};
    public Directions direction;
    World world;

    public Worm(World world) {
        this.world = world;
        head = new WormPart(null, world.height / 2, world.width / 2);
        wormParts = new Array<WormPart>();
        wormParts.add(head);
        direction = Directions.LEFT;
    }

    float deltaStep = 0;
    public void MoveWorm(Directions direction){

        deltaStep+= Gdx.graphics.getDeltaTime();
        if (deltaStep < Constants.WORLD_SPEED){
            return;
        }
        deltaStep = 0;

        this.direction = direction;

        for (int i = wormParts.size - 1; i > 0 ; i--) {

            WormPart before = wormParts.get(i-1);
            WormPart part = wormParts.get(i);
            part.x = before.x;
            part.y = before.y;
        }

        switch (direction) {
            case LEFT:
                this.head.x-=1;
                break;
            case RIGHT:
                this.head.x+=1;
                break;
            case UP:
                this.head.y+=1;
                break;
            case DOWN:
                this.head.y-=1;
                break;
        }

        if (this.head.x < 0){
            this.head.x = world.width;
        }

        if (this.head.x > world.width){
            this.head.x = 0;
        }
        if (this.head.y < 0){
            this.head.y = world.height;
        }
        if (this.head.y > world.height){
            this.head.y = 0;
        }
    }

    public void EatFood(){
        WormPart tailWormPart = GetTail();
        WormPart newWormPart = new WormPart(tailWormPart, tailWormPart.x, tailWormPart.y);
        wormParts.add(newWormPart);
        this.world.CreateFood();
    }

    public WormPart GetHead(){return head;}

    public WormPart GetTail(){return wormParts.get(wormParts.size - 1);}
}