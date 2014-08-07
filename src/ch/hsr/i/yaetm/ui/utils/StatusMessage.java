package ch.hsr.i.yaetm.ui.utils;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import org.jdesktop.swingx.JXLabel;

import ch.hsr.i.yaetm.logic.Ersatzteilliste;
import ch.hsr.i.yaetm.logic.Historyliste;
import ch.hsr.i.yaetm.logic.Reservationliste;
import ch.hsr.i.yaetm.ui.main.YAETMMainView;



public class StatusMessage implements Observer {
	
	private JXLabel statusLabel;
	
	public static enum StatusType {
		ADD_ERSATZTEIL, REMOVE_ERSATZTEIL, UPDATE_ERSATZTEIL, ADD_RESERVATION, REMOVE_RESERVATION, UPDATE_RESERVATION, REMOVE_HISTORY
	}
	
	public StatusMessage(JXLabel statusLabel, Ersatzteilliste ersatzteilListe, Reservationliste reservationListe, Historyliste historyListe) {
		this.statusLabel = statusLabel;
		ersatzteilListe.addObserver(this);
		reservationListe.addObserver(this);
		historyListe.addObserver(this);
	}
	
	private static String getMessage(StatusType type) {
		if(type == null) {
			return "YAETMMainView.WELCOME";
		}
		switch (type) {
		case ADD_ERSATZTEIL:
			return "Ersatzteil hinzugefügt...";
		case REMOVE_ERSATZTEIL:
			return "Ersatzteil gelöscht...";
		case UPDATE_ERSATZTEIL:
			return "Ersatzteil aktualisiert...";
		case ADD_RESERVATION:
			return "Reservation hinzugefügt...";
		case REMOVE_RESERVATION:
			return "Reservation gelöscht...";
		case UPDATE_RESERVATION:
			return "Reservation aktualisiert...";
		case REMOVE_HISTORY:
			return "Verlaufeintrag gelöscht...";
		default:
			return "YAETMMainView.WELCOME";
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof StatusType) {
			statusLabel.setForeground(Color.BLACK);
			statusLabel.setText(getMessage((StatusType)arg));
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(4000);
						statusLabel.setForeground(Color.GRAY);
						statusLabel.setText(YAETMMainView.WELCOME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
