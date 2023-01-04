package Main;

import Entity.NPC_OldMan;
import Monster.MON_GreenSlime;
import Obj.*;
import TileInteractive.IT_DryTree;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    // What object you want to place on the map and on what coordinates
    public void setObject(){
        int i = 0;

        gp.obj[i] = new OBJ_Axe(gp);
        gp.obj[i].worldX = gp.tileSize * 33;
        gp.obj[i].worldY = gp.tileSize * 7;
        i++;

        gp.obj[i] = new OBJ_Shield_Blue(gp);
        gp.obj[i].worldX = gp.tileSize * 35;
        gp.obj[i].worldY = gp.tileSize * 21;

    }

    public void setNPC(){
        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tileSize * 21; // col
        gp.npc[0].worldY = gp.tileSize * 21; // row
    }

    public void setMonster(){
        int i = 0;

        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 21;
        gp.monster[i].worldY = gp.tileSize * 38;
        i++;

        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 42;
        i++;

        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 24;
        gp.monster[i].worldY = gp.tileSize * 37;
        i++;

        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 34;
        gp.monster[i].worldY = gp.tileSize * 42;
        i++;

        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 38;
        gp.monster[i].worldY = gp.tileSize * 42;
    }

    public void setInteractiveTiles(){
        int i = 0;
        gp.iTile[i] = new IT_DryTree(gp, 27, 12);i++;
        gp.iTile[i] = new IT_DryTree(gp, 28, 12);i++;
        gp.iTile[i] = new IT_DryTree(gp, 29, 12);i++;
        gp.iTile[i] = new IT_DryTree(gp, 30, 12);i++;
        gp.iTile[i] = new IT_DryTree(gp, 31, 12);i++;
        gp.iTile[i] = new IT_DryTree(gp, 32, 12);i++;
        gp.iTile[i] = new IT_DryTree(gp, 33, 12);i++;
        gp.iTile[i] = new IT_DryTree(gp, 31, 21);i++;

        gp.iTile[i] = new IT_DryTree(gp, 13, 40);i++;
        gp.iTile[i] = new IT_DryTree(gp, 14, 40);i++;
        gp.iTile[i] = new IT_DryTree(gp, 15, 40);i++;
        gp.iTile[i] = new IT_DryTree(gp, 16, 40);i++;
        gp.iTile[i] = new IT_DryTree(gp, 17, 40);i++;
        gp.iTile[i] = new IT_DryTree(gp, 18, 40);i++;
        gp.iTile[i] = new IT_DryTree(gp, 13, 41);i++;
        gp.iTile[i] = new IT_DryTree(gp, 12, 41);i++;
        gp.iTile[i] = new IT_DryTree(gp, 11, 41);i++;
        gp.iTile[i] = new IT_DryTree(gp, 10, 41);i++;
        gp.iTile[i] = new IT_DryTree(gp, 10, 40);
    }
}