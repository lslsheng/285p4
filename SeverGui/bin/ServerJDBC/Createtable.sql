CREATE TABLE TORRENTINFO
(
	IP CHAR(20) NOT NULL,
	PORT CHAR(10) NOT NULL,
	TORRENTNAME CHAR(20) NOT NULL,
	STATUS CHAR(4)  NOT NULL,
	CONSTRAINT PEERINFO PRIMARY KEY (IP,PORT, TORRENTNAME)
)