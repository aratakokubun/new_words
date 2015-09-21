//package org.example.wordis;
//
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.media.AudioManager;
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.util.Log;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.SeekBar;
//
//import com.kkbnart.wordis.R;
//
//public class Wordis extends Activity {	
//	private static final String FILE_NAME = "scores.txt";
//	
//	CanvasSurfaceView csv;
//	
//    AlertDialog.Builder alertDialogBuilder, alertDialogBuilder_nico;
//    AlertDialog alertDialog, alertDialog_nico;
//	
//    Vibrator vib;
//    private static final int VIB_LENGTH[] = {250, 500};
//    	
//	//sound seek bar
//	public SeekBar seekbar;
//    
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//		//delete title bar
//    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//    	
//    	setVolumeControlStream(AudioManager.STREAM_MUSIC);
//        super.onCreate(savedInstanceState);
//        csv = new CanvasSurfaceView(this);
//        setContentView(csv);
//        
//    	//alert dialog
//        alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder_nico = new AlertDialog.Builder(this);
//        //setting alert dialog title
//        alertDialogBuilder.setTitle("ALERT!");
//        alertDialogBuilder_nico.setTitle("警告!");
//        //setting alert dialog message
//        alertDialogBuilder.setMessage("Really Want To Quit This Game?");
//        alertDialogBuilder_nico.setMessage("マジで止める気？");
//        //setting icon
//        alertDialogBuilder.setIcon(R.drawable.error);
//        alertDialogBuilder_nico.setIcon(R.drawable.error);
//        //register call back listener when pushed "Yes"
//        alertDialogBuilder.setPositiveButton("Yes!",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//        				csv.initialize();
//        				csv.movebg_set();
//        				csv.soundplay(6);
//                    }
//                });
//        alertDialogBuilder_nico.setPositiveButton("男に二言は無い。",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//        				csv.initialize();
//        				csv.movebg_set();
//        				csv.soundplay(6);
//                    }
//                });
//        //register call back listener when pushed "No"
//        alertDialogBuilder.setNegativeButton("No!",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//        				csv.reset_flag = 0;
//        				csv.soundplay(1);
//                    }
//                });
//        alertDialogBuilder_nico.setNegativeButton("そこまで言うなら続けてあげるよ",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//        				csv.reset_flag = 0;
//        				csv.soundplay(1);
//                    }
//                });
//        //set enable or disable of cancel alert
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder_nico.setCancelable(false);
//        alertDialog = alertDialogBuilder.create();
//        alertDialog_nico = alertDialogBuilder_nico.create();
//        
//        //set vibration instance
//        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//        		
//		//seek bar settings
//        /*seekbar.setMax(100);
//        seekbar.setProgress(50);
//		seekbar.setOnSeekBarChangeListener(
//                new OnSeekBarChangeListener() {
//                    public void onProgressChanged(SeekBar seekBar,
//                            int progress, boolean fromUser) {
//                    	volume = (long)(progress/seekBar.getMax());
//                    }
// 
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//                    }
// 
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//                    }
//                }
//        );*/
//    }
//
//    //read from text file
//    public void read(){
//    	try {
//        	FileInputStream fis = openFileInput(FILE_NAME);
//        	InputStreamReader isw = new InputStreamReader(fis);
//    		BufferedReader br = new BufferedReader(isw);
//    		String line;
//    		/*int num = 0;
//    		while ((line = br.readLine()) != null && num < 10){
//        		//StringTokenizer st = new StringTokenizer(line, ",");
//        		//while(st.hasMoreTokens()){};
//    			if(num%2 == 0){
//    				csv.rank[(int)(num/2)] = Integer.parseInt(line);
//    			} else {
//    				csv.rank_line[(int)((num-1)/2)] = Integer.parseInt(line);
//    			}
//    			num++;
//    		}*/
//    		int id = 0;
//    		int no = 0;
//    		for(int i = 0; i < 50; i++){
//    			if((line = br.readLine()) != null){
//    				id = i/10;
//    				no = i - id*10;
//    				if(no%2 == 0){
//    					csv.rank[id][(int)(no/2)] = Integer.parseInt(line);
//    				} else {
//    					csv.rank_line[id][(int)((no-1)/2)] = Integer.parseInt(line);
//    				}
//    			}
//    		}
//    		br.close();
//    	} catch (FileNotFoundException e){
//    	} catch (IOException e){
//    	} catch (NumberFormatException e){
//    		Log.e("Hello!","Hello!");
//    	}
//    }
//    
//    //write to text file
//    public void write(){
//		try {
//        	FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
//        	OutputStreamWriter osw = new OutputStreamWriter(fos);
//			BufferedWriter bw = new BufferedWriter(osw);
//			int id = 0;
//			int no = 0;
//			for(int i = 0; i < 25; i++){
//				id = i/5;
//				no = i - id*5;
//				bw.write(csv.rank[id][no]+"\n");
//				bw.write(csv.rank_line[id][no]+"\n");
//			}
//			bw.flush();
//			
//			bw.close();
//		} catch (FileNotFoundException e){
//		} catch (IOException e) {
//			Log.d("FileWriteActivity", e.getMessage());
//		}
//    }
//    
//    /*//read from CSV file
//    public void readcsv(){
//    	try {
//            File csv = new File("writers.csv"); // CSVデータファイル
//
//            BufferedReader br = new BufferedReader(new FileReader(csv));
//
//            // 最終行まで読み込む
//            String line = "";
//            while ((line = br.readLine()) != null) {
//
//              // 1行をデータの要素に分割
//              StringTokenizer st = new StringTokenizer(line, ",");
//
//              while (st.hasMoreTokens()) {
//                // 1行の各要素をタブ区切りで表示
//                System.out.print(st.nextToken() + "\t");
//              }
//              System.out.println();
//            }
//            br.close();
//
//          } catch (FileNotFoundException e) {
//            // Fileオブジェクト生成時の例外捕捉
//            e.printStackTrace();
//          } catch (IOException e) {
//            // BufferedReaderオブジェクトのクローズ時の例外捕捉
//            e.printStackTrace();
//          }
//        }
//    }
//    
//    //write to CSV file
//    public void writecsv(){
//    	try {
//    		File csv = new File("writers.csv"); // CSVデータファイル
//    		// 追記モード
//    		BufferedWriter bw 
//    		= new BufferedWriter(new FileWriter(csv, true)); 
//    		// 新たなデータ行の追加
//    		bw.write("中上健次" + "," + "1946" + "," + "1992");
//    		bw.newLine();
//    		bw.close();
//    		
//    	} catch (FileNotFoundException e) {
//    		// Fileオブジェクト生成時の例外捕捉
//    		e.printStackTrace();
//    	} catch (IOException e) {
//    		// BufferedWriterオブジェクトのクローズ時の例外捕捉
//    		e.printStackTrace();
//    	}
//    }*/
//
//    public void getss(){
//    	csv.audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//    	csv.volume = csv.audio.getStreamVolume(AudioManager.STREAM_RING);
//    }
//    
//    //display alert dialog
//    public void alert_show(){
//    	if(csv.special == CanvasSurfaceView.SP_NICO){
//    		alertDialog_nico.show();
//    	} else {
//    		alertDialog.show();
//    	}
//    }
//    
//    //vibration enable
//    public void vibration(int mode){
//    	if(csv.vib_flag == 1){
//    		vib.vibrate(VIB_LENGTH[mode]);
//    	}
//    }
//}
