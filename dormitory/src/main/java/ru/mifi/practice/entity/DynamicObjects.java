package ru.mifi.practice.entity;

import ru.mifi.practice.room.Room;
import ru.mifi.practice.ui.Color;
import ru.mifi.practice.ui.Handler;
import ru.mifi.practice.ui.Screen;

import java.awt.*;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public final class DynamicObjects {

    public static Entity.Human createPlayer(Handler input, Room root) {
        return new Player(input, root);
    }

    private abstract static class AbstractDynamicEntity implements Entity.Dynamic {
        protected final Random random = new Random();
        private final UUID id;
        protected int xr = 6;
        protected int yr = 6;
        protected int x;
        protected int y;
        protected int z;
        protected int side;

        protected AbstractDynamicEntity(int x, int y, int z, int side) {
            this.id = UUID.randomUUID();
            this.x = x;
            this.y = y;
            this.z = z;
            this.side = side;
        }

        @Override
        public int x() {
            return x;
        }

        @Override
        public int y() {
            return y;
        }

        @Override
        public int z() {
            return z;
        }

        @Override
        public UUID id() {
            return id;
        }

        @Override
        public void tick() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isRemoved() {
            return false;
        }

        @Override
        public boolean intersects(int x0, int y0, int x1, int y1) {
            return !(x + xr < x0 || y + yr < y0 || x - xr > x1 || y - yr > y1);
        }

        public boolean move(Room room, int xa, int ya) {
            if (xa != 0 || ya != 0) {
                boolean stopped = true;
                if (xa != 0 && move2(room, xa, 0)) {
                    stopped = false;
                }
                if (ya != 0 && move2(room, 0, ya)) {
                    stopped = false;
                }
                if (!stopped) {
                    int xt = x >> 4;
                    int yt = y >> 4;
                    room.getTile(xt, yt).steppedOn(room, xt, yt, this);
                }
                return !stopped;
            }
            return true;
        }

        protected boolean move2(Room room, int xa, int ya) {
            if (xa != 0 && ya != 0) throw new IllegalArgumentException("Move2 can only move along one axis at a time!");

            int xto0 = ((x) - xr) >> 4;
            int yto0 = ((y) - yr) >> 4;
            int xto1 = ((x) + xr) >> 4;
            int yto1 = ((y) + yr) >> 4;

            int xt0 = ((x + xa) - xr) >> 4;
            int yt0 = ((y + ya) - yr) >> 4;
            int xt1 = ((x + xa) + xr) >> 4;
            int yt1 = ((y + ya) + yr) >> 4;
            boolean blocked = false;
            for (int yt = yt0; yt <= yt1; yt++)
                for (int xt = xt0; xt <= xt1; xt++) {
                    if (xt >= xto0 && xt <= xto1 && yt >= yto0 && yt <= yto1) continue;
                    room.getTile(xt, yt).bumpedInto(room, xt, yt, this);
                    if (!room.getTile(xt, yt).mayPass(room, xt, yt, this)) {
                        blocked = true;
                        return false;
                    }
                }
            if (blocked) {
                return false;
            }

            Set<Entity> wasInside = room.getEntities(x - xr, y - yr, x + xr, y + yr);
            Set<Entity> isInside = room.getEntities(x + xa - xr, y + ya - yr, x + xa + xr, y + ya + yr);
            for (Entity e : isInside) {
                if (e == this) {
                    continue;
                }
                e.touchedBy(this);
            }
            isInside.removeAll(wasInside);
            for (Entity e : isInside) {
                if (e == this) {
                    continue;
                }
                if (e.blocks(this)) {
                    return false;
                }
            }
            x += xa;
            y += ya;
            return true;
        }
    }

    private static final class Player extends AbstractDynamicEntity implements Entity.Human {
        private final Handler input;
        private final Room room;
        private final int maxStamina = 10;
        public Item attackItem;
        public Item activeItem;
        private int walkDist = 0;
        private int dir = 0;
        private int hurtTime = 0;
        private int xKnockback;
        private int yKnockback;
        private int attackTime;
        private int attackDir;
        private volatile boolean updated = false;
        private int stamina = 10;
        private int health = 10;
        private int staminaRecharge;
        private int staminaRechargeDelay = 40;
        private State state = State.STAY;

        public Player(Handler input, Room instance) {
            super(24, 24, 0, 1);
            this.input = input;
            this.room = instance;
        }

        @Override
        public int stamina() {
            return stamina;
        }

        @Override
        public int health() {
            return health;
        }

        @Override
        public int staminaRechargeDelay() {
            return staminaRechargeDelay;
        }

        @Override
        public Point move() {
            int xa = 0;
            int ya = 0;
            if (input.up.down) {
                ya--;
            }
            if (input.down.down) {
                ya++;
            }
            if (input.left.down) {
                xa--;
            }
            if (input.right.down) {
                xa++;
            }

            {
//                if (isSwimming()) {
//                    if (swimTimer++ % 2 == 0) return true;
//                }
                if (xKnockback < 0) {
                    move2(room, -1, 0);
                    xKnockback++;
                }
                if (xKnockback > 0) {
                    move2(room, 1, 0);
                    xKnockback--;
                }
                if (yKnockback < 0) {
                    move2(room, 0, -1);
                    yKnockback++;
                }
                if (yKnockback > 0) {
                    move2(room, 0, 1);
                    yKnockback--;
                }
//                if (hurtTime > 0)
//                    return true;
                if (xa != 0 || ya != 0) {
                    walkDist++;
                    if (xa < 0) dir = 2;
                    if (xa > 0) dir = 3;
                    if (ya < 0) dir = 1;
                    if (ya > 0) dir = 0;
                }
            }

            {
                if (xa != 0 || ya != 0) {
                    boolean stopped = true;
                    if (xa != 0 && move2(room, xa, 0)) {
                        stopped = false;
                    }
                    if (ya != 0 && move2(room, 0, ya)) {
                        stopped = false;
                    }
                    if (!stopped) {
                        int xt = x >> 4;
                        int yt = y >> 4;
                        room.getTile(xt, yt).steppedOn(room, xt, yt, this);
                    }
                }
            }


            return new Point(xa, ya);
        }

        @Override
        public void render(Screen screen) {
            int xt = 0;
            int yt = 14;

            int flip1 = (walkDist >> 3) & 1;
            int flip2 = (walkDist >> 3) & 1;

            if (dir == 1) {
                xt += 2;
            }
            if (dir > 1) {
                flip1 = 0;
                flip2 = ((walkDist >> 4) & 1);
                if (dir == 2) {
                    flip1 = 1;
                }
                xt += 4 + ((walkDist >> 3) & 1) * 2;
            }

            int xo = x - 8;
            int yo = y - 11;
//            if (isSwimming()) {
//                yo += 4;
//                int waterColor = Color.get(-1, -1, 115, 335);
//                if (tickTime / 8 % 2 == 0) {
//                    waterColor = Color.get(-1, 335, 5, 115);
//                }
//                screen.render(xo + 0, yo + 3, 5 + 13 * 32, waterColor, 0);
//                screen.render(xo + 8, yo + 3, 5 + 13 * 32, waterColor, 1);
//            }

            if (attackTime > 0 && attackDir == 1) {
                screen.render(xo + 0, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
                screen.render(xo + 8, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 1);
                if (attackItem != null) {
                    attackItem.renderIcon(screen, xo + 4, yo - 4);
                }
            }
            int col = Color.get(-1, 100, 220, 532);
            if (hurtTime > 0) {
                col = Color.get(-1, 555, 555, 555);
            }

//            if (activeItem instanceof FurnitureItem) {
//                yt += 2;
//            }
            screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
            screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
            screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
            screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);

            if (attackTime > 0 && attackDir == 2) {
                screen.render(xo - 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 1);
                screen.render(xo - 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 3);
                if (attackItem != null) {
                    attackItem.renderIcon(screen, xo - 4, yo + 4);
                }
            }
            if (attackTime > 0 && attackDir == 3) {
                screen.render(xo + 8 + 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
                screen.render(xo + 8 + 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 2);
                if (attackItem != null) {
                    attackItem.renderIcon(screen, xo + 8 + 4, yo + 4);
                }
            }
            if (attackTime > 0 && attackDir == 0) {
                screen.render(xo + 0, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 2);
                screen.render(xo + 8, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 3);
                if (attackItem != null) {
                    attackItem.renderIcon(screen, xo + 4, yo + 8 + 4);
                }
            }

//            if (activeItem instanceof FurnitureItem) {
//                Furniture furniture = ((FurnitureItem) activeItem).furniture;
//                furniture.x = x;
//                furniture.y = yo;
//                furniture.render(screen);
//            }
        }

        @Override
        public int getLightRadius() {
//            int r = 10;
//            if (activeItem != null) {
//                if (activeItem instanceof LightItem li) {
//                    int rr = li.getLightRadius();
//                    if (rr > r)
//                        r = rr;
//                }
//            }
            return 5;
        }

        @Override
        public void tick() {
            updated = false;

            if (room != null) {
//                Tile onTile = level.getTile(x >> 4, y >> 4);
//                if (onTile == Tile.stairsDown || onTile == Tile.stairsUp) {
//                    if (onStairDelay == 0) {
//                        changeLevel((onTile == Tile.stairsUp) ? 1 : -1);
//                        onStairDelay = 10;
//                        return;
//                    }
//                    onStairDelay = 10;
//                } else {
//                    if (onStairDelay > 0) {
//                        onStairDelay--;
//                    }
//                }
            }

            if (stamina <= 0 && staminaRechargeDelay == 0 && staminaRecharge == 0) {
                staminaRechargeDelay = 40;
            }

            if (staminaRechargeDelay > 0) {
                staminaRechargeDelay--;
            }

            if (staminaRechargeDelay == 0) {
                staminaRecharge++;
                while (staminaRecharge > 10) {
                    staminaRecharge -= 10;
                    if (stamina < maxStamina) {
                        stamina++;
                    }
                }
                updated = true;
            }

            Point move = move();
            int xa = move.x;
            int ya = move.y;

            if (staminaRechargeDelay % 2 == 0) {
                x += xa;
                y += ya;
                state = State.WALK;
//                if (xa > 0 || ya > 0) {
//                    stamina--;
//                    staminaRecharge = 0;
//                }
            } else {
                state = State.STAY;
            }

            if (input.isAttacked()) {
                if (stamina == 0) {

                } else {
                    stamina--;
                    staminaRecharge = 0;
                    attack();
                    state = State.ATCK;
                }
                updated = true;
            }
            if (attackTime > 0)
                attackTime--;
        }

        @Override
        public State state() {
            return state;
        }

        private boolean use() {
            int yo = -2;
            if (dir == 0 && use(x - 8, y + 4 + yo, x + 8, y + 12 + yo)) return true;
            if (dir == 1 && use(x - 8, y - 12 + yo, x + 8, y - 4 + yo)) return true;
            if (dir == 3 && use(x + 4, y - 8 + yo, x + 12, y + 8 + yo)) return true;
            if (dir == 2 && use(x - 12, y - 8 + yo, x - 4, y + 8 + yo)) return true;

            int xt = x >> 4;
            int yt = (y + yo) >> 4;
            int r = 12;
            if (attackDir == 0) yt = (y + r + yo) >> 4;
            if (attackDir == 1) yt = (y - r + yo) >> 4;
            if (attackDir == 2) xt = (x - r) >> 4;
            if (attackDir == 3) xt = (x + r) >> 4;

            if (xt >= 0 && yt >= 0 && xt < room.width() && yt < room.height()) {
                if (room.getTile(xt, yt).use(room, xt, yt, this, attackDir)) {
                    return true;
                }
            }

            return false;
        }

        private void attack() {
            walkDist += 8;
            attackDir = dir;
            attackItem = activeItem;
            boolean done = false;

            if (activeItem != null) {
                attackTime = 10;
                int yo = -2;
                int range = 12;
                if (dir == 0 && interact(x - 8, y + 4 + yo, x + 8, y + range + yo)) done = true;
                if (dir == 1 && interact(x - 8, y - range + yo, x + 8, y - 4 + yo)) done = true;
                if (dir == 3 && interact(x + 4, y - 8 + yo, x + range, y + 8 + yo)) done = true;
                if (dir == 2 && interact(x - range, y - 8 + yo, x - 4, y + 8 + yo)) done = true;
                if (done) return;

                int xt = x >> 4;
                int yt = (y + yo) >> 4;
                int r = 12;
                if (attackDir == 0) yt = (y + r + yo) >> 4;
                if (attackDir == 1) yt = (y - r + yo) >> 4;
                if (attackDir == 2) xt = (x - r) >> 4;
                if (attackDir == 3) xt = (x + r) >> 4;

                if (xt >= 0 && yt >= 0 && xt < room.width() && yt < room.height()) {
                    if (activeItem.interactOn(room.getTile(xt, yt), room, xt, yt, this, attackDir)) {
                        done = true;
                    } else {
                        if (room.getTile(xt, yt).interact(room, xt, yt, this, activeItem, attackDir)) {
                            done = true;
                        }
                    }
                    if (activeItem.isDepleted()) {
                        activeItem = null;
                    }
                }
            }

            if (done) return;

            if (activeItem == null || activeItem.canAttack()) {
                attackTime = 5;
                int yo = -2;
                int range = 20;
                if (dir == 0) hurt(x - 8, y + 4 + yo, x + 8, y + range + yo);
                if (dir == 1) hurt(x - 8, y - range + yo, x + 8, y - 4 + yo);
                if (dir == 3) hurt(x + 4, y - 8 + yo, x + range, y + 8 + yo);
                if (dir == 2) hurt(x - range, y - 8 + yo, x - 4, y + 8 + yo);

                int xt = x >> 4;
                int yt = (y + yo) >> 4;
                int r = 12;
                if (attackDir == 0) yt = (y + r + yo) >> 4;
                if (attackDir == 1) yt = (y - r + yo) >> 4;
                if (attackDir == 2) xt = (x - r) >> 4;
                if (attackDir == 3) xt = (x + r) >> 4;

                if (xt >= 0 && yt >= 0 && xt < room.width() && yt < room.height()) {
                    room.getTile(xt, yt).hurt(room, xt, yt, this, random.nextInt(3) + 1, attackDir);
                }
            }

        }

        private boolean use(int x0, int y0, int x1, int y1) {
            Set<Entity> entities = room.getEntities(x0, y0, x1, y1);
            for (Entity e : entities) {
                if (e != this) {
                    if (e.use(this, attackDir)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean interact(int x0, int y0, int x1, int y1) {
            Set<Entity> entities = room.getEntities(x0, y0, x1, y1);
            for (Entity e : entities) {
                if (e != this) {
                    if (e.interact(this, activeItem, attackDir)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void hurt(int x0, int y0, int x1, int y1) {
            Set<Entity> entities = room.getEntities(x0, y0, x1, y1);
            for (Entity e : entities) {
                if (e != this) {
                    e.hurt(this, getAttackDamage(e), attackDir);
                }
            }
        }

        private int getAttackDamage(Entity e) {
            int dmg = random.nextInt(3) + 1;
            if (attackItem != null) {
                dmg += attackItem.getAttackDamageBonus(e);
            }
            return dmg;
        }
    }
}
