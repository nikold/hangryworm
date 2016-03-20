package com.dubraver.game.screens;

public class WormPart {

    public int x;
    public int y;
    WormPart next;
    public WormPart(WormPart next, int x, int y) {
        this.next = next;
        this.x = x;
        this.y = y;
    }
}