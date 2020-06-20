package ar.cpfw.book.competition.model;

import java.util.List;
import java.util.Optional;

import ar.cpfw.book.competition.model.api.RadioCompetition;

public interface CompetitionRepository {

	Optional<RadioCompetition> competitionBy(int id);
	
	void addInscription(int idCompetitor, int idCompetition, int additionalPointsForCompetitor)
			throws RadioException;

	List<RadioCompetition> competitionsForInscription()
			throws RadioException;
}
