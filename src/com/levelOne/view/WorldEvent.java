package com.levelOne.view;

import com.levelOne.WorldEngine;

public interface WorldEvent {
	public void onReady(WorldEngine world);
	public void onDefeat();
	public void onVictory();
}
