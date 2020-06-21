CREATE TABLE competitor (
   id_listener INTEGER,
   points INTEGER,
   PRIMARY KEY (id_listener)
);

CREATE TABLE competition (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   description VARCHAR(255),
   rules VARCHAR(1500),
   start_date timestamp,
   inscription_start_date timestamp,
   inscription_end_date timestamp,
   PRIMARY KEY (id)
);

CREATE TABLE inscription (
   id_competition INTEGER references competition(id),
   id_listener INTEGER references competitor(id_listener),
   inscription_date timestamp
);

insert into 
	competition(description, rules, start_date, inscription_start_date, inscription_end_date)
	values('default competition...', 'rules', '2020-09-01 00:00:00', '2020-06-01 00:00:00', '2020-08-30 00:00:00')
	