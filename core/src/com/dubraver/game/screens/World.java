package com.dubraver.game.screens;

import com.badlogic.gdx.math.MathUtils;
import com.dubraver.game.utils.Constants;

class World {

    public Worm worm;
    public int height;
    public int width;
    Food food;
    public World(int height, int width) {
        this.height = height;
        this.width = width;
        worm = new Worm(this);
        CreateFood();
    }

    public Food CreateFood(){
        int x = (int) MathUtils.random(2, Constants.WORLD_WIDTH -2);
        int y = (int) MathUtils.random(2, Constants.WORLD_HEIGHT -2);
        food = new Food(x, y);
        return food;
    }
}