package Monster;

import Entity.Entity;
import Main.GamePanel;
import Obj.OBJ_Coin;
import Obj.OBJ_Heart;
import Obj.OBJ_Mana_Crystal;

import java.util.Random;

public class MON_Bat extends Entity {
    GamePanel gp;

    public MON_Bat(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Bat";
        defaultSpeed = 4;
        speed = defaultSpeed;
        maxLife = 7;
        life = maxLife;
        attack = 5;
        defence = 0;
        exp = 7;

        solidArea.x = 3;
        solidArea.y = 15;
        solidArea.width = 42;
        solidArea.height = 21;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage(){
        // Using two images (down1, down2) for all directions
        up1 = setup("/Resources/Monsters/bat_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/Resources/Monsters/bat_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/Resources/Monsters/bat_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/Resources/Monsters/bat_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/Resources/Monsters/bat_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/Resources/Monsters/bat_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/Resources/Monsters/bat_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/Resources/Monsters/bat_down_2", gp.tileSize, gp.tileSize);
    }

    public void setAction(){

        // Disabling this since I want this monster to be kinda more hard to kill
        // With implementing the aggro effect the monsters comes to you, and sometimes it gets easier to kill
        // if (onPath){
          //  checkStopChasingOrNot(gp.player, 15, 100);
          //  searchPath(getGoalCol(gp.player), getGoalRow(gp.player));

        // } else {
          //  checkStartChasingOrNot(gp.player, 5, 100);
            getRandomDirection(10);
        // }
    }

    public void damageReaction(){
        actionLockCounter = 0;

        // When the monster receive damage it starts moving away from the player
        // direction = gp.player.direction;

        // onPath = true;
    }

    public void checkDrop(){
        int i = new Random().nextInt(100)+1;

        if (i < 50){dropItem(new OBJ_Coin(gp));}
        if (i >= 50 && i < 75){dropItem(new OBJ_Heart(gp));}
        if (i >= 75 && i < 100){dropItem(new OBJ_Mana_Crystal(gp));}
    }
}
