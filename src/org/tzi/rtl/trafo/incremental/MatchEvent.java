package org.tzi.rtl.trafo.incremental;

public class MatchEvent {
	private boolean running = false;
	public MatchEvent(boolean running) {
		this.running = running;
	}
	public boolean isRunning() {
		return running;
	}
}
