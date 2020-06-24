module radioa.competition {

 exports competition.model.api;
 
 //only for injection
 exports competition.model to radioa.main;
 exports competition.persistence to radioa.main;
 
 requires java.sql;
 requires org.apache.derby.client;
 requires org.apache.derby.tools;
}