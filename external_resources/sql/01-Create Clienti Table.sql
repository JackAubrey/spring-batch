CREATE TABLE ClientiDataSet.CLIENTI (
  `id` int NOT NULL AUTO_INCREMENT,
  `CodFid` varchar(45) DEFAULT NULL,
  `Nominativo` varchar(45) DEFAULT NULL,
  `Comune` varchar(45) DEFAULT NULL,
  `Stato` int DEFAULT NULL,
  `Bollini` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=UTF8MB4;