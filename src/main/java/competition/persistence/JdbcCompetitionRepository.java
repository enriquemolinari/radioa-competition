package competition.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import competition.model.api.RadioCompetition;
import competition.model.spi.CompetitionRepository;
import competition.model.spi.RadioException;

public class JdbcCompetitionRepository implements CompetitionRepository {

 private String user;
 private String pwd;
 private String connString;

 public JdbcCompetitionRepository(String user, String pwd, String connString) {
  this.user = user;
  this.pwd = pwd;
  this.connString = connString;
 }

 @Override
 public Optional<RadioCompetition> competitionBy(int id) {

  Connection c = connection();
  try {

   var checkCompetitionSt = c
     .prepareStatement("select id, description, rules"
       + ",start_date, inscription_start_date"
       + ",inscription_end_date from competition " + "where id = ?");

   checkCompetitionSt.setInt(1, id);

   ResultSet resultSet = checkCompetitionSt.executeQuery();

   if (resultSet.next()) {
    return Optional.of(toCompetition(resultSet));
   }

   return Optional.empty();
  } catch (SQLException e) {
   throw new RadioException(e);
  } finally {
   try {
    c.close();
   } catch (SQLException e) {
    throw new RadioException(e);
   }
  }
 }

 @Override
 public void addInscription(int idCompetitor, int idCompetition,
   int addPoints) {

  Connection c = connection();
  try {
   c.setAutoCommit(false);

   var actualPoints = c.prepareStatement(
     "select id_listener, points" + " from competitor " + "where id_listener = ?");

   actualPoints.setInt(1, idCompetitor);

   ResultSet resultSet = actualPoints.executeQuery();

   if (resultSet.next()) {
    addPoints += resultSet.getInt("points");
    
    var st = c.prepareStatement(
      "update competitor set points = ? where id_listener = ?");

    st.setInt(1, addPoints);
    st.setInt(2, idCompetitor);
    st.executeUpdate();
   } else {
    
    var st = c.prepareStatement(
      "insert into competitor(id_listener, points) " + "values(?,?)");

    st.setInt(1, idCompetitor);
    st.setInt(2, addPoints);
    st.executeUpdate();
   }

   PreparedStatement st2 = c.prepareStatement(
     "insert into inscription(id_competition, id_listener, inscription_date) "
       + "values(?,?,?)");

   st2.setInt(1, idCompetition);
   st2.setInt(2, idCompetitor);
   st2.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
   st2.executeUpdate();

   c.commit();
  } catch (Exception e) {
   try {
    c.rollback();
    throw new RadioException(e);
   } catch (SQLException s) {
    throw new RadioException(s);
   }
  } finally {
   try {
    c.setAutoCommit(true);
   } catch (SQLException s) {
    throw new RadioException(s);
   }
  }
 }

 @Override
 public List<RadioCompetition> competitionsForInscription() {
  Connection c = connection();
  try {
   PreparedStatement st = c
     .prepareStatement("select id, description, rules, start_date, "
       + "inscription_start_date, inscription_end_date "
       + "from competition " + "where inscription_start_date <= ? "
       + "and inscription_end_date >= ?");

   st.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
   st.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

   ResultSet resultSet = st.executeQuery();

   var competitions = new ArrayList<RadioCompetition>();

   while (resultSet.next()) {
    competitions.add(toCompetition(resultSet));
   }

   return competitions;
  } catch (SQLException e) {
   throw new RadioException(e);
  } finally {
   try {
    c.close();
   } catch (SQLException e) {
    throw new RadioException(e);
   }
  }
 }

 private RadioCompetition toCompetition(ResultSet resultSet) {
  try {

   int id = resultSet.getInt("id");
   var description = resultSet.getString("description");
   var rules = resultSet.getString("rules");
   var startDate = resultSet.getTimestamp("start_date");
   var inscriptionStartDate = resultSet
     .getTimestamp("inscription_start_date");
   var inscriptionEndDate = resultSet.getTimestamp("inscription_end_date");

   return new RadioCompetition() {
    @Override
    public LocalDateTime startDate() {
     return startDate.toLocalDateTime();
    }

    @Override
    public String rules() {
     return rules;
    }

    @Override
    public LocalDateTime inscriptionStartDate() {
     return inscriptionStartDate.toLocalDateTime();
    }

    @Override
    public LocalDateTime inscriptionEndDate() {
     return inscriptionEndDate.toLocalDateTime();
    }

    @Override
    public int id() {
     return id;
    }

    @Override
    public String description() {
     return description;
    }
   };
  } catch (SQLException e) {
   throw new RadioException(e);
  }
 }

 private Connection connection() {
  String url = this.connString;
  String user = this.user;
  String password = this.pwd;
  try {
   return DriverManager.getConnection(url, user, password);
  } catch (SQLException e) {
   throw new RadioException(e);
  }
 }

}
