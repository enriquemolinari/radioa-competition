package competition.model.api;

public interface RadioProgram {

	Iterable<RadioCompetition> availableCompetitions();

	void addInscription(int idCompetition, int idCompetitor);
}
