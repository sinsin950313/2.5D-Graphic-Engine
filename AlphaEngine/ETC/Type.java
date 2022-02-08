package seaweed.aeproject.AlphaEngine.ETC;

public class Type {
    public enum Item { HP, MP, ETC, WEAPON, ARMOR }
    public enum Direction_Vector{ R, RU, U, LU, L, LD, D, RD }
    public enum State { PLAN, BATTLE, LIST }
    public enum Axis { X_AXIS, Y_AXIS, Z_AXIS }
    public enum Touch{ IN_GAME, OUT_GAME }
    public enum GameObjectComponent { GRAPHIC, VOLUME, MATERIAL, TRIGGER, CUSTOM }
    public enum TouchType { UI, CAMERA }
    public enum TouchState { DOWN, MOVE, LONGTOUCH, UP }
    public enum TouchActionType { INPUT, OUTPUT, CHANGE }

    public class View_Type {
        public final static int INIT = 0;
        public final static int CHOOSE = 1 << 0;
        public final static int LIST = 1 << 1;
        public final static int SKILL = 1 << 2;
        public final static int CHARACTER_SKILL = 1 << 3;
        public final static int BATTLE = 1 << 4;
    }

    public class Object_Type {
        public final static int MASK = 1023;
        public final static int DESTROYABLE = 1 << 0;
        public final static int CHARACTER = 1 << 1;
        public final static int ALLIANCE = 1 << 2;
    }

    public class State_Type {
        public final static int MASK = 1023;//dead state cant control
        public final static int DEAD = 0;
        public final static int CALM = 1 << 0;
        public final static int FIGHT = 1 << 1;
        public final static int CONTROL = 1 << 2;
        public final static int ORDER = 1 << 3;
    }

    /*public class Character_List {
        public final static int ADAM = 1;
        public final static int EVE = 2;
    }*/
    public class Character_List {
        public final static int SHIKI = 1;
        public final static int AZAKA = 2;
        public final static int TOKO = 3;
    }
    public class BackGround_List
    {
        public final static int FOREST = 1;
    }
    public class Scenario_Type
    {
        public final static int TUTORIAL = 0;
    }
    public class Collide_Type
    {
        public final static int SPHERE = 1 << 0;
        public final static int CYLINDER = 1 << 1;
        public final static int RECTANGULAR = 1 << 2;
    }
    public class Draw_Type
    {
        public final static int OBJECT = 1 << 0;
        public final static int SPRITE = 1 << 1;
        public final static int UI = 1 << 2;
        public final static int FIELD = 1 << 3;
        public final static int SKY = 1 << 4;
        public final static int DOT = 1 << 5;
        public final static int LINE = 1 << 6;
    }
}
