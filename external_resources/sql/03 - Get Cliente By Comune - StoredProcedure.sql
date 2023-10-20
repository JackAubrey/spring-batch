CREATE DEFINER=`root`@`%` PROCEDURE `GetCustomerByComune`(
	IN comune VARCHAR(50)
)
IF comune != '' AND comune is not null THEN
	BEGIN
		SELECT c.id, c.codfid, c.nominativo, c.comune, c.stato, c.bollini FROM ClientiDataSet.CLIENTI as c 
		where UPPER(c.comune) = UPPER(comune);
	END;
ELSE
	BEGIN
		SELECT c.id, c.codfid, c.nominativo, c.comune, c.stato, c.bollini FROM ClientiDataSet.CLIENTI as c;
	END;
END IF