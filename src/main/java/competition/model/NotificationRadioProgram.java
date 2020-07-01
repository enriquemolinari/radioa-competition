package competition.model;

import competition.model.api.Notification;
import competition.model.api.RadioCompetition;
import competition.model.api.RadioProgram;

public class NotificationRadioProgram implements RadioProgram {

 private RadioProgram radioProgram;
 private Notification notification;

 public NotificationRadioProgram(RadioProgram radioProgram,
   Notification notification) {
  this.radioProgram = radioProgram;
  this.notification = notification;
 }

 @Override
 public Iterable<RadioCompetition> availableCompetitions() {
  return this.radioProgram.availableCompetitions();
 }

 @Override
 public void addInscription(int idCompetition, int idCompetitor) {
  this.radioProgram.addInscription(idCompetition, idCompetitor);
  notification.send(idCompetitor, "Inscription done succesfully");
 }
}
