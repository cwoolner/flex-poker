CREATE TABLE `cardRanks` (
  `id` int(11) NOT NULL auto_increment,
  `rank` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB;

CREATE TABLE `cardSuits` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB;

CREATE TABLE `cards` (
  `id` int(11) NOT NULL auto_increment,
  `image` varchar(255) default NULL,
  `cardRankId` int(11) default NULL,
  `cardSuitId` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK5A0E763B45FC5D3` (`cardRankId`),
  KEY `FK5A0E763B72D0F55` (`cardSuitId`)
) ENGINE=InnoDB;

CREATE TABLE `gameEventTypes` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB;

CREATE TABLE `gameEvents` (
  `id` int(11) NOT NULL auto_increment,
  `chips` int(11) default NULL,
  `eventTime` datetime default NULL,
  `money` double default NULL,
  `gameId` int(11) default NULL,
  `gameEventTypeId` int(11) default NULL,
  `userId` int(11) default NULL,
  `card1` int(11) default NULL,
  `card2` int(11) default NULL,
  `card3` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKD9CDBAB5F74C1BF` (`gameEventTypeId`),
  KEY `FKD9CDBAB34835311` (`userId`),
  KEY `FKD9CDBAB1B9EA59F` (`gameId`),
  KEY `FKD9CDBAB6BF09753` (`card3`),
  KEY `FKD9CDBAB6BF09752` (`card2`),
  KEY `FKD9CDBAB6BF09751` (`card1`)
) ENGINE=InnoDB;

CREATE TABLE `gameStages` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB;

CREATE TABLE `games` (
  `id` int(11) NOT NULL auto_increment,
  `canceledOn` datetime default NULL,
  `createdOn` datetime default NULL,
  `endTime` datetime default NULL,
  `playersRemaining` int(11) default NULL,
  `startTime` datetime default NULL,
  `totalPlayers` int(11) default NULL,
  `createdBy` int(11) default NULL,
  `gameTypeId` int(11) default NULL,
  `winner` int(11) default NULL,
  `maximumPlayers` int(11) default NULL,
  `gameStageId` int(11) default NULL,
  `allowRebuys` bit(1) default NULL,
  `minimumPlayers` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK5D932C13764212A` (`winner`),
  KEY `FK5D932C14ECE6FB3` (`gameTypeId`),
  KEY `FK5D932C18A02922A` (`createdBy`),
  KEY `FK5D932C14CADF573` (`gameStageId`)
) ENGINE=InnoDB;

CREATE TABLE `seats` (
  `id` int(11) NOT NULL auto_increment,
  `position` int(11) default NULL,
  `tableId` int(11) default NULL,
  `userId` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK683F30E8063817` (`tableId`),
  KEY `FK683F30E34835311` (`userId`)
) ENGINE=InnoDB;

CREATE TABLE `tables` (
  `id` int(11) NOT NULL auto_increment,
  `gameId` int(11) default NULL,
  `bigBlindId` int(11) default NULL,
  `buttonId` int(11) default NULL,
  `smallBlindId` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKCB773E651B9EA59F` (`gameId`),
  KEY `FKCB773E65244C32F2` (`buttonId`),
  KEY `FKCB773E6584882155` (`bigBlindId`),
  KEY `FKCB773E652E9F436E` (`smallBlindId`)
) ENGINE=InnoDB;

CREATE TABLE `userGameStatuses` (
  `id` int(11) NOT NULL auto_increment,
  `chips` int(11) default NULL,
  `enterTime` datetime default NULL,
  `exitTime` datetime default NULL,
  `money` double default NULL,
  `gameId` int(11) default NULL,
  `userId` int(11) default NULL,
  `verified` bit(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK6A46FE3F34835311` (`userId`),
  KEY `FK6A46FE3F1B9EA59F` (`gameId`)
) ENGINE=InnoDB;

CREATE TABLE `users` (
  `id` int(11) NOT NULL auto_increment,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit NOT NULL
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB;

CREATE TABLE `authorities` (
  `username` varchar(255) NOT NULL,
  `authority` varchar(255) NOT NULL
) ENGINE=InnoDB;
