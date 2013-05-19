package com.jpapps.dragonfly;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.ClipData;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainDragonflyActivity extends Activity implements OnTouchListener {
	
	int rows = 7;
	int cols = 5; 
	
	boolean dragging = false;
	FrameLayout frameLayout;
	
	static final int type_girl = 0;
	static final int type_leaf = 1;
	static final int type_stump = 2;
	static final int type_exit = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_dragonfly);
		
		frameLayout = (FrameLayout) this.findViewById(R.id.main_frames);
		
		TableLayout mapTable = (TableLayout) this.findViewById(R.id.gameMapTable);
		
		int cellWidthPix = (int) (this.getResources().getDisplayMetrics().widthPixels / cols + 0.5f);
		
		int[][] mapArray = generateMapArray(rows, cols);
		
		for(int[] row : mapArray) {
			TableRow tableRow = new TableRow(this);
			mapTable.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));	
			for(int cell : row) {
				ImageView cellView = new ImageView(this);
				
				if(cell == type_girl) {
					cellView.setTag(type_girl);
					cellView.setImageResource(R.drawable.girl1);
				} else if(cell == type_leaf) {
					cellView.setTag(type_leaf);
					cellView.setImageResource(R.drawable.leaf1);
				} else if(cell == type_stump) {
					cellView.setTag(type_stump);
					cellView.setImageResource(R.drawable.stump1);
				} else if(cell == type_exit) {
					cellView.setTag(type_exit);
					cellView.setImageResource(R.drawable.exit1);
				}
				
				cellView.setOnTouchListener(this);
				
				tableRow.addView(cellView, new TableRow.LayoutParams(cellWidthPix, cellWidthPix));
				Log.w("Dragonfly", "ID = " + cellView.getId());
			}
		}
	}
	
	private int[][] generateMapArray(int rows, int cols) {
		int[][] newMapArray = new int[rows][cols];
		
		Random rando = new Random();
		
		int totalCells = rows*cols;
		int girlCell = rando.nextInt(totalCells);
		int exitCell = girlCell;
		while(exitCell == girlCell) {
			exitCell = rando.nextInt(totalCells);
		}
		
		int count = 0;
		for (int[] row : newMapArray) {
			for (int c=0; c<row.length; c++) {
				if(count == girlCell) {
					row[c] = type_girl;
				} else if(count == exitCell) {
					row[c] = type_exit;
				} else {
					if(rando.nextDouble() < 0.6) {
						row[c] = type_leaf;
					} else {
						row[c] = type_stump;
					}
				}
				count++;
			}
		}
		return newMapArray;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_dragonfly, menu);
		return true;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if(view.getTag().equals(type_leaf)) {
			if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				dragging = true;
				return true;
			} else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
				dragging = false;
				return false;
			} else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				if(dragging) {
					view.setX(motionEvent.getX());
					view.setY(motionEvent.getY());
				}
				return true;
			}
		}
		return false;
	}

}
