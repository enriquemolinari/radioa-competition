package ar.cpfw.book.competition.model.api;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RadioCompetition {

 int id();

 String description();

 String rules();

 LocalDateTime startDate();

 LocalDateTime inscriptionStartDate();

 LocalDateTime inscriptionEndDate();

 default int pointsForCompetitor() {
  if (inscriptionStartDate().toLocalDate().equals(LocalDate.now()))
   return 10;
  return 0;
 }
}
