package ar.cpfw.book.main;

import ar.cpfw.book.competition.model.DefaultRadioProgram;
import ar.cpfw.book.competition.model.api.RadioCompetition;
import ar.cpfw.book.competition.persistence.JdbcCompetitionRepository;

public class Main {

 public static void main(String[] args) {

  var a = new DefaultRadioProgram(new JdbcCompetitionRepository("app",
    "app", "radiocompetition", "localhost", "1527"));

  var r = a.availableCompetitions();
  for (RadioCompetition radioCompetition : r) {
   System.out.println(radioCompetition.description());
  }

  a.addInscription(2, 2);
 }
}
