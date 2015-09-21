//package org.example.wordis;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Typeface;
//import android.graphics.drawable.BitmapDrawable;
//import android.media.AudioManager;
//import android.media.SoundPool;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.util.Log;
//import android.view.Display;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.WindowManager;
//
//import com.kkbnart.wordis.R;
//
//class CanvasSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
//	// COLOR NO,Red,Green,Orange,Blue,Yellow,Purple,Pink
//	public static final int NO = 0;
//	public static final int Re = 1;
//	public static final int G1 = 2;
//	public static final int G2 = 3;
//	public static final int B = 4;
//	public static final int Y1 = 5;
//	public static final int Y2 = 6;
//	public static final int P = 7;
//	public static final int GR = 8;
//	public static final int WH = 9;
//	public static final int COLOR[] = { 0, Color.rgb(255, 0, 0),
//			Color.rgb(0, 255, 0), Color.rgb(255, 128, 0),
//			Color.rgb(100, 150, 255), Color.rgb(255, 255, 0),
//			Color.rgb(128, 0, 255), Color.rgb(255, 0, 255) };
//	public static final int BLACK = Color.rgb(0, 0, 0);
//	public static final int GREY = Color.rgb(128, 128, 128);
//	public static final int DGREY = Color.rgb(64, 64, 64);
//	public static final int LGREY = Color.rgb(192, 192, 192);
//	public static final int ORANGE = Color.rgb(255, 128, 0);
//	public static final int WHITE = Color.rgb(255, 255, 255);
//	public static final String ALPHABET[] = { "a", "b", "c", "d", "e", "f",
//			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
//			"t", "u", "v", "w", "x", "y", "z", "?", "十", "ー", "", "=", "↑", "↓" };
//	public static final String GREEK[] = { "α", "β", "γ", "δ", "ε", "ζ", "η",
//			"θ", "i", "κ", "λ", "μ", "ν", "ξ", "ο", "π", "ρ", "σ", "τ", "υ",
//			"φ", "χ", "ψ", "ω", "Й", "ж", "?", "十", "ー", "", "=", "↑", "↓" };
//
//	// size constants
//	private static final long BASE_WIDTH = 480;
//	private static final long BASE_HEIGHT = 800;
//	public static final int WIDTH = 10;
//	public static final int HEIGHT = 20;
//	public static final int LENGTH = 30;
//	private static final int CORNER_X = 20;
//	private static final int CORNER_Y = 20;
//	// get display size
//	private Display disp;
//	private int Width;
//	private int Height;
//
//	// thread
//	public Handler mHandler = new Handler();
//
//	// main arrays
//	private static final int ALP_SIZE = LENGTH - 4;
//	private int alphabet[][] = new int[WIDTH][HEIGHT];
//	private int color[][] = new int[WIDTH][HEIGHT];
//	// common use of NO (static declaration of line 1) when state[][] is no (no
//	// block exist)
//	public static final int STABLE = 1;
//	public static final int MOVE = 2;
//	private int state[][] = new int[WIDTH][HEIGHT];
//
//	// declaration of position and alphabet of moving blocks
//	private int set_x[] = new int[4];
//	private int set_y[] = new int[4];
//	private static final double ALP_PROB[] = { 86.0, 91.0, 94.0, 96.0, 98.0,
//			100.0 };
//	// private static final double ALP_PROB[] = {50.0, 70.0, 80.0, 90.0,
//	// 90.0,100.0};
//	private int set_alp[] = new int[4];
//	private int set_color;
//	private int set_dx = 0;
//	private int set_dy = 0;
//	private int set_state = 0;// 0:normal,1:rotate 90 degree clockwise,2:rotate
//								// 180 degree clockwise,3:rotate 270 degree
//								// clockwise
//
//	// set next blocks
//	private static final int NEXT_POS[][] = { { 0, 0 }, { 375, 125 },
//			{ 390, 110 }, { 360, 110 }, { 360, 95 }, { 375, 95 }, { 375, 95 },
//			{ 375, 95 } };
//	private static final int NEXT_STATE[] = { 0, 2, 1, 3, 0, 0, 0, 1 };
//	private int set_next_alp[] = new int[4];
//	private int set_next_color;
//	// candidate for alphabets
//	private int cand_alp[] = new int[10];
//	private int cand_alp_len;
//	private int cand_ans[][] = new int[16][10];
//	private int cand_ans_len[] = new int[16];
//	private int ans[] = new int[10];
//	private int ans_len;
//	private int ans_data;
//
//	// objective position to center blocks of each shape
//	private int obpx[][][] = new int[8][4][4];
//	private int obpy[][][] = new int[8][4][4];
//
//	// count the time of downing block
//	private int count = 0;
//
//	// delete line variables
//	private int line_list[] = new int[5];// list of delete line
//	private int line_del_mode[] = new int[5];// del_mode of each line 0:point*4
//												// 1:normal 2:point-
//	private int line_num = 0;// count of delete line
//	private int line_add = 0;// count of plus and minus blocks
//
//	// point,level
//	private int point = 0;
//	private int d_point = 0;
//	private int dp_display_count = 0;
//	private int level = 1;
//	private int levelup_flag = 0;
//	private int deleted_lines = 0;
//
//	// time to drop *[50ms]
//	private static final int DROP_TIME[] = { 18, 15, 12, 16, 12, 8, 4 };
//	private int time;
//	private long sleep_time = 50;
//	private long prev_time;
//	private long now_time;
//
//	// joy stick flag
//	private static final int KEYCONTROL = 0;
//	private static final int TOUCHCONTROL = 1;
//	private int mode = KEYCONTROL;
//	private int left = 0;
//	private int right = 0;
//	private int down = 0;
//	private int double_down = 0;
//	// rotation direction
//	private static final int ROTATE_RIGHT = 0;
//	private static final int ROTATE_LEFT = 1;
//	private int rotate_direc = ROTATE_RIGHT;
//	private int rotate = 0;
//	// pause
//	private int pause = 0;
//	private static final int PAUSE_LINE_NUM = 6;
//	private static final int SETTING_LINE_NUM = 4;
//	private static final int SPMENU_LINE_NUM = 5;
//	private static final int PAUSE_LINE[] = { 100, 200, 300, 400, 500, 600, 700 };
//	private static final int PAUSE_MAX = (PAUSE_LINE_NUM + 1) * 100;
//	private static final int PAUSE_SUB = 50;
//	private static final int BOX_POSX[] = { 20, 460 };
//	private int pause_count = 0;
//
//	// mode of the game
//	private static final int MENU = 0;
//	private static final int PLAY = 1;
//	private static final int PAUSE = 2;
//	private static final int SCORE = 3;
//	private static final int SETTING = 4;
//	private static final int SPMENU = 5;
//	private static final int SPEASY = 6;
//	private static final int SPMEDIUM = 7;
//	private static final int SPHARD = 8;
//	private static final int SPHELL = 9;
//	private int menu_flag = MENU;
//	private int prepause_menu_flag = menu_flag;// divide play to pause and
//												// special to pause
//	private int presetting_menu_flag = menu_flag;// divide pause to setting and
//													// menu to setting
//
//	// special mode constants
//	public static final int NORMAL = 0;
//	public static final int SP_NICO = 1;
//	public static final int SP_GREEK = 2;
//	public static final int SP_3 = 3;
//	public static final int SP_4 = 4;
//	public static final int SP_5 = 5;
//	public int special = NORMAL;
//
//	// etc flags
//	public int reset_flag = 0;// 1:while pause reset is selected , used in
//								// activity
//	private int gameover_flag = 0;// 1:game over
//	private int gameover_count = 0;
//	private int open_level = 1;// player can set special mode according to the
//								// rank of top score
//	private int delete_flag = 0;// if delete flag rise
//	private int delete_count = 0;
//	private int delete_time = 10;// frame of delete effect
//	private int delete_run_speed = 40;// delete comment and blocks speed
//	private int time_count = 0;// for graphics of time
//	private int sound_flag = 0;// 0:sound off 1:sound on
//	private int read_executed_flag = 0;// ranking.txt is read:1 or not:0
//	private int start_count = 0;// start with counting
//	private int start_count_flag = 0;
//
//	// moving background
//	private int movebg_count = 0;
//	private int temp_movebg_count = 0;
//	private static final double REDUCE_RATE = 1.1;
//	private int touch_speed = 0;
//	private int count_downtoup = 0;
//	private static final int MPF = 4;// move per frame
//	private static final int TOUCH_BOUND = 120;
//	private int touch_pos[][] = new int[3][2];
//	private static final int TOUCH_DOWN = 1;
//	private static final int TOUCH_MOVE = 2;
//	private static final int TOUCH_UP = 3;
//	private int touch_state = NO;
//	private int movebg_no[] = new int[16];
//
//	// comment for sp_nico
//	private static final int CMT_ROW = 30;
//	private static final int CMT_COLUMN = 2;
//	private static final int CMT_INT = 40;
//	private static final int CMT_MAXCOUNT = 60;
//	private static final int CMT_TXTMV = 40;
//	private static final int CMT_TXTST = 48;
//	private String comment[] = new String[40];
//	private int runcmt_no[] = new int[500];
//	private int runcmt_x[] = new int[500];
//	private int runcmt_y[] = new int[500];
//	private int runcmt_speed[] = new int[500];
//	private int runcmt_count[] = new int[500];
//	private int run_count = 0;
//	private int cmt_no[][] = new int[CMT_ROW][CMT_COLUMN];
//
//	// effect for sp_greek
//	private int string_no[] = new int[5];
//	private int string_x[] = new int[5];
//	private int string_y[] = new int[5];
//	private int string_color[] = new int[5];
//	private int string_count[] = new int[5];
//	private int string_last_no = 0;
//
//	// effect for special mode
//	private int arrow_no[] = new int[15];
//	private int arrow_x[] = new int[15];
//	private int arrow_y[] = new int[15];
//	private int arrow_size[] = new int[15];
//	private int arrow_color[] = new int[15];
//	private int arrow_count[] = new int[15];
//	private int arrow_last_no = 0;
//
//	// ranking
//	private static final int SCORE_PLAY = 0;
//	private static final int SCORE_SPEASY = 1;
//	private static final int SCORE_SPMEDIUM = 2;
//	private static final int SCORE_SPHARD = 3;
//	private static final int SCORE_SPHELL = 4;
//	public int rank[][] = new int[5][10];
//	public int rank_line[][] = new int[5][10];
//	private int score_menu = SCORE_PLAY;
//
//	// text position constants (base size 480 800)
//	private static final int TEXT_MATRIX_ROW = 10;
//	private static final int TEXT_MATRIX_COLUMN = 21;
//	private String Text[][] = {
//			{ "W O R D I S", "W O R D I S", "start game", "high scores",
//					"settings", "??????",
//					"Copyright © 2012 ArtGames All Rights Reserved.", "", "",
//					"", "", "", "", "", "", "", "", "", "", "", "" },
//			{ "←", "→", "↓", "↓↓", "PAUSE", "NEXT", "WORD IS ...", "LV.      ",
//					"", "SCORE. ", "", "LINES.  ", "", "get point",
//					"↑level up↑", "a", "n", "s", "w", "e", "r" },
//			{ "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
//					"", "", "", "", "" },
//			{ "4", "5", "SCORE TOP 5 !", "", "points", "lines", "", "points",
//					"lines", "", "points", "lines", "", "points", "lines", "",
//					"points", "lines", "", "", "" },
//			{ "Sound Off", "Vibration Off", "Return To Top", "volume: ", "",
//					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
//					"" },
//			{ "EASY", "MEDIUM", "HARD", "HELL", "Return To Top", "", "", "",
//					"", "", "", "", "", "", "", "", "", "", "", "", "" },
//			{ "→", "←", "↑", "↑↑", "PAUSE", "NEXT", "WORD IS ...", "LV.      ",
//					"", "SCORE. ", "", "LINES.  ", "", "get point",
//					"↑level up↑", "a", "n", "s", "w", "e", "r" },
//			{ "←", "→", "↓", "↓↓", "PAUSE", "NEXT", "WORD IS ...", "LV.      ",
//					"", "SCORE. ", "", "LINES.  ", "", "get point",
//					"↑level up↑", "a", "n", "s", "w", "e", "r" },
//			{ "←", "→", "↓", "↓↓", "PAUSE", "NEXT", "WORD IS ...", "LV.      ",
//					"", "SCORE. ", "", "LINES.  ", "", "get point",
//					"↑level up↑", "a", "n", "s", "w", "e", "r" },
//			{ "←", "→", "↓", "↓↓", "PAUSE", "NEXT", "WORD IS ...", "LV.      ",
//					"", "SCORE. ", "", "LINES.  ", "", "get point",
//					"↑level up↑", "a", "n", "s", "w", "e", "r" } };
//	private static final long RATE_TEXT_X[][] = {
//			{ 50, 50, 135, 128, 165, 180, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0, 0 },
//			{ 50, 250, 160, 372, 358, 350, 335, 335, 335, 335, 335, 335, 335,
//					365, 315, 335, 360, 385, 410, 435, 460 },
//			{ 130, 130, 140, 170, 180, 140, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0, 0 },
//			{ 60, 60, 40, 270, 300, 280, 270, 300, 280, 270, 300, 280, 270,
//					300, 280, 270, 300, 280, 0, 0, 0 },
//			{ 170, 140, 140, 160, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0 },
//			{ 175, 150, 175, 175, 130, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0, 0 },
//			{ 50, 250, 160, 372, 358, 350, 335, 335, 335, 335, 335, 335, 335,
//					365, 315, 335, 360, 385, 410, 435, 460 },
//			{ 50, 250, 160, 372, 358, 350, 335, 335, 335, 335, 335, 335, 335,
//					365, 315, 335, 360, 385, 410, 435, 460 },
//			{ 50, 250, 160, 372, 358, 350, 335, 335, 335, 335, 335, 335, 335,
//					365, 315, 335, 360, 385, 410, 435, 460 },
//			{ 50, 250, 160, 372, 358, 350, 335, 335, 335, 335, 335, 335, 335,
//					365, 315, 335, 360, 385, 410, 435, 460 } };
//	private int Text_X[][] = new int[TEXT_MATRIX_ROW][TEXT_MATRIX_COLUMN];
//	private static final long RATE_TEXT_Y[][] = {
//			{ 122, 120, 300, 420, 540, 665, 780, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0, 0, 0 },
//			{ 660, 660, 760, 760, 680, 40, 240, 330, 360, 390, 420, 450, 480,
//					505, 545, 280, 280, 280, 280, 280, 280 },
//			{ 170, 270, 370, 460, 570, 670, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0, 0 },
//			{ 480, 590, 80, 150, 145, 190, 260, 255, 300, 370, 365, 410, 480,
//					475, 520, 590, 585, 630, 0, 0, 0 },
//			{ 170, 370, 470, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0 },
//			{ 170, 270, 370, 470, 570, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0, 0 },
//			{ 660, 660, 760, 760, 680, 40, 240, 330, 360, 390, 420, 450, 480,
//					505, 545, 280, 280, 280, 280, 280, 280 },
//			{ 660, 660, 760, 760, 680, 40, 240, 330, 360, 390, 420, 450, 480,
//					505, 545, 280, 280, 280, 280, 280, 280 },
//			{ 660, 660, 760, 760, 680, 40, 240, 330, 360, 390, 420, 450, 480,
//					505, 545, 280, 280, 280, 280, 280, 280 },
//			{ 660, 660, 760, 760, 680, 40, 240, 330, 360, 390, 420, 450, 480,
//					505, 545, 280, 280, 280, 280, 280, 280 } };
//	private int Text_Y[][] = new int[TEXT_MATRIX_ROW][TEXT_MATRIX_COLUMN];
//	private static final int RATE_TEXT_SIZE[][] = {
//			{ 70, 66, 40, 40, 40, 40, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0 },
//			{ 40, 40, 35, 35, 25, 30, 20, 20, 20, 20, 20, 20, 20, 30, 30, 20,
//					20, 20, 20, 20, 20 },
//			{ 40, 40, 40, 40, 40, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0 },
//			{ 45, 45, 55, 40, 40, 30, 40, 40, 30, 40, 40, 30, 40, 40, 30, 40,
//					40, 30, 0, 0, 0 },
//			{ 40, 40, 40, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 40, 40, 40, 40, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0 },
//			{ 40, 40, 35, 35, 25, 30, 20, 20, 20, 20, 20, 20, 20, 30, 30, 20,
//					20, 20, 20, 20, 20 },
//			{ 40, 40, 35, 35, 25, 30, 20, 20, 20, 20, 20, 20, 20, 30, 30, 20,
//					20, 20, 20, 20, 20 },
//			{ 40, 40, 35, 35, 25, 30, 20, 20, 20, 20, 20, 20, 20, 30, 30, 20,
//					20, 20, 20, 20, 20 },
//			{ 40, 40, 35, 35, 25, 30, 20, 20, 20, 20, 20, 20, 20, 30, 30, 20,
//					20, 20, 20, 20, 20 } };
//	private int RATE_TEXT_COLOR[][] = {
//			{ BLACK, COLOR[Y1], BLACK, BLACK, BLACK, BLACK, WHITE, 0, 0, 0, 0,
//					0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ BLACK, BLACK, BLACK, BLACK, BLACK, ORANGE, WHITE, WHITE, WHITE,
//					WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, BLACK, BLACK,
//					BLACK, BLACK, BLACK, BLACK },
//			{ BLACK, BLACK, COLOR[Re], BLACK, BLACK, COLOR[G1], 0, 0, 0, 0, 0,
//					0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ BLACK, BLACK, BLACK, COLOR[Re], COLOR[Re], COLOR[Re], COLOR[Y1],
//					COLOR[Y1], COLOR[Y1], COLOR[B], COLOR[B], COLOR[B], BLACK,
//					BLACK, BLACK, BLACK, BLACK, BLACK, 0, 0, 0 },
//			{ LGREY, LGREY, BLACK, BLACK, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0, 0, 0, 0 },
//			{ COLOR[P], COLOR[G1], COLOR[B], COLOR[Re], BLACK, 0, 0, 0, 0, 0,
//					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ BLACK, BLACK, BLACK, BLACK, BLACK, ORANGE, WHITE, WHITE, WHITE,
//					WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, BLACK, BLACK,
//					BLACK, BLACK, BLACK, BLACK },
//			{ BLACK, BLACK, BLACK, BLACK, BLACK, ORANGE, WHITE, WHITE, WHITE,
//					WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, BLACK, BLACK,
//					BLACK, BLACK, BLACK, BLACK },
//			{ BLACK, BLACK, BLACK, BLACK, BLACK, ORANGE, WHITE, WHITE, WHITE,
//					WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, BLACK, BLACK,
//					BLACK, BLACK, BLACK, BLACK },
//			{ BLACK, BLACK, BLACK, BLACK, BLACK, ORANGE, WHITE, WHITE, WHITE,
//					WHITE, WHITE, WHITE, WHITE, BLACK, BLACK, BLACK, BLACK,
//					BLACK, BLACK, BLACK, BLACK } };
//
//	// images
//	// private BitmapFactory.Options options = new BitmapFactory.Options();
//	private BitmapDrawable bg[] = new BitmapDrawable[18];
//	private BitmapDrawable block[] = new BitmapDrawable[10];
//	private BitmapDrawable Bitmap[] = new BitmapDrawable[30];
//
//	// bitmap position constants (base size 480 800)
//	private static final int BITMAP_MATRIX_ROW = 10;
//	private static final int BITMAP_MATRIX_COLUMN = 7;
//	private int BITMAP_NO[][] = { { 7, 7, 7, 8, -1, -1, -1 },
//			{ 0, 0, 0, 0, 1, 2, 10 }, { -1, -1, -1, -1, -1, -1, -1 },
//			{ 3, 4, 5, 23, 22, 11, -1 }, { 18, 20, 11, -1, -1, -1, -1 },
//			{ -1, -1, -1, -1, 11, -1, -1 }, { 0, 0, 0, 0, 1, 2, 10 },
//			{ 0, 0, 0, 0, 1, 2, 10 }, { 0, 0, 0, 0, 1, 2, 10 },
//			{ 0, 0, 0, 0, 1, 2, 10 } };
//	private static final int RATE_BITMAP_POS[][][] = {
//			{ { 110, 260, 370, 320 }, { 110, 380, 370, 440 },
//					{ 110, 500, 370, 560 }, { 110, 620, 370, 680 },
//					{ 40, 20, 80, 60 }, { 80, 20, 120, 60 },
//					{ 120, 20, 160, 60 } },
//			{ { 30, 625, 110, 675 }, { 230, 625, 310, 675 },
//					{ 130, 725, 210, 775 }, { 360, 720, 420, 780 },
//					{ 340, 640, 460, 700 }, { 135, 630, 205, 700 },
//					{ 330, 260, 330, 285 } },
//			{ { 30, 110, 110, 190 }, { 30, 210, 110, 290 },
//					{ 30, 310, 110, 390 }, { 30, 410, 110, 490 },
//					{ 30, 510, 110, 590 }, { 35, 615, 105, 685 },
//					{ 300, 400, 380, 480 } },
//			{ { 40, 110, 120, 170 }, { 40, 220, 120, 280 },
//					{ 40, 330, 120, 390 }, { 30, 685, 150, 735 },
//					{ 330, 685, 450, 735 }, { 200, 670, 280, 750 },
//					{ 0, 0, 0, 0 } },
//			{ { 30, 110, 110, 190 }, { 30, 310, 110, 390 },
//					{ 30, 410, 110, 490 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
//					{ 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
//			{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
//					{ 30, 510, 110, 590 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
//			{ { 30, 625, 110, 675 }, { 230, 625, 310, 675 },
//					{ 130, 725, 210, 775 }, { 360, 720, 420, 780 },
//					{ 340, 640, 460, 700 }, { 135, 630, 205, 700 },
//					{ 330, 260, 330, 285 } },
//			{ { 30, 625, 110, 675 }, { 230, 625, 310, 675 },
//					{ 130, 725, 210, 775 }, { 360, 720, 420, 780 },
//					{ 340, 640, 460, 700 }, { 135, 630, 205, 700 },
//					{ 330, 260, 330, 285 } },
//			{ { 30, 625, 110, 675 }, { 230, 625, 310, 675 },
//					{ 130, 725, 210, 775 }, { 360, 720, 420, 780 },
//					{ 340, 640, 460, 700 }, { 135, 630, 205, 700 },
//					{ 330, 260, 330, 285 } },
//			{ { 30, 625, 110, 675 }, { 230, 625, 310, 675 },
//					{ 130, 725, 210, 775 }, { 360, 720, 420, 780 },
//					{ 340, 640, 460, 700 }, { 135, 630, 205, 700 },
//					{ 330, 260, 330, 285 } } };
//	private int Bitmap_Pos[][][] = new int[BITMAP_MATRIX_ROW][BITMAP_MATRIX_COLUMN][4];
//
//	// sounds
//	private SoundPool soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
//	private int si[] = new int[10];// Sound Id
//	public AudioManager audio;
//	public float volume = (float) 0.5;
//
//	// vibration
//	public static final int VIB_SHORT = 0;
//	public static final int VIB_LONG = 1;
//	public int vib_flag = 0;
//
//	// options
//	private int hell_order[] = new int[4];
//	private static final int HELL_SUN[][] = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 1, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0, 0, 1, 0 },
//			{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 },
//			{ 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 }, { 1, 0, 1, 0, 0, 0, 0, 1, 0, 1 },
//			{ 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 },
//			{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0, 0, 1, 0 },
//			{ 0, 0, 0, 1, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
//	private static final int HELL_MOON[][] = {
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 3, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 3, 0, 0 }, { 0, 0, 0, 3, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 }, { 0, 0, 0, 0, 3, 0, 0, 0, 0, 0 },
//			{ 2, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 }, { 0, 3, 0, 0, 0, 0, 0, 0, 1, 1 },
//			{ 0, 0, 0, 3, 0, 0, 0, 0, 1, 1 }, { 0, 0, 0, 0, 0, 0, 3, 0, 0, 1 },
//			{ 0, 0, 0, 0, 2, 0, 0, 0, 0, 1 }, { 0, 1, 0, 0, 0, 0, 0, 0, 1, 1 },
//			{ 1, 1, 0, 0, 0, 0, 0, 1, 1, 1 }, { 0, 1, 1, 0, 0, 0, 1, 1, 1, 0 },
//			{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 }, { 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 } };
//	private static final int HELL_RECT[][] = {
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
//			{ 0, 1, 0, 0, 0, 0, 0, 0, 1, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
//			{ 0, 1, 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 },
//			{ 0, 1, 0, 0, 0, 0, 0, 0, 1, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
//			{ 1, 0, 0, 0, 1, 1, 0, 0, 0, 1 }, { 0, 1, 0, 1, 0, 0, 1, 0, 1, 0 },
//			{ 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 }, { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0 },
//			{ 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 }, { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 },
//			{ 0, 1, 0, 0, 1, 1, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 },
//			{ 0, 1, 0, 1, 0, 0, 1, 0, 1, 0 }, { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1 } };
//	private static final int HELL_HELL[][] = {
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1 },
//			{ 1, 1, 1, 1, 0, 0, 1, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 1, 1, 1, 1 },
//			{ 1, 0, 0, 1, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 1, 0, 0, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 1, 0, 0, 0 },
//			{ 0, 1, 1, 1, 1, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
//
//	CanvasSurfaceView(Context context) {
//		super(context);
//		setBackgroundColor(WHITE);
//
//		// get display size
//		disp = ((WindowManager) context
//				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//		update_xy();
//
//		// ranking default setting
//		for (int i = 0; i < 5; i++) {
//			for (int j = 0; j < 5; j++) {
//				rank[i][j] = 0;
//				rank_line[i][j] = 0;
//			}
//		}
//
//		// default settings
//		set_bitmap();
//		set_sound(context);
//		set_obp();
//		set_answer();
//		set_comment();
//		set_initialize();
//		set_startstate();
//		movebg_set();
//
//		// initialize of previous time
//		prev_time = SystemClock.uptimeMillis();
//
//		// start thread activity
//		(new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// describe back ground activity here
//				while (true) {
//					long elapsed_time = 0;
//					// execute sleep time
//					while (elapsed_time < sleep_time) {
//						try {
//							// frame time
//							Thread.sleep(10);
//						} catch (InterruptedException e) {
//						}
//						// get millisecond after booting
//						now_time = SystemClock.uptimeMillis();
//						// update elapsed time
//						elapsed_time = now_time - prev_time;
//					}
//					// update previous time
//					prev_time = now_time;
//
//					mHandler.post(new Runnable() {
//						@Override
//						public void run() {
//							// reset delta
//							set_dx = 0;
//							set_dy = 0;
//							flag_procedure();
//
//							if (gameover_flag == 0
//									&& pause_count == 0
//									&& (menu_flag == PLAY
//											|| menu_flag == SPEASY
//											|| menu_flag == SPMEDIUM
//											|| menu_flag == SPHARD || menu_flag == SPHELL)
//									&& delete_flag == 0
//									&& start_count_flag == 0) {
//								if (count == 0) {
//									set_new();
//								} else {
//									set_moveblock();
//								}
//								// drop count increase
//								count++;
//
//								// reset arrays
//								for (int i = 0; i < WIDTH; i++) {
//									for (int j = 0; j < HEIGHT; j++) {
//										if (state[i][j] != STABLE) {
//											color[i][j] = NO;
//											state[i][j] = NO;
//											alphabet[i][j] = 0;
//										}
//									}
//								}
//
//								// shift position of a base block
//								set_x[0] += set_dx;
//								set_y[0] += set_dy;
//
//								// set other parts [1],[2],[3] around [0]
//								for (int i = 1; i < 4; i++) {
//									set_x[i] = set_x[0]
//											+ obpx[set_color][set_state][i];
//									set_y[i] = set_y[0]
//											+ obpy[set_color][set_state][i];
//								}
//
//								// set moving blocks to the main arrangement
//								for (int i = 0; i < 4; i++) {
//									color[set_x[i]][set_y[i]] = set_color;
//									alphabet[set_x[i]][set_y[i]] = set_alp[i];
//									state[set_x[i]][set_y[i]] = MOVE;
//								}
//							}
//
//							// touch ground or other blocks
//							if (count % time == time - 1
//									&& (set_y[0] == HEIGHT - 1
//											|| set_y[1] == HEIGHT - 1
//											|| set_y[2] == HEIGHT - 1
//											|| set_y[3] == HEIGHT - 1
//											|| state[set_x[3]][set_y[3] + 1] == STABLE
//											|| state[set_x[2]][set_y[2] + 1] == STABLE
//											|| state[set_x[1]][set_y[1] + 1] == STABLE || state[set_x[0]][set_y[0] + 1] == STABLE)) {
//								count = 0;
//								// change move block stable
//								for (int i = 0; i < 4; i++) {
//									state[set_x[i]][set_y[i]] = STABLE;
//								}
//
//								// delete flag rise in this process!
//								if (delete_flag == 0) {
//									del_line();
//								}
//								soundplay(2);
//							}
//
//							// delete timing
//							if (delete_count == delete_time) {
//								delete_flag = 0;
//								delete_count = 0;
//							}
//
//							// delete execute
//							if (line_num > 0 && delete_flag == 0) {
//								del_process();
//							}
//
//							// prevent comment when gets point
//							if (special == SP_NICO && delete_count == 0
//									&& delete_flag == 0) {
//								// comment_set();
//								comment_set_ver2();
//							}
//
//							// dp_display count decrease if > 0
//							if (dp_display_count >= 0) {
//								dp_display_count--;
//							} else {
//								levelup_flag = 0;
//							}
//
//							// special GREEK effect
//							if (special == SP_GREEK) {
//								string_set();
//							}
//
//							// add effect
//							if (menu_flag == SPEASY) {
//								arrow_set();
//							}
//
//							// update display
//							update_drawarray();
//							invalidate();
//
//							// reset deleted line number
//							if (delete_flag == 0) {
//								line_num = 0;
//							}
//
//							// game over and shift to best scores
//							if (gameover_count >= 1000) {
//								update_score();
//							}
//						}
//					});
//				}
//			}
//		})).start();
//	}
//
//	/***** help context method *****/
//	// set images
//	private void set_bitmap() {
//		bg[0] = (BitmapDrawable) getResources().getDrawable(R.drawable.sky);
//		bg[1] = (BitmapDrawable) getResources().getDrawable(R.drawable.sea);
//		bg[2] = (BitmapDrawable) getResources().getDrawable(R.drawable.way);
//		bg[3] = (BitmapDrawable) getResources().getDrawable(R.drawable.tea);
//		bg[4] = (BitmapDrawable) getResources().getDrawable(R.drawable.fire);
//		bg[5] = (BitmapDrawable) getResources().getDrawable(R.drawable.snow);
//		bg[6] = (BitmapDrawable) getResources().getDrawable(R.drawable.time);
//		bg[7] = (BitmapDrawable) getResources().getDrawable(R.drawable.sand);
//		bg[8] = (BitmapDrawable) getResources().getDrawable(R.drawable.tokyo);
//		bg[9] = (BitmapDrawable) getResources().getDrawable(R.drawable.sydny);
//		bg[10] = (BitmapDrawable) getResources().getDrawable(R.drawable.paris);
//		bg[11] = (BitmapDrawable) getResources().getDrawable(R.drawable.cairo);
//		bg[12] = (BitmapDrawable) getResources().getDrawable(R.drawable.forest);
//		bg[13] = (BitmapDrawable) getResources().getDrawable(R.drawable.live);
//		bg[14] = (BitmapDrawable) getResources().getDrawable(R.drawable.sunset);
//		bg[15] = (BitmapDrawable) getResources().getDrawable(R.drawable.dragon);
//
//		block[1] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.block1);
//		block[2] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.block_green);
//		block[3] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.block_orange);
//		block[4] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.block_blue);
//		block[5] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.block_yellow);
//		block[6] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.block_purple);
//		block[7] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.block_pink);
//		block[8] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.block_gray);
//		block[9] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.block_white);
//
//		Bitmap[0] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.button1);
//		Bitmap[1] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.button2);
//		Bitmap[2] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.button3);
//		Bitmap[3] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.ranking1);
//		Bitmap[4] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.ranking2);
//		Bitmap[5] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.ranking3);
//		Bitmap[6] = (BitmapDrawable) getResources()
//				.getDrawable(R.drawable.nico);
//		Bitmap[7] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.button6);
//		Bitmap[8] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.button5);
//		Bitmap[9] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.button4);
//		Bitmap[10] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.box1);
//		Bitmap[11] = (BitmapDrawable) getResources()
//				.getDrawable(R.drawable.top);
//		Bitmap[12] = (BitmapDrawable) getResources()
//				.getDrawable(R.drawable.set);
//		Bitmap[13] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.buttonr);
//		Bitmap[14] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.buttonl);
//		Bitmap[15] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.cont);
//		Bitmap[16] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.star);
//		Bitmap[17] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.soundon);
//		Bitmap[18] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.soundoff);
//		Bitmap[19] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.vibon);
//		Bitmap[20] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.viboff);
//		Bitmap[21] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.back);
//		Bitmap[22] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.next);
//		Bitmap[23] = (BitmapDrawable) getResources().getDrawable(
//				R.drawable.back2);
//
//		// bound setting
//		set_bound();
//	}
//
//	private void set_bound() {
//		for (int i = 0; i < 16; i++) {
//			bg[i].setBounds(0, 0, Width, Height);
//		}
//	}
//
//	// set sound
//	private void set_sound(Context context) {
//		si[0] = soundpool.load(context, R.raw.button, 1);
//		si[1] = soundpool.load(context, R.raw.cancel, 1);
//		si[2] = soundpool.load(context, R.raw.drop, 1);
//		si[3] = soundpool.load(context, R.raw.levelup, 1);
//		si[4] = soundpool.load(context, R.raw.menu, 1);
//		si[5] = soundpool.load(context, R.raw.minus, 1);
//		si[6] = soundpool.load(context, R.raw.ok, 1);
//		si[7] = soundpool.load(context, R.raw.plus, 1);
//	}
//
//	// objective position setting
//	private void set_obp() {
//		for (int i = 0; i < 8; i++) {
//			obpx[i][0][0] = 0;
//			obpy[i][0][0] = 0;
//		}
//		// Re
//		obpx[1][0][1] = -1;
//		obpy[1][0][1] = 0;
//		obpx[1][0][2] = 1;
//		obpy[1][0][2] = 0;
//		obpx[1][0][3] = 0;
//		obpy[1][0][3] = 1;
//		// G1
//		obpx[2][0][1] = -1;
//		obpy[2][0][1] = 0;
//		obpx[2][0][2] = 1;
//		obpy[2][0][2] = 0;
//		obpx[2][0][3] = -1;
//		obpy[2][0][3] = 1;
//		// G2
//		obpx[3][0][1] = 1;
//		obpy[3][0][1] = 0;
//		obpx[3][0][2] = -1;
//		obpy[3][0][2] = 0;
//		obpx[3][0][3] = 1;
//		obpy[3][0][3] = 1;
//		// B
//		obpx[4][0][1] = -1;
//		obpy[4][0][1] = 0;
//		obpx[4][0][2] = 0;
//		obpy[4][0][2] = 1;
//		obpx[4][0][3] = -1;
//		obpy[4][0][3] = 1;
//		// Y1
//		obpx[5][0][1] = -1;
//		obpy[5][0][1] = 0;
//		obpx[5][0][2] = 1;
//		obpy[5][0][2] = 1;
//		obpx[5][0][3] = 0;
//		obpy[5][0][3] = 1;
//		// Y2
//		obpx[6][0][1] = 1;
//		obpy[6][0][1] = 0;
//		obpx[6][0][2] = -1;
//		obpy[6][0][2] = 1;
//		obpx[6][0][3] = 0;
//		obpy[6][0][3] = 1;
//		// p
//		obpx[7][0][1] = -1;
//		obpy[7][0][1] = 0;
//		obpx[7][0][2] = 1;
//		obpy[7][0][2] = 0;
//		obpx[7][0][3] = 2;
//		obpy[7][0][3] = 0;
//		// set in each mode
//		for (int i = 1; i < 4; i++) {
//			for (int j = 1; j < 8; j++) {
//				for (int k = 0; k < 4; k++) {
//					obpx[j][i][k] = (int) Math.round(Math
//							.cos(i * Math.PI / 2.0)
//							* obpx[j][0][k]
//							- Math.sin(i * Math.PI / 2.0) * obpy[j][0][k]);
//					obpy[j][i][k] = (int) Math.round(Math
//							.sin(i * Math.PI / 2.0)
//							* obpx[j][0][k]
//							+ Math.cos(i * Math.PI / 2.0) * obpy[j][0][k]);
//				}
//			}
//		}
//	}
//
//	// set answer
//	private void set_answer() {
//		// sky
//		cand_ans_len[0] = 3;
//		cand_ans[0][0] = 18;
//		cand_ans[0][1] = 10;
//		cand_ans[0][2] = 24;
//		// sea
//		cand_ans_len[1] = 3;
//		cand_ans[1][0] = 18;
//		cand_ans[1][1] = 4;
//		cand_ans[1][2] = 0;
//		// way
//		cand_ans_len[2] = 3;
//		cand_ans[2][0] = 22;
//		cand_ans[2][1] = 0;
//		cand_ans[2][2] = 24;
//		// tea
//		cand_ans_len[3] = 3;
//		cand_ans[3][0] = 19;
//		cand_ans[3][1] = 4;
//		cand_ans[3][2] = 0;
//		// fire
//		cand_ans_len[4] = 4;
//		cand_ans[4][0] = 5;
//		cand_ans[4][1] = 8;
//		cand_ans[4][2] = 17;
//		cand_ans[4][3] = 4;
//		// snow
//		cand_ans_len[5] = 4;
//		cand_ans[5][0] = 18;
//		cand_ans[5][1] = 13;
//		cand_ans[5][2] = 14;
//		cand_ans[5][3] = 22;
//		// time
//		cand_ans_len[6] = 4;
//		cand_ans[6][0] = 19;
//		cand_ans[6][1] = 8;
//		cand_ans[6][2] = 12;
//		cand_ans[6][3] = 4;
//		// sand
//		cand_ans_len[7] = 4;
//		cand_ans[7][0] = 18;
//		cand_ans[7][1] = 0;
//		cand_ans[7][2] = 13;
//		cand_ans[7][3] = 3;
//		// TOKYO
//		cand_ans_len[8] = 5;
//		cand_ans[8][0] = 19;
//		cand_ans[8][1] = 14;
//		cand_ans[8][2] = 10;
//		cand_ans[8][3] = 24;
//		cand_ans[8][4] = 14;
//		// CYDNY
//		cand_ans_len[9] = 5;
//		cand_ans[9][0] = 18;
//		cand_ans[9][1] = 24;
//		cand_ans[9][2] = 3;
//		cand_ans[9][3] = 13;
//		cand_ans[9][4] = 24;
//		// PARIS
//		cand_ans_len[10] = 5;
//		cand_ans[10][0] = 15;
//		cand_ans[10][1] = 0;
//		cand_ans[10][2] = 17;
//		cand_ans[10][3] = 8;
//		cand_ans[10][4] = 18;
//		// CAIRO
//		cand_ans_len[11] = 5;
//		cand_ans[11][0] = 2;
//		cand_ans[11][1] = 0;
//		cand_ans[11][2] = 8;
//		cand_ans[11][3] = 17;
//		cand_ans[11][4] = 14;
//		// forest
//		cand_ans_len[12] = 6;
//		cand_ans[12][0] = 5;
//		cand_ans[12][1] = 14;
//		cand_ans[12][2] = 17;
//		cand_ans[12][3] = 4;
//		cand_ans[12][4] = 18;
//		cand_ans[12][5] = 19;
//		// guitar
//		cand_ans_len[13] = 6;
//		cand_ans[13][0] = 6;
//		cand_ans[13][1] = 20;
//		cand_ans[13][2] = 8;
//		cand_ans[13][3] = 19;
//		cand_ans[13][4] = 0;
//		cand_ans[13][5] = 17;
//		// sunset
//		cand_ans_len[14] = 6;
//		cand_ans[14][0] = 18;
//		cand_ans[14][1] = 20;
//		cand_ans[14][2] = 13;
//		cand_ans[14][3] = 18;
//		cand_ans[14][4] = 4;
//		cand_ans[14][5] = 19;
//		// dragon
//		cand_ans_len[15] = 6;
//		cand_ans[15][0] = 3;
//		cand_ans[15][1] = 17;
//		cand_ans[15][2] = 0;
//		cand_ans[15][3] = 6;
//		cand_ans[15][4] = 14;
//		cand_ans[15][5] = 13;
//	}
//
//	// set comment
//	private void set_comment() {
//		comment[0] = "wwwwwwwwww";
//		comment[1] = "m9(^Д^)";
//		comment[2] = "減点ワロタwww";
//		comment[3] = "減点とかwww";
//		comment[4] = "すげぇぇぇぇぇぇ!!!";
//		comment[5] = "天才か・・・・!?";
//		comment[6] = "おおおおおおおお～!!!";
//		comment[7] = "うますぎる・・・";
//		comment[8] = "おおお～!";
//		comment[9] = "上手い";
//		comment[10] = "4人がイイネ!と言っています";
//		comment[11] = "ギルティだな";
//		comment[12] = "おおw";
//		comment[13] = "いいねw";
//		comment[14] = "上手い・・・のか?";
//		comment[15] = "【審議中】( ´・ω)(´・ω・)(｀・ω・´)(・ω・｀)(ω・｀ )";
//		comment[16] = "しょぼ・・・";
//		comment[17] = "がっかりだわ・・・";
//		comment[18] = "wwwww";
//		comment[19] = "10点ってwww";
//		comment[20] = "ざわ・・・ざわ・・・";
//		comment[21] = "やばくね？";
//		comment[22] = "ここで終わるとかwww";
//		comment[23] = "ワンチャンある!!";
//		comment[24] = "あああああああああ";
//		comment[25] = "ヲワタ・・";
//		comment[26] = "【審議中】( ´・ω)(´・ω・)(｀・ω・´)(・ω・｀)(ω・｀ )";
//		comment[27] = "え・・・これワンチャンないなぁ・・・";
//		comment[28] = "おいまだか・・・？";
//		comment[29] = "いつまで休んでんだよww";
//		comment[30] = "早くしろ~";
//		comment[31] = "え・・・これワンチャンあるなぁ";
//	}
//
//	// initialize arrays
//	private void set_initialize() {
//		// initialization of matrix "area"
//		for (int i = 0; i < WIDTH; i++) {
//			for (int j = 0; j < HEIGHT; j++) {
//				color[i][j] = NO;
//				state[i][j] = NO;
//				alphabet[i][j] = 0;
//			}
//		}
//		// comment
//		for (int i = 0; i < CMT_ROW; i++) {
//			cmt_no[i][0] = -1;
//			cmt_no[i][1] = -1;
//		}
//		// special GREEK
//		for (int i = 0; i < 5; i++) {
//			string_no[i] = 0;
//			string_x[i] = 0;
//			string_y[i] = 0;
//			string_color[i] = 0;
//			string_count[i] = 0;
//		}
//		// add
//		for (int i = 0; i < 15; i++) {
//			arrow_no[i] = 0;
//			arrow_x[i] = 0;
//			arrow_y[i] = 0;
//			arrow_size[i] = 0;
//			arrow_color[i] = 0;
//			arrow_count[i] = 0;
//		}
//	}
//
//	// start state
//	private void set_startstate() {
//		// alphabet setting
//		ans_data = (int) (Math.random() * 4);
//		cand_alp_len = cand_ans_len[ans_data];
//		ans_len = cand_alp_len;
//
//		for (int i = 0; i < cand_alp_len; i++) {
//			cand_alp[i] = cand_ans[ans_data][i];
//			ans[i] = cand_ans[ans_data][i];
//		}
//
//		// set default dropping time
//		time = 18;
//		// set default next
//		set_next_color = 1 + (int) (Math.random() * 7.0);
//		for (int i = 0; i < 4; i++) {
//			set_next_alp[i] = cand_alp[(int) (cand_alp_len * Math.random())];
//		}
//	}
//
//	// flag procedures
//	private void flag_procedure() {
//		// pause count increase
//		if (pause != 0) {
//			if (pause_count < PAUSE_MAX) {
//				pause_count += PAUSE_SUB;
//			} else {
//				pause_count = PAUSE_MAX;
//			}
//		} else {
//			if (pause_count > PAUSE_MAX) {
//				pause_count = PAUSE_MAX;
//			} else if (pause_count > 0) {
//				pause_count -= PAUSE_SUB;
//			} else {
//				pause_count = 0;
//				if (menu_flag == PAUSE) {
//					menu_flag = prepause_menu_flag;
//				}
//			}
//		}
//
//		// game over count increase
//		if (gameover_flag != 0) {
//			if (gameover_count < 1000) {
//				gameover_count++;
//			} else {
//				gameover_count = 1000;
//			}
//		}
//
//		// delete count increase
//		if (delete_flag == 1) {
//			if (delete_count < delete_time) {
//				delete_count++;
//			} else {
//				delete_count = 40;
//			}
//		}
//
//		// time_count increase
//		if (ans_data == 6) {
//			if (time_count < 3600) {
//				time_count++;
//			} else {
//				time_count = 0;
//			}
//		}
//
//		// start count increase
//		if (start_count_flag == 1) {
//			if (start_count < 60) {
//				start_count++;
//			} else {
//				start_count_flag = 0;
//			}
//		}
//
//		// move background
//		if (menu_flag == MENU || menu_flag == SCORE || menu_flag == SETTING
//				|| menu_flag == SPMENU) {
//			switch (touch_state) {
//			case NO:
//				movebg_count++;
//				break;
//			case TOUCH_DOWN:
//				touch_state = NO;
//				break;
//			case TOUCH_MOVE:
//				movebg_count = temp_movebg_count
//						- (touch_pos[1][0] - touch_pos[0][0]) / 4;
//				break;
//			case TOUCH_UP:
//				movebg_count += touch_speed / MPF;
//				if (touch_speed > 0) {
//					touch_speed = (int) (touch_speed / REDUCE_RATE);
//					if (touch_speed < MPF) {
//						touch_state = NO;
//					}
//				} else {
//					touch_speed = (int) (touch_speed / REDUCE_RATE) + MPF;
//					if (touch_speed > MPF) {
//						touch_state = NO;
//					}
//				}
//				break;
//			}
//			if (movebg_count >= 0) {
//				movebg_count = movebg_count % (16 * Width / MPF);
//			} else {
//				movebg_count = (movebg_count + (16 * Width / MPF))
//						% (16 * Width / MPF);
//			}
//		}
//	}
//
//	// set new blocks
//	private void set_new() {
//		// color corresponds to block category ( 1 to 7 )
//		set_color = set_next_color;
//		set_next_color = 1 + (int) (Math.random() * 7.0);
//		set_state = 0;
//
//		// set center block start position
//		switch (set_color) {
//		case Re:
//			set_x[0] = 4;
//			set_y[0] = 0;
//			break;
//		case G1:
//			set_x[0] = 4;
//			set_y[0] = 0;
//			break;
//		case G2:
//			set_x[0] = 5;
//			set_y[0] = 0;
//			break;
//		case B:
//			set_x[0] = 5;
//			set_y[0] = 0;
//			break;
//		case Y1:
//			set_x[0] = 4;
//			set_y[0] = 0;
//			break;
//		case Y2:
//			set_x[0] = 5;
//			set_y[0] = 0;
//			break;
//		case P:
//			set_x[0] = 4;
//			set_y[0] = 0;
//			break;
//		}
//		// set other blocks start position
//		for (int i = 1; i < 4; i++) {
//			set_x[i] = set_x[0] + obpx[set_color][set_state][i];
//			set_y[i] = set_y[0] + obpy[set_color][set_state][i];
//		}
//		// check next can be set or not (game over)
//		for (int i = 0; i < 4; i++) {
//			if (state[set_x[i]][set_y[i]] == STABLE) {
//				gameover_flag = 1;
//				break;
//			}
//		}
//		// set alphabets
//		for (int i = 0; i < 4; i++) {
//			double prob = Math.random() * 100;
//			set_alp[i] = set_next_alp[i];
//			if (prob < ALP_PROB[0]) {
//				set_next_alp[i] = cand_alp[(int) (Math.random() * cand_alp_len)];
//			} else if (prob < ALP_PROB[1]) {
//				set_next_alp[i] = 26;
//			} else if (prob < ALP_PROB[2]) {
//				set_next_alp[i] = 27;
//			} else if (prob < ALP_PROB[3]) {
//				set_next_alp[i] = 28;
//			} else if (prob < ALP_PROB[3]) {
//				set_next_alp[i] = 29;
//			} else {
//				set_next_alp[i] = 30;
//			}
//		}
//	}
//
//	// control moving blocks
//	private void set_moveblock() {
//		// drop at every time
//		if (count % time == 0 && set_y[0] < HEIGHT - 1 && set_y[1] < HEIGHT - 1
//				&& set_y[2] < HEIGHT - 1 && set_y[3] < HEIGHT - 1
//				&& state[set_x[3]][set_y[3] + 1] != STABLE
//				&& state[set_x[2]][set_y[2] + 1] != STABLE
//				&& state[set_x[1]][set_y[1] + 1] != STABLE
//				&& state[set_x[0]][set_y[0] + 1] != STABLE) {
//			set_dy = 1;
//		}
//
//		// command
//		if (left == 1 && set_x[0] > 0 && set_x[1] > 0 && set_x[2] > 0
//				&& set_x[3] > 0 && state[set_x[3] - 1][set_y[3]] != STABLE
//				&& state[set_x[2] - 1][set_y[2]] != STABLE
//				&& state[set_x[1] - 1][set_y[1]] != STABLE
//				&& state[set_x[0] - 1][set_y[0]] != STABLE) {
//			set_dx = -1;
//		} else if (right == 1 && set_x[0] < WIDTH - 1 && set_x[1] < WIDTH - 1
//				&& set_x[2] < WIDTH - 1 && set_x[3] < WIDTH - 1
//				&& state[set_x[3] + 1][set_y[3]] != STABLE
//				&& state[set_x[2] + 1][set_y[2]] != STABLE
//				&& state[set_x[1] + 1][set_y[1]] != STABLE
//				&& state[set_x[0] + 1][set_y[0]] != STABLE) {
//			set_dx = 1;
//		} else if (down == 1 && set_y[0] < HEIGHT - 1 && set_y[1] < HEIGHT - 1
//				&& set_y[2] < HEIGHT - 1 && set_y[3] < HEIGHT - 1
//				&& state[set_x[3]][set_y[3] + 1] != STABLE
//				&& state[set_x[2]][set_y[2] + 1] != STABLE
//				&& state[set_x[1]][set_y[1] + 1] != STABLE
//				&& state[set_x[0]][set_y[0] + 1] != STABLE) {
//			set_dy = 1;
//		} else if (rotate == 1) {
//			int next_state = 0;
//			int slide[] = { 0, -1, 1 };
//			if (rotate_direc == ROTATE_RIGHT) {
//				next_state = (set_state + 1) % 4;
//			} else if (rotate_direc == ROTATE_LEFT) {
//				next_state = (set_state + 3) % 4;
//			}
//			for (int h = 0; h < 3; h++) {
//				for (int i = 0; i < 4; i++) {
//					int next_x = set_x[0] + obpx[set_color][next_state][i]
//							+ slide[h];
//					int next_y = set_y[0] + obpy[set_color][next_state][i];
//					if (set_color == B) {
//						set_state = next_state;
//						switch (set_state) {
//						case 0:
//							if (rotate_direc == ROTATE_RIGHT) {
//								set_dx = 1;
//							} else if (rotate_direc == ROTATE_LEFT) {
//								set_dy = -1;
//							}
//							break;
//						case 1:
//							if (rotate_direc == ROTATE_RIGHT) {
//								set_dy = 1;
//							} else if (rotate_direc == ROTATE_LEFT) {
//								set_dx = 1;
//							}
//							break;
//						case 2:
//							if (rotate_direc == ROTATE_RIGHT) {
//								set_dx = -1;
//							} else if (rotate_direc == ROTATE_LEFT) {
//								set_dy = 1;
//							}
//							break;
//						case 3:
//							if (rotate_direc == ROTATE_RIGHT) {
//								set_dy = -1;
//							} else if (rotate_direc == ROTATE_LEFT) {
//								set_dx = -1;
//							}
//							break;
//						}
//						break;
//					} else if (next_x >= 0 && next_x <= WIDTH - 1
//							&& next_y >= 0 && next_y <= HEIGHT - 1
//							&& state[next_x][next_y] != STABLE
//							&& set_color != B) {
//						if (i == 3) {
//							set_state = next_state;
//							set_dx = slide[h];
//						}
//					} else {
//						break;
//					}
//				}
//				if (set_state == next_state) {
//					break;
//				}
//			}
//		} else if (double_down == 1) {
//			// go to expected drop point
//			for (int i = 0; i < HEIGHT - 1; i++) {
//				if (set_y[3] + i == HEIGHT - 1 || set_y[1] + i == HEIGHT - 1
//						|| set_y[2] + i == HEIGHT - 1
//						|| set_y[0] + i == HEIGHT - 1
//						|| state[set_x[3]][set_y[3] + i + 1] == STABLE
//						|| state[set_x[1]][set_y[1] + i + 1] == STABLE
//						|| state[set_x[2]][set_y[2] + i + 1] == STABLE
//						|| state[set_x[0]][set_y[0] + i + 1] == STABLE) {
//					set_dy = i;
//					count = 5;
//					break;
//				}
//			}
//		}
//
//		// reset flags
//		right = 0;
//		left = 0;
//		down = 0;
//		rotate = 0;
//		double_down = 0;
//	}
//
//	/***** update method *****/
//	// update top 5 and reset variables
//	private void update_score() {
//		int temp1 = 0, temp2 = 0;
//		int temp_line1 = 0, temp_line2 = 0;
//		switch (menu_flag) {
//		case PLAY:
//			score_menu = SCORE_PLAY;
//			break;
//		case SPEASY:
//			score_menu = SCORE_SPEASY;
//			break;
//		case SPMEDIUM:
//			score_menu = SCORE_SPMEDIUM;
//			break;
//		case SPHARD:
//			score_menu = SCORE_SPHARD;
//			break;
//		case SPHELL:
//			score_menu = SCORE_SPHELL;
//			break;
//		default:
//			score_menu = -1;
//			break;
//		}
//
//		for (int i = 0; i < 5; i++) {
//			if (point > rank[score_menu][i]) {
//				temp2 = rank[score_menu][i];
//				temp_line2 = rank_line[score_menu][i];
//				for (int j = i + 1; j < 5; j++) {
//					temp1 = rank[score_menu][j];
//					temp_line1 = rank_line[score_menu][j];
//					rank[score_menu][j] = temp2;
//					rank_line[score_menu][j] = temp_line2;
//					temp2 = temp1;
//					temp_line2 = temp_line1;
//				}
//				rank[score_menu][i] = point;
//				rank_line[score_menu][i] = deleted_lines;
//				break;
//			}
//		}
//		// write to text file
//		((Wordis) getContext()).write();
//		update_level();
//		initialize();
//
//		// go to high score display
//		menu_flag = SCORE;
//	}
//
//	// update special open level
//	private void update_level() {
//		if (rank[0][0] >= 3600) {
//			open_level = 4;
//		} else if (rank[0][0] >= 2400) {
//			open_level = 3;
//		} else if (rank[0][0] >= 1200) {
//			open_level = 2;
//		}
//		if (rank[1][0] >= 3600) {
//			open_level = 5;
//		} else if (rank[1][0] >= 1800) {
//			open_level = 4;
//		}
//		if (rank[2][0] >= 3600) {
//			open_level = 6;
//		} else if (rank[2][0] >= 1800) {
//			open_level = 5;
//		}
//		if (rank[3][0] >= 1800) {
//			open_level = 6;
//		}
//	}
//
//	// update position of pasting images and texts
//	private void update_xy() {
//		// get display size
//		Width = disp.getWidth();
//		Height = disp.getHeight();
//
//		for (int i = 0; i < TEXT_MATRIX_ROW; i++) {
//			for (int j = 0; j < TEXT_MATRIX_COLUMN; j++) {
//				Text_X[i][j] = (int) (RATE_TEXT_X[i][j] * (double) (Width / BASE_WIDTH));
//				Text_Y[i][j] = (int) (RATE_TEXT_Y[i][j] * (double) (Height / BASE_HEIGHT));
//			}
//		}
//
//		for (int i = 0; i < BITMAP_MATRIX_ROW; i++) {
//			for (int j = 0; j < BITMAP_MATRIX_COLUMN; j++) {
//				Bitmap_Pos[i][j][0] = (int) (RATE_BITMAP_POS[i][j][0] * (double) (Width / BASE_WIDTH));
//				Bitmap_Pos[i][j][2] = (int) (RATE_BITMAP_POS[i][j][2] * (double) (Width / BASE_WIDTH));
//				Bitmap_Pos[i][j][1] = (int) (RATE_BITMAP_POS[i][j][1] * (double) (Height / BASE_HEIGHT));
//				Bitmap_Pos[i][j][3] = (int) (RATE_BITMAP_POS[i][j][3] * (double) (Height / BASE_HEIGHT));
//			}
//		}
//	}
//
//	// update arrays of images and text at every time of drawing
//	// correct this function to change position or size of text and bitmap.
//	// contain PSEUDO CONSTANTS
//	private void update_drawarray() {
//		switch (menu_flag) {
//		case MENU:
//			update_menuarray();
//			break;
//		case PLAY:
//			update_playarray();
//			break;
//		case PAUSE:
//			update_pausearray();
//			break;
//		case SCORE:
//			update_scorearray();
//			break;
//		case SETTING:
//			update_settingarray();
//			break;
//		case SPMENU:
//			update_spmenuarray();
//			break;
//		case SPEASY:
//			update_playarray();
//			break;
//		case SPMEDIUM:
//			update_playarray();
//			break;
//		case SPHARD:
//			update_playarray();
//			break;
//		case SPHELL:
//			update_playarray();
//			break;
//		}
//	}
//
//	private void update_menuarray() {
//		// special open condition
//		if (open_level > 3) {
//			Text[menu_flag][5] = "special";
//			Text_X[menu_flag][5] = 175;
//			Text_X[menu_flag][5] = (int) (Text_X[menu_flag][5] * (double) (Width / BASE_WIDTH));
//			Text_Y[menu_flag][5] = 660;
//			Text_Y[menu_flag][5] = (int) (Text_Y[menu_flag][5] * (double) (Height / BASE_HEIGHT));
//			BITMAP_NO[menu_flag][3] = 9;
//		}
//
//		if (open_level >= 5) {
//			BITMAP_NO[menu_flag][4] = 16;
//			BITMAP_NO[menu_flag][5] = 16;
//			BITMAP_NO[menu_flag][6] = 16;
//		} else if (open_level >= 4) {
//			BITMAP_NO[menu_flag][4] = 16;
//			BITMAP_NO[menu_flag][5] = 16;
//			BITMAP_NO[menu_flag][6] = -1;
//		} else if (open_level >= 3) {
//			BITMAP_NO[menu_flag][4] = 16;
//			BITMAP_NO[menu_flag][5] = -1;
//			BITMAP_NO[menu_flag][6] = -1;
//		} else {
//			BITMAP_NO[menu_flag][4] = -1;
//			BITMAP_NO[menu_flag][5] = -1;
//			BITMAP_NO[menu_flag][6] = -1;
//		}
//	}
//
//	private void update_playarray() {
//		// control mode
//		if (mode == KEYCONTROL) {
//			if (menu_flag == SPEASY) {
//				Text[menu_flag][0] = "→";
//				Text[menu_flag][1] = "←";
//				Text[menu_flag][2] = "↑";
//				Text[menu_flag][3] = "↑↑";
//			} else {
//				Text[menu_flag][0] = "←";
//				Text[menu_flag][1] = "→";
//				Text[menu_flag][2] = "↓";
//				Text[menu_flag][3] = "↓↓";
//			}
//			if (rotate_direc == ROTATE_RIGHT) {
//				BITMAP_NO[menu_flag][5] = 13;
//			} else if (rotate_direc == ROTATE_LEFT) {
//				BITMAP_NO[menu_flag][5] = 14;
//			}
//		} else {
//			Text[menu_flag][0] = "";
//			Text[menu_flag][1] = "";
//			Text[menu_flag][2] = "";
//			Text[menu_flag][3] = "";
//			BITMAP_NO[menu_flag][5] = 2;
//		}
//		Text[menu_flag][8] = "" + level;
//		Text[menu_flag][10] = "" + point;
//		Text[menu_flag][12] = "" + deleted_lines;
//		// answer
//		for (int i = 0; i < 6; i++) {
//			if (special == SP_GREEK && i < ans_len) {
//				Text[menu_flag][15 + i] = GREEK[ans[i]];
//			} else if (i < ans_len) {
//				Text[menu_flag][15 + i] = ALPHABET[ans[i]];
//			} else {
//				Text[menu_flag][15 + i] = "";
//			}
//		}
//		Bitmap_Pos[menu_flag][6][2] = RATE_BITMAP_POS[menu_flag][6][2] + 25
//				* ans_len;
//		// get point and level up
//		if (dp_display_count >= 0) {
//			String sign;
//			// set sign of display point get
//			if (d_point > 0) {
//				sign = "+";
//			} else {
//				sign = "";
//			}
//			Text[menu_flag][13] = sign + d_point;
//			RATE_TEXT_COLOR[menu_flag][13] = Color.argb(20 * dp_display_count,
//					255, 0, 0);
//			// level up
//			if (levelup_flag != 0) {
//				if (special == SP_NICO) {
//					Text[menu_flag][14] = "↑（´∀｀∩）level うp~↑";
//					Text_X[menu_flag][14] = 225;
//				} else {
//					Text[menu_flag][14] = "↑level up!↑";
//					Text_X[menu_flag][14] = 315;
//				}
//				Text_Y[menu_flag][14] = 545 + dp_display_count * 2;
//				RATE_TEXT_COLOR[menu_flag][14] = Color.argb(
//						20 * dp_display_count, 255, 0, 0);
//			} else {
//				Text[menu_flag][14] = "";
//			}
//		} else {
//			Text[menu_flag][13] = "";
//			Text[menu_flag][14] = "";
//		}
//	}
//
//	private void update_pausearray() {
//		// resume the game
//		if (pause_count > PAUSE_LINE[0]) {
//			Text[2][0] = "Resume Game";
//			BITMAP_NO[2][0] = 21;
//		} else {
//			Text[2][0] = "";
//			BITMAP_NO[2][0] = -1;
//		}
//		// return to top
//		if (pause_count > PAUSE_LINE[1]) {
//			Text[2][1] = "Return To Top";
//			BITMAP_NO[2][1] = 11;
//		} else {
//			Text[2][1] = "";
//			BITMAP_NO[2][1] = -1;
//		}
//		// control mode
//		if (pause_count > PAUSE_LINE[2]) {
//			if (mode == KEYCONTROL) {
//				Text[2][2] = "Key Control";
//				RATE_TEXT_COLOR[2][2] = COLOR[B];
//			} else {
//				Text[2][2] = "Touch Control";
//				RATE_TEXT_COLOR[2][2] = COLOR[Re];
//			}
//			BITMAP_NO[2][2] = 15;
//		} else {
//			Text[2][2] = "";
//			BITMAP_NO[2][2] = -1;
//		}
//		// special mode
//		if (pause_count > PAUSE_LINE[3]) {
//			switch (special) {
//			case NORMAL:
//				if (open_level == 1) {
//					Text[2][3] = "??????";
//					Text_X[2][3] = 180;
//					RATE_TEXT_SIZE[2][3] = 40;
//				} else {
//					Text[2][3] = "NORMAL";
//					Text_X[2][3] = 170;
//					RATE_TEXT_SIZE[2][3] = 40;
//				}
//				break;
//			case SP_NICO:
//				Text[2][3] = "(原宿)";
//				Text_X[2][3] = 160;
//				RATE_TEXT_SIZE[2][3] = 45;
//				break;
//			case SP_GREEK:
//				Text[2][3] = "GREEK";
//				Text_X[2][3] = 180;
//				RATE_TEXT_SIZE[2][3] = 45;
//				break;
//			case SP_3:
//				Text[2][3] = "special3";
//				Text_X[2][3] = 170;
//				RATE_TEXT_SIZE[2][3] = 45;
//				break;
//			case SP_4:
//				Text[2][3] = "special4";
//				Text_X[2][3] = 170;
//				RATE_TEXT_SIZE[2][3] = 45;
//				break;
//			case SP_5:
//				Text[2][3] = "special5";
//				Text_X[2][3] = 170;
//				RATE_TEXT_SIZE[2][3] = 45;
//				break;
//			}
//			BITMAP_NO[2][3] = 16;
//			// NICO bitmap graphic
//			if (special == SP_NICO) {
//				BITMAP_NO[2][6] = 6;
//			} else {
//				BITMAP_NO[2][6] = -1;
//			}
//		} else {
//			BITMAP_NO[2][3] = -1;
//			BITMAP_NO[2][6] = -1;
//			Text[2][3] = "";
//		}
//		// settings
//		if (pause_count > PAUSE_LINE[4]) {
//			Text[2][4] = "Settings";
//			BITMAP_NO[menu_flag][4] = 12;
//		} else {
//			Text[2][4] = "";
//			BITMAP_NO[menu_flag][4] = -1;
//		}
//		// rotate direction
//		if (pause_count > PAUSE_LINE[5]) {
//			if (rotate_direc == ROTATE_RIGHT) {
//				Text[2][5] = "Rotate Right";
//				RATE_TEXT_COLOR[2][5] = COLOR[G1];
//				BITMAP_NO[menu_flag][5] = 13;
//			} else if (rotate_direc == ROTATE_LEFT) {
//				Text[2][5] = "Rotate Left";
//				RATE_TEXT_COLOR[2][5] = COLOR[G2];
//				BITMAP_NO[menu_flag][5] = 14;
//			}
//		} else {
//			Text[2][5] = "";
//			BITMAP_NO[menu_flag][5] = -1;
//		}
//	}
//
//	private void update_scorearray() {
//		// rank
//		int set_length_rank = 0;
//		int set_length_line = 0;
//		for (int i = 0; i < 5; i++) {
//			set_length_rank = 0;
//			set_length_line = 0;
//			for (int j = 7; j > 0; j--) {
//				if (Math.abs(rank[score_menu][i]) >= Math.pow(10, j - 1)
//						&& set_length_rank == 0) {
//					set_length_rank = j;
//				}
//				if (Math.abs(rank_line[score_menu][i]) >= Math.pow(10, j - 1)
//						&& set_length_line == 0) {
//					set_length_line = j;
//				}
//			}
//			Text[3][3 + 3 * i] = "" + rank[score_menu][i];
//			Text_X[3][3 + 3 * i] = 270 - 20 * set_length_rank;
//			Text[3][5 + 3 * i] = rank_line[score_menu][i] + " lines";
//			Text_X[3][5 + 3 * i] = 280 - 20 * set_length_line;
//		}
//		// top strings
//		switch (score_menu) {
//		case SCORE_PLAY:
//			Text[menu_flag][2] = "SCORE TOP 5";
//			break;
//		case SCORE_SPEASY:
//			Text[menu_flag][2] = "         EASY";
//			break;
//		case SCORE_SPMEDIUM:
//			Text[menu_flag][2] = "      MEDIUM";
//			break;
//		case SCORE_SPHARD:
//			Text[menu_flag][2] = "         HARD";
//			break;
//		case SCORE_SPHELL:
//			Text[menu_flag][2] = "         HELL";
//			break;
//		}
//		// next and back BITMAP
//		if (open_level >= score_menu + 3) {
//			BITMAP_NO[menu_flag][4] = 22;
//		} else {
//			BITMAP_NO[menu_flag][4] = -1;
//		}
//		if (score_menu > SCORE_PLAY) {
//			BITMAP_NO[menu_flag][3] = 23;
//		} else {
//			BITMAP_NO[menu_flag][3] = -1;
//		}
//	}
//
//	private void update_settingarray() {
//		// sound
//		if (sound_flag == 0) {
//			Text[4][0] = "Sound Off";
//			RATE_TEXT_COLOR[4][0] = LGREY;
//			BITMAP_NO[menu_flag][0] = 18;
//		} else {
//			Text[4][0] = "Sound On";
//			RATE_TEXT_COLOR[4][0] = BLACK;
//			BITMAP_NO[menu_flag][0] = 17;
//		}
//		// vibration
//		if (vib_flag == 0) {
//			Text[4][1] = "Vibration Off";
//			RATE_TEXT_COLOR[4][1] = LGREY;
//			BITMAP_NO[menu_flag][1] = 20;
//		} else {
//			Text[4][1] = "Vibration On";
//			RATE_TEXT_COLOR[4][1] = BLACK;
//			BITMAP_NO[menu_flag][1] = 19;
//		}
//		// return to top
//		if (presetting_menu_flag == PAUSE) {
//			Text[4][2] = "Return To Pause";
//			Text_X[4][2] = 120;
//			BITMAP_NO[4][2] = 21;
//		} else {
//			Text[4][2] = "Return To Top";
//			Text_X[4][2] = 130;
//			BITMAP_NO[4][2] = 11;
//		}
//		// volume
//		Text[4][3] = "volume : " + (int) (100 * volume) + "/" + 100;
//	}
//
//	private void update_spmenuarray() {
//		if (open_level >= 6) {
//			Text[menu_flag][3] = "HELL";
//			RATE_TEXT_COLOR[menu_flag][3] = COLOR[Re];
//		} else {
//			Text[menu_flag][3] = "??????";
//			RATE_TEXT_COLOR[menu_flag][3] = BLACK;
//		}
//		if (open_level >= 5) {
//			Text[menu_flag][2] = "HARD";
//			RATE_TEXT_COLOR[menu_flag][2] = COLOR[B];
//		} else {
//			Text[menu_flag][2] = "??????";
//			RATE_TEXT_COLOR[menu_flag][2] = BLACK;
//		}
//		if (open_level >= 4) {
//			Text[menu_flag][1] = "MEDIUM";
//			RATE_TEXT_COLOR[menu_flag][1] = COLOR[G1];
//		} else {
//			Text[menu_flag][1] = "??????";
//			RATE_TEXT_COLOR[menu_flag][1] = BLACK;
//		}
//		if (open_level >= 3) {
//			Text[menu_flag][0] = "EASY";
//			RATE_TEXT_COLOR[menu_flag][0] = COLOR[P];
//		} else {
//			Text[menu_flag][0] = "??????";
//			RATE_TEXT_COLOR[menu_flag][0] = BLACK;
//		}
//	}
//
//	/***** touch methods *****/
//	// main touch method
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		int action = event.getAction();
//		int x = (int) event.getX();
//		int y = (int) event.getY();
//
//		// read a rank file at first time
//		if (read_executed_flag == 0) {
//			((Wordis) getContext()).read();
//			update_level();
//			((Wordis) getContext()).write();
//			// set flag not to read more times
//			read_executed_flag = 1;
//			// Toast.makeText((Wordis)getContext(),
//			// "pushed start button: "+rank[0]+","+rank_line[0]+":"+rank[1]+","+rank_line[1],
//			// Toast.LENGTH_SHORT).show();
//		}
//
//		// switch mode
//		switch (menu_flag) {
//		case MENU:
//			menu_touch(x, y, action);
//			break;
//		case PLAY:
//			if (gameover_flag == 1 && gameover_count >= 50) {
//				gameover_touch(x, y, action);
//			} else if (start_count_flag == 1) {
//				// touch disabled
//			} else if (mode == KEYCONTROL && pause_count == 0) {
//				key_action(x, y, action);
//			} else if (pause_count == 0) {
//				touch_action(x, y, action);
//			}
//			break;
//		case PAUSE:
//			if (reset_flag == 1) {
//				// touch disabled
//			} else {
//				pause_touch(x, y, action);
//			}
//			break;
//		case SCORE:
//			scores_touch(x, y, action);
//			break;
//		case SETTING:
//			setting_touch(x, y, action);
//			break;
//		case SPMENU:
//			spmenu_touch(x, y, action);
//			break;
//		default:
//			if (gameover_flag == 1 && gameover_count >= 50) {
//				gameover_touch(x, y, action);
//			} else if (start_count_flag == 1) {
//				// touch disabled
//			} else if (mode == KEYCONTROL && pause_count == 0) {
//				key_action(x, y, action);
//			} else if (pause_count == 0) {
//				touch_action(x, y, action);
//			}
//			break;
//		}
//
//		// reset direction flags while pause
//		if (menu_flag == PAUSE) {
//			right = 0;
//			left = 0;
//			down = 0;
//			rotate = 0;
//			double_down = 0;
//		}
//
//		return true;
//	}
//
//	// menu touch action
//	private void menu_touch(int x, int y, int action) {
//		if (action == MotionEvent.ACTION_UP) {
//			for (int i = 0; i < 4; i++) {
//				if (x >= Bitmap_Pos[0][i][0] && x <= Bitmap_Pos[0][i][2]
//						&& y >= Bitmap_Pos[0][i][1] && y <= Bitmap_Pos[0][i][3]) {
//					switch (i) {
//					case 0:
//						menu_flag = PLAY;
//						start_count_flag = 1;
//						set_bound();
//						touch_state = NO;
//						break;
//					case 1:
//						menu_flag = SCORE;
//						touch_state = NO;
//						break;
//					case 2:
//						presetting_menu_flag = menu_flag;
//						menu_flag = SETTING;
//						touch_state = NO;
//						break;
//					case 3:
//						if (open_level >= 2) {
//							menu_flag = SPMENU;
//							delete_time = 20;
//							touch_state = NO;
//						}
//						break;
//					}
//					soundplay(6);
//				} else if (i == 3 && touch_state == TOUCH_MOVE
//						&& count_downtoup < Math.abs(x - touch_pos[0][0]) / 10
//						&& Math.abs(x - touch_pos[0][0]) > TOUCH_BOUND
//						&& Math.abs(y - touch_pos[0][1]) < TOUCH_BOUND) {
//					touch_state = TOUCH_UP;
//					touch_speed = -(x - touch_pos[0][0]);
//				}
//			}
//		} else if (action == MotionEvent.ACTION_DOWN) {
//			touch_pos[0][0] = x;
//			touch_pos[0][1] = y;
//			touch_pos[1][0] = x;
//			touch_pos[1][1] = y;
//			temp_movebg_count = movebg_count;
//			count_downtoup = 0;
//			touch_state = TOUCH_DOWN;
//		} else if (action == MotionEvent.ACTION_MOVE) {
//			touch_pos[1][0] = x;
//			touch_pos[1][1] = y;
//			touch_state = TOUCH_MOVE;
//			count_downtoup++;
//		} else {
//			touch_state = NO;
//		}
//
//	}
//
//	// game over touch action
//	private void gameover_touch(int x, int y, int action) {
//		if (action == MotionEvent.ACTION_UP) {
//			update_score();
//			movebg_set();
//		}
//	}
//
//	// pause touch action
//	private void pause_touch(int x, int y, int action) {
//		if (action == MotionEvent.ACTION_UP) {
//			for (int i = 0; i < PAUSE_LINE_NUM; i++) {
//				if (x > BOX_POSX[0] && x < BOX_POSX[1] && y > PAUSE_LINE[i]
//						&& y < PAUSE_LINE[i + 1] && pause_count > PAUSE_LINE[i]) {
//					switch (i) {
//					case 0: // resume game
//						pause = 0;
//						break;
//					case 1: // return to top
//						reset_flag = 1;
//						((Wordis) getContext()).alert_show();
//						break;
//					case 2: // control change
//						mode = (mode + 1) % 2;
//						break;
//					case 3: // special mode change
//						special = (special + 1) % open_level;
//						if (special == SP_NICO || special == SP_GREEK) {
//							delete_time = 20;
//						} else {
//							delete_time = 10;
//						}
//						break;
//					case 4: // setting
//						presetting_menu_flag = menu_flag;
//						menu_flag = SETTING;
//						break;
//					case 5: // rotate direction
//						rotate_direc = (rotate_direc + 1) % 2;
//						break;
//					}
//					// play sound
//					soundplay(6);
//				}
//			}
//		}
//	}
//
//	// mode key
//	private void key_action(int x, int y, int action) {
//		if (action == MotionEvent.ACTION_DOWN) {
//			for (int i = 0; i < BITMAP_MATRIX_COLUMN; i++) {
//				if (x >= Bitmap_Pos[menu_flag][i][0]
//						&& y >= Bitmap_Pos[menu_flag][i][1]
//						&& x <= Bitmap_Pos[menu_flag][i][2]
//						&& y <= Bitmap_Pos[menu_flag][i][3]) {
//					// rise flag
//					switch (i) {
//					case 0:
//						left = 1;
//						break;
//					case 1:
//						right = 1;
//						break;
//					case 2:
//						down = 1;
//						break;
//					case 3:
//						double_down = 1;
//						break;
//					case 4:
//						prepause_menu_flag = menu_flag;
//						menu_flag = PAUSE;
//						pause = 1;
//						break;
//					case 5:
//						rotate = 1;
//						break;
//					}
//					// play sound
//					soundplay(0);
//				}
//			}
//		}
//	}
//
//	// mode touch
//	private void touch_action(int x, int y, int action) {
//		// set minimum x , maximum x, minimum y and maximum y of moving blocks
//		int minx = 1000, maxx = 0, miny = 1000, maxy = 0;
//
//		for (int i = 0; i < 4; i++) {
//			if (20 + LENGTH * set_x[i] < minx) {
//				minx = 20 + LENGTH * set_x[i];
//			}
//			if (20 + LENGTH * (set_x[i] + 1) > maxx) {
//				maxx = 20 + LENGTH * (set_x[i] + 1);
//			}
//			if (20 + LENGTH * set_y[i] < miny) {
//				miny = 20 + LENGTH * set_y[i];
//			}
//			if (20 + LENGTH * (set_y[i] + 1) > maxy) {
//				maxy = 20 + LENGTH * (set_y[i] + 1);
//			}
//		}
//
//		if (action == MotionEvent.ACTION_MOVE) {
//			// x direction move
//			if ((y > miny - LENGTH * 0.5) && (y < maxy + LENGTH * 0.5)) {
//				if (x < minx - 15) {
//					left = 1;
//					soundplay(0);
//				} else if (x > maxx + LENGTH * 0.5) {
//					right = 1;
//					soundplay(0);
//				}
//
//				// y direction move
//			} else if ((x > minx - LENGTH * 0.5) && (x < maxx - LENGTH * 0.5)) {
//				if (y >= maxy + 15) {
//					down = 1;
//					soundplay(0);
//				}
//			}
//		} else if (action == MotionEvent.ACTION_UP) {
//			if ((x > minx - LENGTH * 0.5) && (x < maxx + LENGTH * 0.5)
//					&& (y > miny - LENGTH * 0.5) && (y < maxy + LENGTH * 0.5)) {
//				rotate = 1;
//				soundplay(0);
//			}
//		} else if (action == MotionEvent.ACTION_DOWN) {
//			if (x >= Bitmap_Pos[menu_flag][4][0]
//					&& y >= Bitmap_Pos[menu_flag][4][1]
//					&& x <= Bitmap_Pos[menu_flag][4][2]
//					&& y <= Bitmap_Pos[menu_flag][4][3]) {
//				prepause_menu_flag = menu_flag;
//				menu_flag = PAUSE;
//				pause = 1;
//				// play sound
//				soundplay(4);
//			}
//		}
//	}
//
//	// scores display touch action
//	private void scores_touch(int x, int y, int action) {
//		if (action == MotionEvent.ACTION_UP) {
//			for (int i = 3; i < 6; i++) {
//				if (x > Bitmap_Pos[menu_flag][i][0]
//						&& x < Bitmap_Pos[menu_flag][i][2]
//						&& y > Bitmap_Pos[menu_flag][i][1]
//						&& y < Bitmap_Pos[menu_flag][i][3]) {
//					switch (i) {
//					case 3:
//						if (score_menu > SCORE_PLAY) {
//							score_menu--;
//						}
//						break;
//					case 4:
//						if (open_level >= score_menu + 3) {
//							score_menu++;
//						}
//						break;
//					case 5:
//						menu_flag = MENU;
//						break;
//					}
//					soundplay(4);
//				}
//			}
//		}
//	}
//
//	// setting display touch action
//	private void setting_touch(int x, int y, int action) {
//		if (action == MotionEvent.ACTION_UP) {
//			for (int i = 0; i < SETTING_LINE_NUM; i++) {
//				if (x > BOX_POSX[0] && x < BOX_POSX[1] && y > PAUSE_LINE[i]
//						&& y < PAUSE_LINE[i + 1]) {
//					switch (i) {
//					case 0:
//						sound_flag = (sound_flag + 1) % 2;
//						break;
//					case 2:
//						vib_flag = (vib_flag + 1) % 2;
//						((Wordis) getContext()).vibration(VIB_SHORT);
//						break;
//					case 3:
//						menu_flag = presetting_menu_flag;
//						if (menu_flag == PAUSE) {
//							set_bound();
//						}
//						break;
//					default:
//						break;
//					}
//					soundplay(4);
//				}
//			}
//		} else if (action == MotionEvent.ACTION_MOVE) {
//			if (x > 50 && x < 430 && y > 220 && y < 280) {
//				volume = (float) (x - 50) / 380;
//			}
//		}
//	}
//
//	// pause touch action
//	private void spmenu_touch(int x, int y, int action) {
//		if (action == MotionEvent.ACTION_UP) {
//			for (int i = 0; i < SPMENU_LINE_NUM; i++) {
//				if (x > BOX_POSX[0] && x < BOX_POSX[1] && y > PAUSE_LINE[i]
//						&& y < PAUSE_LINE[i + 1]) {
//					switch (i) {
//					case 0: // special mode easy
//						if (open_level >= 3) {
//							menu_flag = SPEASY;
//							start_count_flag = 1;
//							set_bound();
//						}
//						break;
//					case 1: // special mode middle
//						if (open_level >= 4) {
//							menu_flag = SPMEDIUM;
//							start_count_flag = 1;
//							set_medium();
//							set_bound();
//						}
//						break;
//					case 2: // special mode hard
//						if (open_level >= 5) {
//							menu_flag = SPHARD;
//							start_count_flag = 1;
//							set_hard();
//							set_bound();
//						}
//						break;
//					case 3: // special mode hell
//						if (open_level >= 6) {
//							menu_flag = SPHELL;
//							start_count_flag = 1;
//							// define order of hell mode
//							int temparray[] = { 0, 1, 2, 3 };
//							for (int j = 0; j < 20; j++) {
//								int no1 = (int) (Math.random() * 4);
//								int no2 = (int) (Math.random() * 4);
//								int temp = temparray[no1];
//								temparray[no1] = temparray[no2];
//								temparray[no2] = temp;
//							}
//							hell_order = temparray;
//							set_hell(hell_order[0]);
//							set_bound();
//						}
//						break;
//					case 4: // return to top
//						menu_flag = MENU;
//						break;
//					}
//					// play sound
//					soundplay(6);
//				}
//			}
//		}
//	}
//
//	// reset touch action :: NOT IN USE
//	private void reset_touch(int x, int y, int action) {
//		if (action == MotionEvent.ACTION_UP) {
//			// yes
//			if (x > 70 && x < 230 && y > 350 && y < 440) {
//				initialize();
//				soundplay(6);
//
//				// no
//			} else if (x > 250 && x < 410 && y > 350 && y < 440) {
//				reset_flag = 0;
//				soundplay(1);
//			}
//		}
//	}
//
//	/***** initialize method *****/
//	// initialize all variables and set as start state
//	public void initialize() {
//		// initialize flags
//		set_dx = 0;
//		set_dy = 0;
//		set_state = 0;// 0:normal,1:rotate 90 degree clockwise,2:rotate 180
//						// degree clockwise,3:rotate 270 degree clockwise
//
//		// count the time of downing block
//		count = 0;
//
//		// delete line variables
//		line_num = 0;// count of delete line
//
//		// point,level
//		point = 0;
//		d_point = 0;
//		dp_display_count = 0;
//		level = 1;
//		deleted_lines = 0;
//
//		// joy stick flag
//		mode = 0;// 0:joy stick 1:touch action
//		left = 0;
//		right = 0;
//		down = 0;
//		rotate = 0;
//		double_down = 0;
//		pause = 0;
//		pause_count = 0;
//		delete_flag = 0;
//		delete_count = 0;
//
//		// etc flags
//		menu_flag = MENU;// 0:menu 1:on play
//		prepause_menu_flag = menu_flag;
//		reset_flag = 0;
//		gameover_flag = 0;
//		gameover_count = 0;
//		start_count_flag = 0;
//		start_count = 0;
//
//		run_count = 0;
//		time_count = 0;
//
//		// initialization of matrix "area"
//		set_initialize();
//		// alphabet setting
//		set_startstate();
//	}
//
//	/***** draw main board methods *****/
//	// main drawing process
//	@Override
//	protected void onDraw(Canvas canvas) {
//		Paint paint = new Paint();
//
//		// mode switch
//		switch (menu_flag) {
//		case MENU:
//			draw_menu(canvas, paint);
//			break;
//		case PLAY:
//			if (gameover_flag == 1) {
//				draw_gameover(canvas, paint);
//			} else if (start_count_flag == 1) {
//				draw_startcount(canvas, paint);
//			} else if (delete_flag == 1) {
//				draw_delline(canvas, paint);
//			} else {
//				draw_graphics(canvas, paint);
//			}
//			break;
//		case PAUSE:
//			draw_pause(canvas, paint);
//			break;
//		case SCORE:
//			draw_scores(canvas, paint);
//			break;
//		case SETTING:
//			draw_setting(canvas, paint);
//			break;
//		case SPMENU:
//			draw_spmenu(canvas, paint);
//			break;
//		case SPEASY:
//			if (gameover_flag == 1) {
//				draw_speasy_gameover(canvas, paint);
//			} else if (start_count_flag == 1) {
//				draw_startcount(canvas, paint);
//			} else if (delete_flag == 1) {
//				draw_speasy_delline(canvas, paint);
//			} else {
//				draw_speasy_graphics(canvas, paint);
//			}
//			break;
//		case SPMEDIUM:
//			if (gameover_flag == 1) {
//				draw_gameover(canvas, paint);
//			} else if (start_count_flag == 1) {
//				draw_startcount(canvas, paint);
//			} else if (delete_flag == 1) {
//				draw_delline(canvas, paint);
//			} else {
//				draw_graphics(canvas, paint);
//			}
//			break;
//		case SPHARD:
//			if (gameover_flag == 1) {
//				draw_gameover(canvas, paint);
//			} else if (start_count_flag == 1) {
//				draw_startcount(canvas, paint);
//			} else if (delete_flag == 1) {
//				draw_delline(canvas, paint);
//			} else {
//				draw_graphics(canvas, paint);
//			}
//			break;
//		case SPHELL:
//			if (gameover_flag == 1) {
//				draw_gameover(canvas, paint);
//			} else if (start_count_flag == 1) {
//				draw_startcount(canvas, paint);
//			} else if (delete_flag == 1) {
//				draw_delline(canvas, paint);
//			} else {
//				draw_graphics(canvas, paint);
//			}
//			break;
//		}
//
//		// draw patterned bitmap
//		draw_pattern_bitmap(canvas, paint);
//
//		// draw patterned text
//		draw_pattern_text(canvas, paint);
//	}
//
//	// draw main menu
//	public void draw_menu(Canvas canvas, Paint paint) {
//		// paste background
//		draw_movebg(canvas, paint);
//	}
//
//	// draw pause
//	public void draw_pause(Canvas canvas, Paint paint) {
//		// background
//		draw_background(canvas, paint);
//
//		// special GREEK effect
//		draw_greek_effect(canvas, paint);
//
//		// draw pause box
//		draw_list_box(canvas, paint, PAUSE_LINE_NUM, pause_count);
//
//		// draw comment if special = NICO
//		// draw_comment(canvas, paint);
//		draw_comment_ver2(canvas, paint);
//	}
//
//	// draw game base graphics
//	public void draw_graphics(Canvas canvas, Paint paint) {
//		// background
//		draw_background(canvas, paint);
//
//		// special GREEK effect
//		draw_greek_effect(canvas, paint);
//
//		// draw comment
//		// draw_comment(canvas, paint);
//		draw_comment_ver2(canvas, paint);
//
//		// draw expected drop point
//		draw_expected(canvas, paint);
//
//		// draw dropping and stable blocks
//		draw_blocks(canvas, paint);
//
//		// draw next board
//		draw_next_blocks(canvas, paint, 0);
//
//		// draw guide line
//		draw_board_lines(canvas, paint);
//	}
//
//	// draw deleting lines
//	public void draw_delline(Canvas canvas, Paint paint) {
//		// background
//		draw_background(canvas, paint);
//
//		// special GREEK effect
//		draw_greek_effect(canvas, paint);
//
//		// draw comment
//		// draw_comment(canvas, paint);
//		draw_comment_ver2(canvas, paint);
//
//		// draw dropping and stable blocks
//		draw_blocks(canvas, paint);
//
//		// draw next blocks
//		draw_next_blocks(canvas, paint, 0);
//
//		// draw guide line
//		draw_board_lines(canvas, paint);
//	}
//
//	// draw game over graphics
//	public void draw_gameover(Canvas canvas, Paint paint) {
//		// paste background
//		draw_background(canvas, paint);
//
//		// special GREEK effect
//		draw_greek_effect(canvas, paint);
//
//		// draw dropping and stable blocks
//		draw_blocks(canvas, paint);
//
//		// draw next board and option = 1
//		draw_next_blocks(canvas, paint, 1);
//
//		// draw comment if special = NICO
//		// draw_comment(canvas, paint);
//		draw_comment_ver2(canvas, paint);
//
//		// draw guide line
//		draw_board_lines(canvas, paint);
//
//		// draw game over message
//		draw_gameover_message(canvas, paint);
//	}
//
//	// draw start count graphics
//	public void draw_startcount(Canvas canvas, Paint paint) {
//		// back ground
//		draw_background(canvas, paint);
//
//		// draw next blocks
//		draw_next_blocks(canvas, paint, 0);
//
//		// draw blocks
//		draw_blocks(canvas, paint);
//
//		// draw guide line
//		draw_board_lines(canvas, paint);
//
//		// draw count
//		paint.setTextSize(200 + 5 * ((start_count - 1) % 20));
//		paint.setColor(Color.argb(12 * ((start_count - 1) % 20), 255, 255, 255));
//		canvas.drawText("" + (int) (3 - (start_count - 1) / 20),
//				200 - (200 + 5 * ((start_count - 1) % 20)) / 4,
//				300 + (200 + 5 * ((start_count - 1) % 20)) / 4, paint);
//	}
//
//	// draw best scores
//	private void draw_scores(Canvas canvas, Paint paint) {
//		// paste background
//		draw_movebg(canvas, paint);
//		paint.setColor(Color.argb(192, 200, 200, 200));
//		canvas.drawRect(20, 20, 460, 660, paint);
//	}
//
//	// draw setting display
//	private void draw_setting(Canvas canvas, Paint paint) {
//		draw_movebg(canvas, paint);
//		// draw setting box
//		draw_list_box(canvas, paint, SETTING_LINE_NUM, 1000);
//	}
//
//	// draw special menu display
//	private void draw_spmenu(Canvas canvas, Paint paint) {
//		draw_movebg(canvas, paint);
//		// draw setting box
//		draw_list_box(canvas, paint, SPMENU_LINE_NUM, 1000);
//	}
//
//	/***** draw add method *****/
//	// draw add game base graphics
//	private void draw_speasy_graphics(Canvas canvas, Paint paint) {
//		// background
//		draw_background(canvas, paint);
//
//		// add effect
//		draw_arrow_effect(canvas, paint);
//
//		// special GREEK effect
//		draw_greek_effect(canvas, paint);
//
//		// draw comment
//		// draw_comment(canvas, paint);
//		draw_comment_ver2(canvas, paint);
//
//		// draw expected drop point
//		draw_add_expected(canvas, paint);
//
//		// draw dropping and stable blocks
//		draw_add_blocks(canvas, paint);
//
//		// draw next board
//		draw_next_blocks(canvas, paint, 0);
//
//		// draw guide line
//		draw_board_lines(canvas, paint);
//	}
//
//	// draw add deleting lines
//	private void draw_speasy_delline(Canvas canvas, Paint paint) {
//		// background
//		draw_background(canvas, paint);
//
//		// add effect
//		draw_arrow_effect(canvas, paint);
//
//		// special GREEK effect
//		draw_greek_effect(canvas, paint);
//
//		// draw comment
//		// draw_comment(canvas, paint);
//		draw_comment_ver2(canvas, paint);
//
//		// draw dropping and stable blocks
//		draw_add_blocks(canvas, paint);
//
//		// draw next blocks
//		draw_next_blocks(canvas, paint, 0);
//
//		// draw guide line
//		draw_board_lines(canvas, paint);
//	}
//
//	// draw add game over
//	private void draw_speasy_gameover(Canvas canvas, Paint paint) {
//		// paste background
//		draw_background(canvas, paint);
//
//		// add effect
//		draw_arrow_effect(canvas, paint);
//
//		// special GREEK effect
//		draw_greek_effect(canvas, paint);
//
//		// draw dropping and stable blocks
//		draw_add_blocks(canvas, paint);
//
//		// draw next board and option = 1
//		draw_next_blocks(canvas, paint, 1);
//
//		// draw comment if special = NICO
//		// draw_comment(canvas, paint);
//		draw_comment_ver2(canvas, paint);
//
//		// draw guide line
//		draw_board_lines(canvas, paint);
//
//		// draw game over message
//		draw_gameover_message(canvas, paint);
//	}
//
//	/***** draw parts method *****/
//	// draw background
//	private void draw_background(Canvas canvas, Paint paint) {
//		// paste background
//		bg[ans_data].draw(canvas);
//
//		// draw clock
//		if (ans_data == 6) {
//			paint.setColor(WHITE);
//			// long
//			canvas.drawLine(
//					236 + (int) (6 * Math.cos(Math.PI * (time_count % 60) / 30
//							- Math.PI / 2)),
//					360 + (int) (6 * Math.sin(Math.PI * (time_count % 60) / 30
//							- Math.PI / 2)),
//					236 + (int) (140 * Math.cos(Math.PI * (time_count % 60)
//							/ 30 - Math.PI / 2)),
//					350 + (int) (140 * Math.sin(Math.PI * (time_count % 60)
//							/ 30 - Math.PI / 2)), paint);
//			// short
//			canvas.drawLine(
//					236 + (int) (6 * Math.cos(Math.PI * time_count / 1800
//							- Math.PI / 2)),
//					360 + (int) (6 * Math.sin(Math.PI * time_count / 1800
//							- Math.PI / 2)),
//					236 + (int) (80 * Math.cos(Math.PI * time_count / 1800
//							- Math.PI / 2)),
//					350 + (int) (80 * Math.sin(Math.PI * time_count / 1800
//							- Math.PI / 2)), paint);
//		}
//	}
//
//	// draw moving background
//	private void draw_movebg(Canvas canvas, Paint paint) {
//		int oneframe = Width / MPF;
//		int bg1 = ((int) (movebg_count / oneframe)) % 16;
//		int bg2 = (bg1 + 1) % 16;
//
//		bg[movebg_no[bg1]].setBounds(-(movebg_count - oneframe * bg1) * MPF, 0,
//				Width - (movebg_count - oneframe * bg1) * MPF, Height);
//		bg[movebg_no[bg2]].setBounds(Width - (movebg_count - oneframe * bg1)
//				* MPF, 0, Width * 2 - (movebg_count - oneframe * bg1) * MPF,
//				Height);
//		bg[movebg_no[bg1]].draw(canvas);
//		bg[movebg_no[bg2]].draw(canvas);
//	}
//
//	// draw expected drop point
//	private void draw_expected(Canvas canvas, Paint paint) {
//		for (int i = 0; i < HEIGHT - 1; i++) {
//			if (set_y[3] + i == HEIGHT - 1 || set_y[1] + i == HEIGHT - 1
//					|| set_y[2] + i == HEIGHT - 1 || set_y[0] + i == HEIGHT - 1
//					|| state[set_x[3]][set_y[3] + i + 1] == STABLE
//					|| state[set_x[1]][set_y[1] + i + 1] == STABLE
//					|| state[set_x[2]][set_y[2] + i + 1] == STABLE
//					|| state[set_x[0]][set_y[0] + i + 1] == STABLE) {
//				for (int j = 0; j < 4; j++) {
//					// avoid over line draw
//					if (set_y[j] + i > 0) {
//						paint.setColor(COLOR[set_color]);
//						// add design
//						if ((set_x[j] < WIDTH - 1)
//								&& (state[set_x[j] + 1][set_y[j]] == MOVE)) {
//							canvas.drawRect(15 + LENGTH * (set_x[j] + 1), 20
//									+ LENGTH * (set_y[j] + i - 1), 20 + LENGTH
//									* (set_x[j] + 1), 30 + LENGTH
//									* (set_y[j] + i - 1), paint);
//							canvas.drawRect(15 + LENGTH * (set_x[j] + 1), 40
//									+ LENGTH * (set_y[j] + i - 1), 20 + LENGTH
//									* (set_x[j] + 1), 50 + LENGTH
//									* (set_y[j] + i - 1), paint);
//						} else {
//							canvas.drawRect(15 + LENGTH * (set_x[j] + 1), 20
//									+ LENGTH * (set_y[j] + i - 1), 20 + LENGTH
//									* (set_x[j] + 1), 50 + LENGTH
//									* (set_y[j] + i - 1), paint);
//						}
//						if ((set_x[j] > 0)
//								&& (state[set_x[j] - 1][set_y[j]] == MOVE)) {
//							canvas.drawRect(20 + LENGTH * set_x[j], 20 + LENGTH
//									* (set_y[j] + i - 1), 25 + LENGTH
//									* set_x[j], 30 + LENGTH
//									* (set_y[j] + i - 1), paint);
//							canvas.drawRect(20 + LENGTH * set_x[j], 40 + LENGTH
//									* (set_y[j] + i - 1), 25 + LENGTH
//									* set_x[j], 50 + LENGTH
//									* (set_y[j] + i - 1), paint);
//						} else {
//							canvas.drawRect(20 + LENGTH * set_x[j], 20 + LENGTH
//									* (set_y[j] + i - 1), 25 + LENGTH
//									* set_x[j], 50 + LENGTH
//									* (set_y[j] + i - 1), paint);
//						}
//						if ((set_y[j] < HEIGHT - 1)
//								&& (state[set_x[j]][set_y[j] + 1] == MOVE)) {
//							canvas.drawRect(20 + LENGTH * set_x[j], 15 + LENGTH
//									* (set_y[j] + i), 30 + LENGTH * set_x[j],
//									20 + LENGTH * (set_y[j] + i), paint);
//							canvas.drawRect(40 + LENGTH * set_x[j], 15 + LENGTH
//									* (set_y[j] + i), 50 + LENGTH * set_x[j],
//									20 + LENGTH * (set_y[j] + i), paint);
//						} else {
//							canvas.drawRect(20 + LENGTH * set_x[j], 15 + LENGTH
//									* (set_y[j] + i), 50 + LENGTH * set_x[j],
//									20 + LENGTH * (set_y[j] + i), paint);
//						}
//						if ((set_y[j] > 0)
//								&& (state[set_x[j]][set_y[j] - 1] == MOVE)) {
//							canvas.drawRect(20 + LENGTH * set_x[j], 20 + LENGTH
//									* (set_y[j] + i - 1), 30 + LENGTH
//									* set_x[j], 25 + LENGTH
//									* (set_y[j] + i - 1), paint);
//							canvas.drawRect(40 + LENGTH * set_x[j], 20 + LENGTH
//									* (set_y[j] + i - 1), 50 + LENGTH
//									* set_x[j], 25 + LENGTH
//									* (set_y[j] + i - 1), paint);
//						} else {
//							canvas.drawRect(20 + LENGTH * set_x[j], 20 + LENGTH
//									* (set_y[j] + i - 1), 50 + LENGTH
//									* set_x[j], 25 + LENGTH
//									* (set_y[j] + i - 1), paint);
//						}
//						canvas.drawRect(31 + LENGTH * set_x[j], 31 + LENGTH
//								* (set_y[j] + i - 1), 39 + LENGTH * set_x[j],
//								39 + LENGTH * (set_y[j] + i - 1), paint);
//					}
//				}
//				break;
//			}
//		}
//	}
//
//	// draw add expected drop point
//	private void draw_add_expected(Canvas canvas, Paint paint) {
//		for (int i = 0; i < HEIGHT - 1; i++) {
//			// if drop point or not
//			if ((set_y[3] + i == HEIGHT - 1) || (set_y[1] + i == HEIGHT - 1)
//					|| (set_y[2] + i == HEIGHT - 1)
//					|| (set_y[0] + i == HEIGHT - 1)
//					|| (state[set_x[3]][set_y[3] + i + 1] == STABLE)
//					|| (state[set_x[1]][set_y[1] + i + 1] == STABLE)
//					|| (state[set_x[2]][set_y[2] + i + 1] == STABLE)
//					|| (state[set_x[0]][set_y[0] + i + 1] == STABLE)) {
//				for (int j = 0; j < 4; j++) {
//					// avoid over line draw
//					if (set_y[j] + i > 0) {
//						paint.setColor(COLOR[set_color]);
//						// add design
//						if ((set_x[j] < WIDTH - 1)
//								&& (state[set_x[j] + 1][set_y[j]] == MOVE)) {
//							canvas.drawRect(20 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 20 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 25 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 30 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//							canvas.drawRect(20 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 40 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 25 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 50 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//						} else {
//							canvas.drawRect(20 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 20 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 25 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 50 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//						}
//						if ((set_x[j] > 0)
//								&& (state[set_x[j] - 1][set_y[j]] == MOVE)) {
//							canvas.drawRect(45 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 20 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 50 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 30 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//							canvas.drawRect(45 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 40 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 50 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 50 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//						} else {
//							canvas.drawRect(45 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 20 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 50 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 50 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//						}
//						if ((set_y[j] < HEIGHT - 1)
//								&& (state[set_x[j]][set_y[j] + 1] == MOVE)) {
//							canvas.drawRect(20 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 20 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 30 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 25 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//							canvas.drawRect(40 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 20 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 50 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 25 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//						} else {
//							canvas.drawRect(20 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 20 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 50 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 25 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//						}
//						if ((set_y[j] > 0)
//								&& (state[set_x[j]][set_y[j] - 1] == MOVE)) {
//							canvas.drawRect(20 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 45 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 30 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 50 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//							canvas.drawRect(40 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 45 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 50 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 50 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//						} else {
//							canvas.drawRect(20 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 45 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), 50 + LENGTH
//									* (WIDTH - 1 - set_x[j]), 50 + LENGTH
//									* (HEIGHT - 1 - set_y[j] - i), paint);
//						}
//						canvas.drawRect(31 + LENGTH * (WIDTH - 1 - set_x[j]),
//								31 + LENGTH * (HEIGHT - 1 - set_y[j] - i), 39
//										+ LENGTH * (WIDTH - 1 - set_x[j]), 39
//										+ LENGTH * (HEIGHT - 1 - set_y[j] - i),
//								paint);
//					}
//				}
//				break;
//			}
//		}
//	}
//
//	// draw stable and dropping blocks
//	private void draw_blocks(Canvas canvas, Paint paint) {
//		int temp_color;
//		int correct_pos[] = new int[2];
//		int run_line = 0;
//
//		paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
//		paint.setColor(BLACK);
//		paint.setTextSize(ALP_SIZE);
//
//		for (int j = 1; j < HEIGHT; j++) {
//			run_line = 0;
//			for (int i = 0; i < WIDTH; i++) {
//				// if block exists in area[i][j],set color of that area
//				if (color[i][j] != NO) {
//					switch (alphabet[i][j]) {
//					case 26:
//						temp_color = (int) (Math.random() * 7 + 1);
//						correct_pos[0] = 7;
//						correct_pos[1] = 25;
//						break;
//					case 27:
//						temp_color = WH;
//						correct_pos[0] = 1;
//						correct_pos[1] = 25;
//						break;
//					case 28:
//						temp_color = WH;
//						correct_pos[0] = 2;
//						correct_pos[1] = 25;
//						break;
//					case 29:
//						temp_color = GR;
//						correct_pos[0] = 5;
//						correct_pos[1] = 25;
//						break;
//					case 30:
//						temp_color = WH;
//						correct_pos[0] = 7;
//						correct_pos[1] = 25;
//						break;
//					default:
//						temp_color = color[i][j];
//						correct_pos[0] = 5;
//						correct_pos[1] = 25;
//						break;
//					}
//					// effect of each flag
//					if (gameover_flag == 1 && j >= HEIGHT - gameover_count) {
//						temp_color = GR;
//					} else if (delete_flag == 1) {
//						for (int k = line_num - 1; k >= 0; k--) {
//							// if this line is deleted line effect changes
//							if (j == line_list[k]) {
//								run_line = line_list[k];
//								break;
//							}
//						}
//					}
//					// draw
//					if (run_line != 0) {
//						block[temp_color].setBounds(CORNER_X + LENGTH * i
//								- delete_count * delete_run_speed, CORNER_Y
//								+ LENGTH * (j - 1), CORNER_X + LENGTH * (i + 1)
//								- delete_count * delete_run_speed, CORNER_Y
//								+ LENGTH * j);
//						block[temp_color].draw(canvas);
//						if (special == SP_GREEK) {
//							canvas.drawText(GREEK[alphabet[i][j]], CORNER_X
//									+ LENGTH * i - delete_count
//									* delete_run_speed + correct_pos[0],
//									CORNER_Y + LENGTH * (j - 1)
//											+ correct_pos[1], paint);
//						} else {
//							canvas.drawText(ALPHABET[alphabet[i][j]], CORNER_X
//									+ LENGTH * i - delete_count
//									* delete_run_speed + correct_pos[0],
//									CORNER_Y + LENGTH * (j - 1)
//											+ correct_pos[1], paint);
//						}
//						// draw add effect
//						if (special == SP_NICO) {
//							if (run_line % 2 == 0) {
//								canvas.drawText("ブ━━━━⊂二（＾ω＾　）二二⊃━━━━ン",
//										CORNER_X + LENGTH * (WIDTH + 1)
//												- delete_count
//												* delete_run_speed, CORNER_Y
//												+ LENGTH * (j - 1)
//												+ correct_pos[1], paint);
//							} else {
//								canvas.drawText("キタ━━━━━(ﾟ∀ﾟ )━━━━━!!",
//										CORNER_X + LENGTH * (WIDTH + 1)
//												- delete_count
//												* delete_run_speed, CORNER_Y
//												+ LENGTH * (j - 1)
//												+ correct_pos[1], paint);
//							}
//						}
//					} else {
//						block[temp_color].setBounds(CORNER_X + LENGTH * i,
//								CORNER_Y + LENGTH * (j - 1), CORNER_X + LENGTH
//										* (i + 1), CORNER_Y + LENGTH * j);
//						block[temp_color].draw(canvas);
//						if (special == SP_GREEK) {
//							canvas.drawText(GREEK[alphabet[i][j]], CORNER_X
//									+ LENGTH * i + correct_pos[0], CORNER_Y
//									+ LENGTH * (j - 1) + correct_pos[1], paint);
//						} else {
//							canvas.drawText(ALPHABET[alphabet[i][j]], CORNER_X
//									+ LENGTH * i + correct_pos[0], CORNER_Y
//									+ LENGTH * (j - 1) + correct_pos[1], paint);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	// draw stable and dropping blocks
//	private void draw_add_blocks(Canvas canvas, Paint paint) {
//		int temp_color;
//		int correct_pos[] = new int[2];
//		int run_line = 0;
//
//		paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
//		paint.setColor(BLACK);
//		paint.setTextSize(ALP_SIZE);
//
//		for (int j = 1; j < HEIGHT; j++) {
//			run_line = 0;
//			for (int i = 0; i < WIDTH; i++) {
//				// if block exists in area[i][j],set color of that area
//				if (color[i][j] != NO) {
//					switch (alphabet[i][j]) {
//					case 26:
//						temp_color = (int) (Math.random() * 7 + 1);
//						correct_pos[0] = 7;
//						correct_pos[1] = 25;
//						break;
//					case 27:
//						temp_color = WH;
//						correct_pos[0] = 1;
//						correct_pos[1] = 25;
//						break;
//					case 28:
//						temp_color = WH;
//						correct_pos[0] = 2;
//						correct_pos[1] = 25;
//						break;
//					case 29:
//						temp_color = GR;
//						correct_pos[0] = 5;
//						correct_pos[1] = 25;
//						break;
//					case 30:
//						temp_color = WH;
//						correct_pos[0] = 7;
//						correct_pos[1] = 25;
//						break;
//					default:
//						temp_color = color[i][j];
//						correct_pos[0] = 5;
//						correct_pos[1] = 25;
//						break;
//					}
//					// effect of each flag
//					if (gameover_flag == 1 && j >= HEIGHT - gameover_count) {
//						temp_color = GR;
//					} else if (delete_flag == 1) {
//						for (int k = line_num - 1; k >= 0; k--) {
//							// if this line is deleted line effect changes
//							if (j == line_list[k]) {
//								run_line = line_list[k];
//								break;
//							}
//						}
//					}
//					// draw
//					if (run_line != 0) {
//						block[temp_color].setBounds(CORNER_X + LENGTH
//								* (WIDTH - 1 - i) - delete_count
//								* delete_run_speed, CORNER_Y + LENGTH
//								* (HEIGHT - 1 - j),
//								CORNER_X + LENGTH * (WIDTH - i) - delete_count
//										* delete_run_speed, CORNER_Y + LENGTH
//										* (HEIGHT - j));
//						block[temp_color].draw(canvas);
//						if (special == SP_GREEK) {
//							canvas.drawText(GREEK[alphabet[i][j]], CORNER_X
//									+ LENGTH * (WIDTH - 1 - i) - delete_count
//									* delete_run_speed + correct_pos[0],
//									CORNER_Y + LENGTH * (HEIGHT - 1 - j)
//											+ correct_pos[1], paint);
//						} else {
//							canvas.drawText(ALPHABET[alphabet[i][j]], CORNER_X
//									+ LENGTH * (WIDTH - 1 - i) - delete_count
//									* delete_run_speed + correct_pos[0],
//									CORNER_Y + LENGTH * (HEIGHT - 1 - j)
//											+ correct_pos[1], paint);
//						}
//						// draw add effect
//						if (special == SP_NICO) {
//							if (run_line % 2 == 0) {
//								canvas.drawText("ブ━━━━⊂二（＾ω＾　）二二⊃━━━━ン",
//										CORNER_X + LENGTH * (WIDTH + 1)
//												- delete_count
//												* delete_run_speed, CORNER_X
//												+ LENGTH * (HEIGHT - 1 - j)
//												+ correct_pos[1], paint);
//							} else {
//								canvas.drawText("キタ━━━━━(ﾟ∀ﾟ )━━━━━!!",
//										CORNER_X + LENGTH * (WIDTH + 1)
//												- delete_count
//												* delete_run_speed, CORNER_X
//												+ LENGTH * (HEIGHT - 1 - j)
//												+ correct_pos[1], paint);
//							}
//						} else {
//							if (run_line % 2 == 0) {
//								canvas.drawText(
//										"T h a n k  Y o u  F o r  P l a y i n g !",
//										CORNER_X + LENGTH * (WIDTH + 1)
//												- delete_count
//												* delete_run_speed, CORNER_X
//												+ LENGTH * (HEIGHT - 1 - j)
//												+ correct_pos[1], paint);
//							} else {
//								canvas.drawText(
//										"I  W i s h  Y o u  E n j o y e d  P l a y i n g !",
//										CORNER_X + LENGTH * (WIDTH + 1)
//												- delete_count
//												* delete_run_speed, CORNER_X
//												+ LENGTH * (HEIGHT - 1 - j)
//												+ correct_pos[1], paint);
//							}
//						}
//					} else {
//						block[temp_color]
//								.setBounds(CORNER_X + LENGTH * (WIDTH - 1 - i),
//										CORNER_Y + LENGTH * (HEIGHT - 1 - j),
//										CORNER_X + LENGTH * (WIDTH - i),
//										CORNER_Y + LENGTH * (HEIGHT - j));
//						block[temp_color].draw(canvas);
//						if (special == SP_GREEK) {
//							canvas.drawText(GREEK[alphabet[i][j]],
//									CORNER_X + LENGTH * (WIDTH - 1 - i)
//											+ correct_pos[0],
//									CORNER_Y + LENGTH * (HEIGHT - 1 - j)
//											+ correct_pos[1], paint);
//						} else {
//							canvas.drawText(ALPHABET[alphabet[i][j]],
//									CORNER_X + LENGTH * (WIDTH - 1 - i)
//											+ correct_pos[0],
//									CORNER_Y + LENGTH * (HEIGHT - 1 - j)
//											+ correct_pos[1], paint);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	// draw next blocks
//	private void draw_next_blocks(Canvas canvas, Paint paint, int option) {
//		// define color
//		int draw_color = 0;
//		int correct_pos[] = new int[2];
//
//		// draw next blocks
//		paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
//		paint.setTextSize(ALP_SIZE);
//		paint.setColor(BLACK);
//		for (int i = 0; i < 4; i++) {
//			// game over or not
//			if (option == 1) {
//				draw_color = GR;
//				correct_pos[0] = 5;
//				correct_pos[1] = 25;
//			} else {
//				switch (set_next_alp[i]) {
//				case 26:
//					draw_color = (int) (Math.random() * 7 + 1);
//					correct_pos[0] = 7;
//					correct_pos[1] = 25;
//					break;
//				case 27:
//					draw_color = WH;
//					correct_pos[0] = 1;
//					correct_pos[1] = 25;
//					break;
//				case 28:
//					draw_color = WH;
//					correct_pos[0] = 2;
//					correct_pos[1] = 25;
//					break;
//				case 29:
//					draw_color = GR;
//					correct_pos[0] = 5;
//					correct_pos[1] = 25;
//					break;
//				case 30:
//					draw_color = WH;
//					correct_pos[0] = 7;
//					correct_pos[1] = 25;
//					break;
//				default:
//					draw_color = set_next_color;
//					correct_pos[0] = 5;
//					correct_pos[1] = 25;
//					break;
//				}
//			}
//			// draw bitmap
//			block[draw_color].setBounds(NEXT_POS[set_next_color][0]
//					+ obpx[set_next_color][NEXT_STATE[set_next_color]][i]
//					* LENGTH, NEXT_POS[set_next_color][1]
//					+ obpy[set_next_color][NEXT_STATE[set_next_color]][i]
//					* LENGTH, NEXT_POS[set_next_color][0]
//					+ obpx[set_next_color][NEXT_STATE[set_next_color]][i]
//					* LENGTH + LENGTH, NEXT_POS[set_next_color][1]
//					+ obpy[set_next_color][NEXT_STATE[set_next_color]][i]
//					* LENGTH + LENGTH);
//			block[draw_color].draw(canvas);
//			// draw text
//			if (special == SP_GREEK) {
//				canvas.drawText(
//						GREEK[set_next_alp[i]],
//						NEXT_POS[set_next_color][0]
//								+ obpx[set_next_color][NEXT_STATE[set_next_color]][i]
//								* LENGTH + correct_pos[0],
//						NEXT_POS[set_next_color][1]
//								+ obpy[set_next_color][NEXT_STATE[set_next_color]][i]
//								* LENGTH + correct_pos[1], paint);
//			} else {
//				canvas.drawText(
//						ALPHABET[set_next_alp[i]],
//						NEXT_POS[set_next_color][0]
//								+ obpx[set_next_color][NEXT_STATE[set_next_color]][i]
//								* LENGTH + correct_pos[0],
//						NEXT_POS[set_next_color][1]
//								+ obpy[set_next_color][NEXT_STATE[set_next_color]][i]
//								* LENGTH + correct_pos[1], paint);
//			}
//		}
//	}
//
//	// draw guide lines
//	private void draw_board_lines(Canvas canvas, Paint paint) {
//		// main board
//		// broad line
//		paint.setColor(BLACK);
//		canvas.drawRect(16, 16, 20, 24 + LENGTH * (HEIGHT - 1), paint);
//		canvas.drawRect(20 + LENGTH * WIDTH, 16, 24 + LENGTH * WIDTH, 24
//				+ LENGTH * (HEIGHT - 1), paint);
//		canvas.drawRect(16, 16, 24 + LENGTH * WIDTH, 20, paint);
//		canvas.drawRect(16, 20 + LENGTH * (HEIGHT - 1), 24 + LENGTH * WIDTH, 24
//				+ LENGTH * (HEIGHT - 1), paint);
//
//		// column line
//		paint.setColor(WHITE);
//		for (int i = 0; i <= WIDTH; i++) {
//			canvas.drawLine(20 + LENGTH * i, 20, 20 + LENGTH * i, 20 + LENGTH
//					* (HEIGHT - 1), paint);
//		}
//
//		// row line
//		for (int i = 0; i < HEIGHT; i++) {
//			canvas.drawLine(20, 20 + LENGTH * i, 20 + LENGTH * WIDTH, 20
//					+ LENGTH * i, paint);
//		}
//
//		// next board
//		canvas.drawLine(30 + LENGTH * WIDTH, 20 + LENGTH, 30 + LENGTH * WIDTH,
//				20 + LENGTH * 6, paint);
//		canvas.drawLine(30 + LENGTH * WIDTH, 20 + LENGTH, 30 + LENGTH
//				* (WIDTH + 4), 20 + LENGTH, paint);
//		canvas.drawLine(30 + LENGTH * (WIDTH + 4), 20 + LENGTH, 30 + LENGTH
//				* (WIDTH + 4), 20 + LENGTH * 6, paint);
//		canvas.drawLine(30 + LENGTH * WIDTH, 20 + LENGTH * 6, 30 + LENGTH
//				* (WIDTH + 4), 20 + LENGTH * 6, paint);
//	}
//
//	// draw patterned text
//	private void draw_pattern_text(Canvas canvas, Paint paint) {
//		paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
//
//		for (int i = 0; i < TEXT_MATRIX_COLUMN; i++) {
//			paint.setTextSize(RATE_TEXT_SIZE[menu_flag][i]);
//			paint.setColor(RATE_TEXT_COLOR[menu_flag][i]);
//			canvas.drawText(Text[menu_flag][i], Text_X[menu_flag][i],
//					Text_Y[menu_flag][i], paint);
//		}
//	}
//
//	// draw patterned bitmap
//	private void draw_pattern_bitmap(Canvas canvas, Paint paint) {
//		for (int i = 0; i < BITMAP_MATRIX_COLUMN; i++) {
//			if (BITMAP_NO[menu_flag][i] != -1) {
//				Bitmap[BITMAP_NO[menu_flag][i]].setBounds(
//						Bitmap_Pos[menu_flag][i][0],
//						Bitmap_Pos[menu_flag][i][1],
//						Bitmap_Pos[menu_flag][i][2],
//						Bitmap_Pos[menu_flag][i][3]);
//				Bitmap[BITMAP_NO[menu_flag][i]].draw(canvas);
//			}
//		}
//	}
//
//	// draw game over message
//	private void draw_gameover_message(Canvas canvas, Paint paint) {
//		if (gameover_count >= 20) {
//			paint.setTypeface(Typeface.create(Typeface.SERIF,
//					Typeface.BOLD_ITALIC));
//			paint.setTextSize(55);
//
//			if (gameover_count >= 20) {
//				paint.setColor(BLACK);
//				canvas.drawText("G", 40,
//						Math.min(345, (int) (34.5 * (gameover_count - 20))),
//						paint);
//				paint.setColor(COLOR[Y1]);
//				canvas.drawText("G", 30,
//						Math.min(350, 35 * (gameover_count - 20)), paint);
//			}
//			if (gameover_count >= 30) {
//				paint.setColor(BLACK);
//				canvas.drawText("A", 90,
//						Math.min(345, (int) (34.5 * (gameover_count - 30))),
//						paint);
//				paint.setColor(COLOR[Y1]);
//				canvas.drawText("A", 80,
//						Math.min(350, 35 * (gameover_count - 30)), paint);
//			}
//			if (gameover_count >= 40) {
//				paint.setColor(BLACK);
//				canvas.drawText("M", 140,
//						Math.min(345, (int) (34.5 * (gameover_count - 40))),
//						paint);
//				paint.setColor(COLOR[Y1]);
//				canvas.drawText("M", 130,
//						Math.min(350, 35 * (gameover_count - 40)), paint);
//			}
//			if (gameover_count >= 50) {
//				paint.setColor(BLACK);
//				canvas.drawText("E", 190,
//						Math.min(345, (int) (34.5 * (gameover_count - 50))),
//						paint);
//				paint.setColor(COLOR[Y1]);
//				canvas.drawText("E", 180,
//						Math.min(350, 35 * (gameover_count - 50)), paint);
//			}
//			if (gameover_count >= 60) {
//				paint.setColor(BLACK);
//				canvas.drawText("O", 250,
//						Math.min(345, (int) (34.5 * (gameover_count - 60))),
//						paint);
//				paint.setColor(COLOR[Y1]);
//				canvas.drawText("O", 240,
//						Math.min(350, 35 * (gameover_count - 60)), paint);
//			}
//			if (gameover_count >= 70) {
//				paint.setColor(BLACK);
//				canvas.drawText("V", 300,
//						Math.min(345, (int) (34.5 * (gameover_count - 70))),
//						paint);
//				paint.setColor(COLOR[Y1]);
//				canvas.drawText("V", 290,
//						Math.min(350, 35 * (gameover_count - 70)), paint);
//			}
//			if (gameover_count >= 80) {
//				paint.setColor(BLACK);
//				canvas.drawText("E", 350,
//						Math.min(345, (int) (34.5 * (gameover_count - 80))),
//						paint);
//				paint.setColor(COLOR[Y1]);
//				canvas.drawText("E", 340,
//						Math.min(350, 35 * (gameover_count - 80)), paint);
//			}
//			if (gameover_count >= 90) {
//				paint.setColor(BLACK);
//				canvas.drawText("R", 400,
//						Math.min(345, (int) (34.5 * (gameover_count - 90))),
//						paint);
//				paint.setColor(COLOR[Y1]);
//				canvas.drawText("R", 390,
//						Math.min(350, 35 * (gameover_count - 90)), paint);
//			}
//			paint.setColor(BLACK);
//			canvas.drawLine(35, 353,
//					Math.min(445, 35 + 5 * (gameover_count - 20)), 353, paint);
//			paint.setColor(COLOR[Y1]);
//			canvas.drawLine(30, 358,
//					Math.min(440, 30 + 5 * (gameover_count - 20)), 358, paint);
//		}
//	}
//
//	// draw comment (special NICO effect)
//	private void draw_comment(Canvas canvas, Paint paint) {
//		if (special == SP_NICO) {
//			int red_line = 0;
//			for (int i = 0; i < run_count + 1; i++) {
//				if (runcmt_x[i] > -Math.abs(runcmt_speed[i])
//						* Math.abs(runcmt_speed[i]) * 2) {
//					if (runcmt_speed[i] < 0) {
//						paint.setColor(Color.argb(220, 255, 0, 0));
//						paint.setTextSize(48);
//						runcmt_x[i] += runcmt_speed[i];
//						canvas.drawText(comment[runcmt_no[i]],
//								120 + runcmt_speed[i] * 6, 400 - 50 * red_line,
//								paint);
//						red_line++;
//					} else {
//						paint.setColor(Color.argb(220, 255, 255, 255));
//						paint.setTextSize(40);
//						runcmt_x[i] -= runcmt_speed[i];
//						canvas.drawText(comment[runcmt_no[i]], runcmt_x[i],
//								runcmt_y[i], paint);
//					}
//				}
//			}
//		}
//	}
//
//	// draw comment ver2
//	private void draw_comment_ver2(Canvas canvas, Paint paint) {
//		if (special == SP_NICO) {
//			for (int i = 0; i < CMT_ROW; i++) {
//				int no1 = cmt_no[i][0];
//				int no2 = cmt_no[i][1];
//				// comment1
//				if (no1 != -1) {
//					if (runcmt_speed[no1] == 0) {
//						paint.setColor(COLOR[Re]);
//						paint.setTextSize(CMT_TXTST);
//					} else {
//						paint.setColor(WHITE);
//						paint.setTextSize(CMT_TXTMV);
//					}
//					runcmt_x[no1] -= runcmt_speed[no1];
//					canvas.drawText(comment[runcmt_no[no1]], runcmt_x[no1],
//							runcmt_y[no1], paint);
//					runcmt_count[no1]++;
//				}
//				// comment2
//				if (no2 != -1 && runcmt_no[no2] != runcmt_no[no1]) {
//					if (runcmt_speed[no2] == 0) {
//						paint.setColor(COLOR[Re]);
//						paint.setTextSize(CMT_TXTST);
//					} else {
//						paint.setColor(WHITE);
//						paint.setTextSize(CMT_TXTMV);
//					}
//					runcmt_x[no2] -= runcmt_speed[no2];
//					canvas.drawText(comment[runcmt_no[no2]], runcmt_x[no2],
//							runcmt_y[no2], paint);
//					runcmt_count[no2]++;
//				}
//			}
//		}
//	}
//
//	// draw special GREEK effect
//	private void draw_greek_effect(Canvas canvas, Paint paint) {
//		if (special == SP_GREEK) {
//			paint.setTypeface(Typeface.create(Typeface.SERIF,
//					Typeface.BOLD_ITALIC));
//
//			for (int i = 0; i < string_last_no; i++) {
//				paint.setTextSize(20 + 5 * string_count[i]);
//				paint.setColor(COLOR[string_color[i]]);
//				canvas.drawText(GREEK[string_no[i]], string_x[i], string_y[i],
//						paint);
//			}
//		}
//	}
//
//	// draw add effect
//	private void draw_arrow_effect(Canvas canvas, Paint paint) {
//		paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC));
//
//		for (int i = 0; i < arrow_last_no; i++) {
//			paint.setTextSize(arrow_size[i]);
//			paint.setColor(Color.argb((int) (arrow_size[i] * 1.5), 0, 0, 0));
//			canvas.drawText("↑", arrow_x[i], arrow_y[i], paint);
//		}
//	}
//
//	// list box of pause and settings
//	private void draw_list_box(Canvas canvas, Paint paint, int line,
//			int shower_count) {
//		// draw rectangle
//		if (shower_count > 0) {
//			paint.setColor(GREY);
//			canvas.drawRect(10, 100, 470,
//					Math.min(PAUSE_LINE[line], shower_count + 100), paint);
//			paint.setColor(DGREY);
//			canvas.drawRect(16, 106, 464,
//					Math.min(PAUSE_LINE[line] - 6, shower_count + 94), paint);
//			paint.setColor(LGREY);
//			canvas.drawRect(20, 110, 464,
//					Math.min(PAUSE_LINE[line] - 6, shower_count + 94), paint);
//			canvas.drawRect(
//					19,
//					Math.min(PAUSE_LINE[line] - 9,
//							Math.max(100, shower_count + 91)), 20,
//					Math.min(PAUSE_LINE[line] - 8, shower_count + 92), paint);
//			canvas.drawRect(
//					18,
//					Math.min(PAUSE_LINE[line] - 8,
//							Math.max(100, shower_count + 92)), 20,
//					Math.min(PAUSE_LINE[line] - 7, shower_count + 93), paint);
//			canvas.drawRect(
//					17,
//					Math.min(PAUSE_LINE[line] - 7,
//							Math.max(100, shower_count + 93)), 20,
//					Math.min(PAUSE_LINE[line] - 6, shower_count + 94), paint);
//			canvas.drawRect(461, 110, 464, Math.min(109, shower_count + 100),
//					paint);
//			canvas.drawRect(462, 109, 464, Math.min(108, shower_count + 100),
//					paint);
//			canvas.drawRect(463, 108, 464, Math.min(107, shower_count + 100),
//					paint);
//			paint.setColor(WHITE);
//			canvas.drawRect(20, 110, 460,
//					Math.min(PAUSE_LINE[line] - 10, shower_count + 90), paint);
//		}
//
//		// draw lines
//		paint.setColor(BLACK);
//		for (int i = 0; i < line - 1; i++) {
//			if (shower_count > PAUSE_LINE[i]
//					&& (i != 0 || menu_flag != SETTING)) {
//				canvas.drawLine(20, (i + 2) * 100, 460, (i + 2) * 100, paint);
//			}
//		}
//
//		// setting option draw
//		if (menu_flag == SETTING) {
//			paint.setColor(BLACK);
//			canvas.drawRect(46, 216, 434, 284, paint);
//			paint.setColor(LGREY);
//			canvas.drawRect(50, 220, 430, 280, paint);
//			paint.setColor(COLOR[Y1]);
//			canvas.drawRect(50, 220, 50 + 380 * volume, 280, paint);
//		}
//	}
//
//	// reset shower :: NOT IN USE
//	private void reset_shower(Canvas canvas, Paint paint) {
//		// reset y or n draw
//		if (reset_flag == 1) {
//			paint.setColor(GREY);
//			canvas.drawRect(60, 250, 420, 450, paint);
//			paint.setColor(DGREY);
//			canvas.drawRect(66, 346, 414, 444, paint);
//			paint.setColor(LGREY);
//			canvas.drawRect(70, 350, 414, 444, paint);
//			canvas.drawRect(69, 441, 70, 442, paint);
//			canvas.drawRect(68, 442, 70, 443, paint);
//			canvas.drawRect(67, 443, 70, 444, paint);
//			canvas.drawRect(411, 349, 414, 350, paint);
//			canvas.drawRect(412, 348, 414, 349, paint);
//			canvas.drawRect(413, 347, 414, 348, paint);
//			paint.setColor(WHITE);
//			canvas.drawRect(70, 350, 410, 440, paint);
//
//			paint.setColor(BLACK);
//			paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
//			paint.setTextSize(35);
//			canvas.drawText("Do you really want", 75, 290, paint);
//			canvas.drawText("to quit a game ?", 85, 330, paint);
//
//			// draw alternatives
//			paint.setTextSize(40);
//			canvas.drawLine(240, 350, 240, 440, paint);
//			canvas.drawText("YES!", 110, 410, paint);
//			canvas.drawText("NO!", 290, 410, paint);
//		}
//	}
//
//	/***** delete methods *****/
//	// delete line activity
//	private void del_line() {
//		// initialize line list
//		for (int i = 0; i < 5; i++) {
//			line_list[i] = 0;
//			line_del_mode[i] = -1;
//		}
//
//		line_num = 0;// count of delete line
//		line_add = 0;// plus and minus blocks
//		int next = 0;// flag of going next line
//		int same_no = -1;// using same alphabet bonus
//		int temp_line_add = 0;
//		int correct_count = 0;
//
//		for (int i = HEIGHT - 1; i >= 0; i--) {
//			next = 0;
//			same_no = -1;
//			temp_line_add = line_add;
//			// all same alphabets bonus
//			if (state[0][i] == STABLE) {
//				for (int j = 1; j < WIDTH; j++) {
//					if (alphabet[j][i] == 26) {
//						if (j == WIDTH - 1) {
//							line_list[line_num] = i;
//							line_del_mode[line_num] = 0;
//							line_num++;
//							next = 1;
//							delete_flag = 1;
//							break;
//						}
//					} else if (state[j][i] == STABLE
//							&& (alphabet[j][i] == same_no || same_no == -1)) {
//						if (same_no == -1) {
//							same_no = alphabet[j][i];
//						}
//						if (j == WIDTH - 1) {
//							line_list[line_num] = i;
//							line_del_mode[line_num] = 0;
//							line_num++;
//							next = 1;
//							delete_flag = 1;
//							break;
//						}
//					} else {
//						break;
//					}
//				}
//			}
//
//			// shift 1 each time and check the connection of length of word
//			// equal to selected word
//			if (next == 0) {
//				// plus or minus
//				for (int j = 0; j < WIDTH; j++) {
//					if (alphabet[j][i] == 27) {
//						line_add += 1;
//					} else if (alphabet[j][i] == 28) {
//						line_add -= 1;
//					}
//				}
//				for (int j = 0; j < WIDTH - (ans_len - 1); j++) {
//					correct_count = 0;
//					for (int k = j; k < WIDTH; k++) {
//						if (state[k][i] == STABLE
//								&& (alphabet[k][i] == ans[correct_count] || alphabet[k][i] == 26)) {
//							correct_count++;
//							if (correct_count == ans_len) {
//								line_list[line_num] = i;
//								line_del_mode[line_num] = 1;
//								line_num++;
//								next = 1;
//								delete_flag = 1;
//								break;
//							}
//						} else if (state[k][i] == STABLE
//								&& alphabet[k][i] == 30) {
//						} else {
//							break;
//						}
//					}
//					if (next == 1) {
//						break;
//					}
//				}
//				// if not delete this line
//				if (next == 0) {
//					line_add = temp_line_add;
//				}
//			}
//
//			// simply 1 row filled
//			if (next == 0) {
//				for (int j = 0; j < WIDTH; j++) {
//					if (state[j][i] == STABLE) {
//						if (j == WIDTH - 1) {
//							line_list[line_num] = i;
//							line_del_mode[line_num] = 2;
//							line_num++;
//							delete_flag = 1;
//							break;
//						}
//					} else {
//						break;
//					}
//				}
//			}
//		}
//
//		// sum of deleted lines
//		deleted_lines += line_num;
//	}
//
//	// delete lines at once
//	public void del_process() {
//		d_point = 0;
//		int mode_point[] = new int[3];
//		int mode_count[] = new int[3];
//		mode_point[0] = 0;
//		mode_count[0] = 0;
//		mode_point[1] = 0;
//		mode_count[1] = 0;
//		mode_point[2] = 0;
//		mode_count[2] = 0;
//
//		// calculate delta point of each delete　mode
//		for (int i = 0; i < line_num; i++) {
//			switch (line_del_mode[i]) {
//			case 0:
//				mode_point[0] = 200 * (mode_count[0] + 1) * (mode_count[0] + 2);
//				mode_count[0]++;
//				break;
//			case 1:
//				mode_point[1] = 50 * (mode_count[1] + 1) * (mode_count[1] + 2);
//				mode_count[1]++;
//				break;
//			case 2:
//				mode_point[2] = 10 * (mode_count[2] - 1);
//				mode_count[2]++;
//				break;
//			}
//		}
//
//		// calculate delta point
//		if (mode_point[1] > 0 || mode_point[0] > 0) {
//			mode_point[2] += 10 * mode_count[2];
//		}
//		d_point = mode_point[0] + mode_point[1] + mode_point[2] + line_add * 20;
//
//		// level up action
//		if (d_point > 0) {
//			for (int border = 12; border > 0; border--) {
//				if (border == 1 && level == 1 && point + d_point >= 0) {
//					soundplay(7);
//					level = 1;
//					time = DROP_TIME[0];
//					break;
//				} else if (point + d_point >= 400 * (border - 1)
//						&& level < border) {
//					level = border;
//					time = DROP_TIME[(border - 1) % 3];
//					levelup_flag = 1;
//					soundplay(3);
//					if ((border - 1) % 3 == 0) {
//						// set new answer
//						ans_data = (int) (Math.random() * 4)
//								+ (int) (border / 3) * 4;
//						cand_alp_len = cand_ans_len[ans_data];
//						ans_len = cand_alp_len;
//						for (int i = 0; i < cand_alp_len; i++) {
//							cand_alp[i] = cand_ans[ans_data][i];
//							ans[i] = cand_ans[ans_data][i];
//						}
//						// special
//						if (menu_flag == SPHELL) {
//							set_hell(hell_order[(int) (border / 3)]);
//						} else if (menu_flag == SPHARD) {
//							set_hard();
//						} else if (menu_flag == SPMEDIUM) {
//							set_medium();
//						}
//						// vibration set
//						((Wordis) getContext()).vibration(VIB_LONG);
//					} else {
//						// vibration set
//						((Wordis) getContext()).vibration(VIB_SHORT);
//					}
//					break;
//				} else if (border == 1) {
//					soundplay(7);
//				}
//			}
//			dp_display_count = 16;
//		} else if (d_point < 0) {
//			for (int border = 4; border > 0; border--) {
//				if (point + d_point <= (level - 1) * 400 - 100 * (border - 1)) {
//					time = Math.min(DROP_TIME[border + 2], time);
//				}
//			}
//			dp_display_count = 16;
//			soundplay(5);
//		}
//
//		// decide point now
//		point += d_point;
//
//		// shift lines over NUM to the below and set that line empty
//		for (int i = line_num - 1; i >= 0; i--) {
//			for (int j = line_list[i]; j > 0; j--) {
//				for (int k = 0; k < WIDTH; k++) {
//					color[k][j] = color[k][j - 1];
//					color[k][j - 1] = NO;
//					alphabet[k][j] = alphabet[k][j - 1];
//					alphabet[k][j - 1] = 0;
//					state[k][j] = state[k][j - 1];
//					state[k][j - 1] = NO;
//				}
//			}
//		}
//	}
//
//	// del_mode 0:word fill 1:normal fill :: NOT IN USE
//	public void del_effect(int start, int end, int del_mode) {
//		// get point
//		d_point = 0;
//		if (del_mode == 0) {
//			if (start == end) {
//				d_point = 100;
//			} else if (start == end + 1) {
//				d_point = 300;
//			} else if (start == end + 2) {
//				d_point = 600;
//			} else if (start == end + 3) {
//				d_point = 1000;
//			}
//		} else {
//			if (start == end) {
//				d_point = -10;
//			} else if (start == end + 1) {
//				d_point = 0;
//			} else if (start == end + 2) {
//				d_point = 10;
//			} else if (start == end + 3) {
//				d_point = 20;
//			}
//		}
//		// level up
//		if (d_point > 0) {
//			// set display point get effect count
//			if ((point + d_point >= 5500) && (level < 12)) {
//				level = 12;
//				time = 4;
//			} else if ((point + d_point >= 5000) && (level < 11)) {
//				level = 11;
//				time = 4;
//			} else if ((point + d_point >= 4500) && (level < 10)) {
//				level = 10;
//				time = 4;
//			} else if ((point + d_point >= 4000) && (level < 9)) {
//				level = 9;
//				time = 4;
//			} else if ((point + d_point >= 3500) && (level < 8)) {
//				level = 8;
//				time = 5;
//			} else if ((point + d_point >= 3000) && (level < 7)) {
//				level = 7;
//				time = 6;
//				// set new answer
//				ans_data = (int) (Math.random() * 4) + 8;
//				cand_alp_len = cand_ans_len[ans_data];
//				ans_len = cand_alp_len;
//				for (int i = 0; i < cand_alp_len; i++) {
//					cand_alp[i] = cand_ans[ans_data][i];
//					ans[i] = cand_ans[ans_data][i];
//				}
//			} else if ((point + d_point >= 2500) && (level < 6)) {
//				level = 6;
//				time = 6;
//			} else if ((point + d_point >= 2000) && (level < 5)) {
//				level = 5;
//				time = 7;
//			} else if ((point + d_point >= 1500) && (level < 4)) {
//				level = 4;
//				time = 8;
//				// set new answer
//				ans_data = (int) (Math.random() * 4) + 4;
//				cand_alp_len = cand_ans_len[ans_data];
//				ans_len = cand_alp_len;
//				for (int i = 0; i < cand_alp_len; i++) {
//					cand_alp[i] = cand_ans[ans_data][i];
//					ans[i] = cand_ans[ans_data][i];
//				}
//			} else if ((point + d_point >= 1000) && (level < 3)) {
//				level = 3;
//				time = 8;
//			} else if ((point + d_point >= 500) && (level < 2)) {
//				level = 2;
//				time = 9;
//			} else if ((level == 1) && (point + d_point >= 0)) {
//				level = 1;
//				time = 10;
//			}
//			dp_display_count = 10;
//		} else if (d_point < 0) {
//			if (point + d_point <= (level - 1) * 500 - 300) {
//				time = 2;
//			} else if (point + d_point <= (level - 1) * 500 - 200) {
//				time = Math.min(4, time);
//			} else if (point + d_point <= (level - 1) * 500 - 100) {
//				time = Math.min(6, time);
//			} else if (point + d_point < (level - 1) * 500) {
//				time = Math.min(8, time);
//			}
//			dp_display_count = 10;
//		}
//		point += d_point;
//		// shift lines over i to the below and set that line empty
//		for (int i = start; i > start - end; i--) {
//			for (int j = 0; j < WIDTH; j++) {
//				color[j][i] = color[j][end + (i - start - 1)];
//				color[j][end + (i - start - 1)] = NO;
//				alphabet[j][i] = alphabet[j][end + (i - start - 1)];
//				alphabet[j][end + (i - start - 1)] = -1;
//				state[j][i] = state[j][end + (i - start - 1)];
//				state[j][end + (i - start - 1)] = NO;
//			}
//		}
//	}
//
//	/***** special effect *****/
//	// running comments setting
//	private void comment_set() {
//		// run count reset
//		if (run_count > 400) {
//			run_count = 0;
//		}
//		// comment set according to effects
//		if (pause != 0 && pause_count > 800) {
//			if (Math.random() < 0.02) {
//				runcmt_no[run_count] = (int) (Math.random() * 4) + 28;
//				runcmt_x[run_count] = Width;
//				runcmt_y[run_count] = CMT_INT * (1 + run_count % 15);
//				if (Math.random() < 0.02) {
//					runcmt_speed[run_count] = -comment[runcmt_no[run_count]]
//							.length() / 2 - 10;
//				} else {
//					runcmt_speed[run_count] = comment[runcmt_no[run_count]]
//							.length() / 2 + 10;
//				}
//				run_count++;
//			}
//		} else if (line_num > 0) {
//			if (d_point < 0) {
//				for (int i = 0; i < 15; i++) {
//					runcmt_no[run_count] = (int) (Math.random() * 4);
//					runcmt_x[run_count] = Width + i * 10;
//					runcmt_y[run_count] = CMT_INT * (1 + run_count % 15);
//					if (Math.random() < 0.02) {
//						runcmt_speed[run_count] = -comment[runcmt_no[run_count]]
//								.length() / 2 - 10;
//					} else {
//						runcmt_speed[run_count] = comment[runcmt_no[run_count]]
//								.length() / 2 + 10;
//					}
//					run_count++;
//				}
//			} else if (d_point > 800) {
//				for (int i = 0; i < 50; i++) {
//					runcmt_no[run_count] = (int) (Math.random() * 4) + 4;
//					runcmt_x[run_count] = Width + i * 10;
//					runcmt_y[run_count] = CMT_INT * (1 + run_count % 15);
//					if (Math.random() < 0.02) {
//						runcmt_speed[run_count] = -comment[runcmt_no[run_count]]
//								.length() / 2 - 10;
//					} else {
//						runcmt_speed[run_count] = comment[runcmt_no[run_count]]
//								.length() / 2 + 10;
//					}
//					run_count++;
//				}
//			} else if (d_point > 250) {
//				for (int i = 0; i < 30; i++) {
//					runcmt_no[run_count] = (int) (Math.random() * 4) + 8;
//					runcmt_x[run_count] = Width + i * 10;
//					runcmt_y[run_count] = CMT_INT * (1 + run_count % 15);
//					if (Math.random() < 0.02) {
//						runcmt_speed[run_count] = -comment[runcmt_no[run_count]]
//								.length() / 2 - 10;
//					} else {
//						runcmt_speed[run_count] = comment[runcmt_no[run_count]]
//								.length() / 2 + 10;
//					}
//					run_count++;
//				}
//			} else if (d_point > 50) {
//				for (int i = 0; i < 15; i++) {
//					runcmt_no[run_count] = (int) (Math.random() * 4) + 12;
//					runcmt_x[run_count] = Width + i * 10;
//					runcmt_y[run_count] = CMT_INT * (1 + run_count % 15);
//					if (Math.random() < 0.02) {
//						runcmt_speed[run_count] = -comment[runcmt_no[run_count]]
//								.length() / 2 - 10;
//					} else {
//						runcmt_speed[run_count] = comment[runcmt_no[run_count]]
//								.length() / 2 + 10;
//					}
//					run_count++;
//				}
//			} else if (d_point > 0) {
//				for (int i = 0; i < 15; i++) {
//					runcmt_no[run_count] = (int) (Math.random() * 4) + 16;
//					runcmt_x[run_count] = Width + i * 10;
//					runcmt_y[run_count] = CMT_INT * (1 + run_count % 15);
//					if (Math.random() < 0.02) {
//						runcmt_speed[run_count] = -comment[runcmt_no[run_count]]
//								.length() / 2 - 10;
//					} else {
//						runcmt_speed[run_count] = comment[runcmt_no[run_count]]
//								.length() / 2 + 10;
//					}
//					run_count++;
//				}
//			}
//		} else if (gameover_flag == 1) {
//			// set looping times
//			int loop = 15;
//			if (point >= 500) {
//				loop += point / 100;
//			} else if (point < 100) {
//				loop = 50;
//			}
//			for (int i = 0; i < loop; i++) {
//				if (Math.random() < 0.01) {
//					runcmt_no[run_count] = (int) (Math.random() * 4) + 24;
//					runcmt_x[run_count] = Width + i * 10;
//					runcmt_y[run_count] = CMT_INT * (1 + run_count % 15);
//					if (Math.random() < 0.02) {
//						runcmt_speed[run_count] = -comment[runcmt_no[run_count]]
//								.length() / 2 - 10;
//					} else {
//						runcmt_speed[run_count] = comment[runcmt_no[run_count]]
//								.length() / 2 + 10;
//					}
//					run_count++;
//				}
//			}
//		} else {
//			for (int i = 2; i < 6; i++) {
//				for (int j = 1; j < 7; j++) {
//					if (state[i][j] == STABLE && Math.random() < 0.001 * j) {
//						runcmt_no[run_count] = (int) (Math.random() * 4) + 20;
//						runcmt_x[run_count] = Width + i * 10;
//						runcmt_y[run_count] = CMT_INT * (1 + run_count % 15);
//						if (Math.random() < 0.02) {
//							runcmt_speed[run_count] = -comment[runcmt_no[run_count]]
//									.length() / 2 - 10;
//						} else {
//							runcmt_speed[run_count] = comment[runcmt_no[run_count]]
//									.length() / 2 + 10;
//						}
//						run_count++;
//					}
//				}
//			}
//			if (Math.random() < 0.005) {
//				runcmt_no[run_count] = (int) (Math.random() * 28);
//				runcmt_x[run_count] = Width;
//				runcmt_y[run_count] = CMT_INT * (1 + run_count % 15);
//				if (Math.random() < 0.02) {
//					runcmt_speed[run_count] = -comment[runcmt_no[run_count]]
//							.length() / 2 - 10;
//				} else {
//					runcmt_speed[run_count] = comment[runcmt_no[run_count]]
//							.length() / 2 + 10;
//				}
//				run_count++;
//			}
//		}
//	}
//
//	private void comment_set_ver2() {
//		for (int i = 0; i < CMT_ROW; i++) {
//			if (cmt_no[i][0] != -1
//					&& runcmt_count[cmt_no[i][0]] >= CMT_MAXCOUNT) {
//				cmt_no[i][0] = cmt_no[i][1];
//			}
//			if (cmt_no[i][1] != -1) {
//				if (cmt_no[i][1] == cmt_no[i][0]
//						&& runcmt_count[cmt_no[i][1]] >= CMT_MAXCOUNT / 2) {
//					cmt_no[i][1] = -1;
//				}
//			}
//		}
//
//		// run count reset
//		if (run_count >= 100) {
//			run_count = 0;
//		}
//		// comment set according to effects
//		if (pause != 0 && pause_count > 800) {
//			if (Math.random() < 0.02) {
//				onecomment_set(28, 4);
//			}
//		} else if (line_num > 0) {
//			int num;
//			if (d_point < 0) {
//				num = 10 + (int) (Math.random() * 10);
//				for (int i = 0; i < num; i++) {
//					onecomment_set(0, 4);
//				}
//			} else if (d_point > 800) {
//				num = 20 + (int) (Math.random() * 10);
//				for (int i = 0; i < num; i++) {
//					onecomment_set(4, 8);
//				}
//			} else if (d_point > 250) {
//				num = 15 + (int) (Math.random() * 10);
//				for (int i = 0; i < num; i++) {
//					onecomment_set(4, 12);
//				}
//			} else if (d_point > 50) {
//				num = 10 + (int) (Math.random() * 10);
//				for (int i = 0; i < num; i++) {
//					onecomment_set(8, 12);
//				}
//			} else if (d_point > 0) {
//				num = 10 + (int) (Math.random() * 10);
//				for (int i = 0; i < num; i++) {
//					onecomment_set(12, 8);
//				}
//			}
//		} else if (gameover_flag == 1) {
//			// set looping times
//			int num = 15 + (int) (Math.random() * 10);
//			if (point >= 500) {
//				num += point / 100;
//			} else if (point < 100) {
//				num += 25;
//			}
//			for (int i = 0; i < num; i++) {
//				if (Math.random() < 0.01) {
//					onecomment_set(24, 4);
//				}
//			}
//		} else {
//			for (int i = 2; i < 6; i++) {
//				for (int j = 1; j < 7; j++) {
//					if (state[i][j] == STABLE && Math.random() < 0.002 * j) {
//						onecomment_set(20, 4);
//					}
//				}
//			}
//			if (Math.random() < 0.01) {
//				onecomment_set(0, 28);
//			}
//		}
//	}
//
//	private void onecomment_set(int start, int range) {
//		for (int i = 0; i < CMT_ROW; i++) {
//			if (cmt_no[i][1] == -1) {
//				cmt_no[i][1] = run_count;
//				if (cmt_no[i][0] == -1) {
//					cmt_no[i][0] = run_count;
//				}
//				runcmt_no[cmt_no[i][1]] = start + (int) (Math.random() * range);
//				runcmt_count[cmt_no[i][1]] = 0;
//				if (Math.random() < 0.04) {
//					runcmt_x[cmt_no[i][1]] = Width / 2
//							- comment[runcmt_no[cmt_no[i][1]]].length()
//							* CMT_TXTST / 2;
//					runcmt_y[cmt_no[i][1]] = CMT_INT * (1 + i % (CMT_ROW / 2))
//							+ CMT_INT / 2 * (i / (CMT_ROW / 2));
//					runcmt_speed[cmt_no[i][1]] = 0;
//				} else {
//					runcmt_x[cmt_no[i][1]] = Width;
//					runcmt_y[cmt_no[i][1]] = CMT_INT * (1 + i % (CMT_ROW / 2))
//							+ CMT_INT / 2 * (i / (CMT_ROW / 2));
//					runcmt_speed[cmt_no[i][1]] = (Width + comment[runcmt_no[cmt_no[i][1]]]
//							.length() * (CMT_TXTMV - 5))
//							/ (CMT_MAXCOUNT - 2);
//				}
//				run_count++;
//				break;
//			}
//		}
//	}
//
//	// pop up strings setting
//	private void string_set() {
//		// new string
//		if (string_last_no < 5 && Math.random() < 0.05 - 0.002 * string_last_no) {
//			string_no[string_last_no] = (int) (26 * Math.random());
//			string_x[string_last_no] = (int) (400 * Math.random());
//			string_y[string_last_no] = (int) (600 * Math.random());
//			string_color[string_last_no] = (int) (7 * Math.random()) + 1;
//			string_count[string_last_no] = 0;
//
//			string_last_no++;
//		}
//
//		// pop
//		if (string_count[0] >= 20) {
//			string_last_no--;
//
//			for (int i = 0; i < string_last_no; i++) {
//				string_no[i] = string_no[i + 1];
//				string_x[i] = string_x[i + 1];
//				string_y[i] = string_y[i + 1];
//				string_color[i] = string_color[i + 1];
//				string_count[i] = string_count[i + 1];
//			}
//
//			string_no[string_last_no] = 0;
//			string_x[string_last_no] = 0;
//			string_y[string_last_no] = 0;
//			string_color[string_last_no] = 0;
//			string_count[string_last_no] = 0;
//		}
//
//		// update
//		for (int i = 0; i < string_last_no; i++) {
//			string_count[i]++;
//		}
//	}
//
//	// run arrows setting
//	private void arrow_set() {
//		// new arrow
//		if (arrow_last_no < 15 && Math.random() < 0.20 - 0.004 * arrow_last_no) {
//			arrow_no[arrow_last_no] = (int) (4 * Math.random());
//			arrow_x[arrow_last_no] = (int) (375 * Math.random()) + 25;
//			arrow_y[arrow_last_no] = 760;
//			arrow_size[arrow_last_no] = ((int) (5 * Math.random()) + 4) * 20;
//			arrow_color[arrow_last_no] = (int) (7 * Math.random()) + 1;
//			arrow_count[arrow_last_no] = 0;
//
//			arrow_last_no++;
//		}
//
//		// pop
//		for (int i = 0; i < arrow_last_no; i++) {
//			if (arrow_y[i] < -20) {
//				arrow_last_no--;
//
//				for (int j = i; j < arrow_last_no; j++) {
//					arrow_no[j] = arrow_no[j + 1];
//					arrow_x[j] = arrow_x[j + 1];
//					arrow_y[j] = arrow_y[j + 1];
//					arrow_size[j] = arrow_size[j + 1];
//					arrow_color[j] = arrow_color[j + 1];
//					arrow_count[j] = arrow_count[j + 1];
//				}
//
//				arrow_no[arrow_last_no] = 0;
//				arrow_x[arrow_last_no] = 0;
//				arrow_y[arrow_last_no] = 0;
//				arrow_size[arrow_last_no] = 0;
//				arrow_color[arrow_last_no] = 0;
//				arrow_count[arrow_last_no] = 0;
//			}
//		}
//
//		// update
//		for (int i = 0; i < arrow_last_no; i++) {
//			arrow_count[i]++;
//			arrow_y[i] -= arrow_size[i] / 3;
//		}
//	}
//
//	// set move background array
//	public void movebg_set() {
//		int array[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
//		for (int i = 0; i < 100; i++) {
//			int no1 = (int) (Math.random() * 16);
//			int no2 = (int) (Math.random() * 16);
//			int temp = array[no1];
//			array[no1] = array[no2];
//			array[no2] = temp;
//		}
//		movebg_count = 0;
//		for (int i = 0; i < 16; i++) {
//			movebg_no[i] = array[i];
//		}
//	}
//
//	// set special options
//	private void set_medium() {
//		for (int j = 0; j < WIDTH; j++) {
//			for (int k = 12; k < HEIGHT; k++) {
//				if ((j + k + 1) % 10 != 0) {
//					alphabet[j][k] = 29;
//					color[j][k] = COLOR[Re];
//					state[j][k] = STABLE;
//				} else {
//					alphabet[j][k] = 0;
//					color[j][k] = NO;
//					state[j][k] = NO;
//				}
//			}
//		}
//	}
//
//	private void set_hard() {
//		for (int j = 0; j < WIDTH; j++) {
//			for (int k = 10; k < HEIGHT; k++) {
//				if ((j + k) % 2 == 0) {
//					alphabet[j][k] = 29;
//					color[j][k] = COLOR[Re];
//					state[j][k] = STABLE;
//				} else {
//					alphabet[j][k] = 0;
//					color[j][k] = NO;
//					state[j][k] = NO;
//				}
//			}
//		}
//	}
//
//	private void set_hell(int id) {
//		int array[][] = new int[HEIGHT][WIDTH];
//		switch (id) {
//		case 0:
//			array = HELL_SUN;
//			break;
//		case 1:
//			array = HELL_MOON;
//			break;
//		case 2:
//			array = HELL_RECT;
//			break;
//		case 3:
//			array = HELL_HELL;
//			break;
//		}
//		for (int j = 0; j < WIDTH; j++) {
//			for (int k = 0; k < HEIGHT; k++) {
//				if (array[k][j] == 1) {
//					alphabet[j][k] = 28;
//					color[j][k] = COLOR[Re];
//					state[j][k] = STABLE;
//				} else if (array[k][j] == 2) {
//					alphabet[j][k] = 26;
//					color[j][k] = COLOR[Re];
//					state[j][k] = STABLE;
//				} else if (array[k][j] == 3) {
//					alphabet[j][k] = 27;
//					color[j][k] = COLOR[Re];
//					state[j][k] = STABLE;
//				} else {
//					alphabet[j][k] = 0;
//					color[j][k] = NO;
//					state[j][k] = NO;
//				}
//			}
//		}
//	}
//
//	/***** sound player *****/
//	public void soundplay(int soundid) {
//		if (sound_flag == 1) {
//			soundpool
//					.play(si[soundid], (float) volume, (float) volume, 1, 0, 1);
//		}
//	}
//
//	/***** canvas surface methods *****/
//	@Override
//	public void surfaceChanged(SurfaceHolder holder, int format, int width,
//			int height) {
//		// SurfaceViewのサイズなどが変更されたときに呼び出されるメソッド。
//		update_xy();
//		Log.d("SampleSurView", "surfaceChanged called.");
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder holder) {
//		// SurfaceViewが最初に生成されたときに呼び出されるメソッド
//		Log.d("SampleSurView", "surfaceCreated called.");
//
//		// SurfaceHolderからCanvasのインスタンスを取得する
//		Canvas canvas = holder.lockCanvas();
//
//		// 描画が終わったら呼び出すメソッド。
//		holder.unlockCanvasAndPost(canvas);
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		// SurfaceViewが破棄されるときに呼び出されるメソッド
//		Log.d("SampleSurView", "surfaceDestroyed");
//	}
//
//}