CREATE DATABASE potato;

CREATE TABLE CrawlData
(
  `id` int(11) NOT NULL AUTO_INCREMENT,	--인덱스
  `date` varchar(50) NULL,	--발견날짜
  `kind` varchar(255) NULL,	--동물 종류
  `variety` varchar(255) NULL,	--동물품종
  `gender` varchar(255) NULL,	--성별
  `place` varchar(255) NULL,	--지역(도)
  `place2` varchar(255) NULL,	--상세지역
  `age` varchar(255) NULL,	--나이
  `phone` varchar(255) NULL,	--보호자 정보
  `img` varchar(255) NULL,	--이미지
  PRIMARY KEY(id)
)ENGINE=InnoDB default character set utf8 collate utf8_general_ci;

CREATE TABLE RegisterData
(
  `id` int(11) NOT NULL AUTO_INCREMENT,	--인덱스
  `date` varchar(50) NULL,	--발견날짜
  `kind` varchar(255) NULL,	--동물 종류
  `variety` varchar(255) NULL,	--동물품종
  `gender` varchar(255) NULL,	--성별
  `place` varchar(255) NULL,	--지역(도)
  `place2` varchar(255) NULL,	--상세지역
  `age` varchar(255) NULL,	--나이
  `phone` varchar(255) NULL,	--보호자 정보
  `img` varchar(255) NULL,	--이미지
  PRIMARY KEY(id)
)ENGINE=InnoDB default character set utf8 collate utf8_general_ci;

CREATE TABLE CaptureImage
(
  `name` varchar(50) NULL,	--입력 이미지 이름
  `place` varchar(255) NULL,	--잃어버린 지역
  `place2` varchar(255) NULL,	--잃어버린 상세지역
  `kind` varchar(255) NULL	--동물 종류
)ENGINE=InnoDB default character set utf8 collate utf8_general_ci;

CREATE TABLE ResultData
(
  `rank` int(11) NOT NULL,	--입력 사진과 일치하는 정도(순위)
  `date` varchar(50) NULL,	--발견날짜
  `kind` varchar(255) NULL,	--동물 종류
  `variety` varchar(255) NULL,	--동물품종
  `gender` varchar(255) NULL,	--성별
  `place` varchar(255) NULL,	--발견지역
  `place2` varchar(255) NULL,	--발견 세부지역
  `phone` varchar(255) NULL,	--보호자 정보
  `img` varchar(255) NULL,	--이미지
  PRIMARY KEY(rank)
)ENGINE=InnoDB default character set utf8 collate utf8_general_ci;

CREATE TABLE WriteData
(
  `id` int(11) NOT NULL AUTO_INCREMENT,	--인덱스
  `name` varchar(50) NULL,	--아이디
  `pw` varchar(50) NULL,		--비밀번호
  `date` varchar(255) NULL,	--작성날짜
  `content` varchar(255) NULL,	--게시글 내용
  `img` varchar(255) NULL,	--첨부 이미지
  `title` varchar(255) NULL,	--게시글 제목
  PRIMARY KEY(id)
)ENGINE=InnoDB default character set utf8 collate utf8_general_ci;