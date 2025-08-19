CREATE TABLE Message(
   Id_Message COUNTER,
   Date_Commencement DATETIME NOT NULL,
   Date_signalement DATETIME NOT NULL,
   contenu_code TEXT,
   PointRepere TEXT,
   Surface_Approximative DECIMAL(15,2),
   Description TEXT,
   Direction VARCHAR(20) NOT NULL,
   Elements_visibles VARCHAR(100),
   Degats VARCHAR(100),
   Id_UserApp INT NOT NULL,
   id_centrale INT NOT NULL,
   PRIMARY KEY(Id_Message),
   UNIQUE(Date_signalement),
   FOREIGN KEY(Id_UserApp) REFERENCES UserApp(Id_UserApp)
);

