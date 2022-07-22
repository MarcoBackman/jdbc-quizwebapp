#CREATE DATABASE IF NOT EXISTS quiz_website;
USE quiz_website;
SET SQL_SAFE_UPDATES = 0;

/* Danger zone
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS QuizLog;
DROP TABLE IF EXISTS UserSubmission;
DROP TABLE IF EXISTS FeedBack;
DROP TABLE IF EXISTS Quiz;
DROP TABLE IF EXISTS MultipleQuestionSet;
DROP TABLE IF EXISTS QuestionOption;
DROP TABLE IF EXISTS ShortQuestion;
DROP TABLE IF EXISTS QuizType;
#ALTER TABLE Quiz
#ADD user_answer VARCHAR(100);
*/

#Create tables
CREATE TABLE IF NOT EXISTS User (
	user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE KEY NOT NULL,
    user_pw VARCHAR(150) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE NOT NULL,
    is_suspended BOOLEAN DEFAULT FALSE NOT NULL,
    has_rated BOOLEAN DEFAULT FALSE, #Only one user can feedback once
    email VARCHAR(50) NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS FeedBack (
	feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    rate FLOAT DEFAULT 0.0,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    content VARCHAR(300) 
);


CREATE TABLE IF NOT EXISTS QuizLog (
	log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    time_start VARCHAR(100) NOT NULL,
    time_end VARCHAR(100) NOT NULL,
    quiz_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Quiz (
	quiz_id INT AUTO_INCREMENT PRIMARY KEY,
    quiz_type INT,
    score INT
);

CREATE TABLE IF NOT EXISTS Question (
	question_id INT AUTO_INCREMENT PRIMARY KEY,
    question_content VARCHAR(500) NOT NULL,
    short_question_answer VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    is_short_qestion BOOLEAN NOT NULL,
    quiz_type INT
);

CREATE TABLE IF NOT EXISTS QuestionOption (
	option_id INT AUTO_INCREMENT PRIMARY KEY,
    option_content VARCHAR(200) NOT NULL,
    is_answer BOOLEAN,
    question_id INT
);

CREATE TABLE IF NOT EXISTS UserQuestion (
	user_question_id INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id INT NOT NULL,
    question_id INT NOT NULL,
    user_answer VARCHAR(100),
    selected_option_id INT,
    is_short_question BOOLEAN NOT NULL
);

#DROP TABLE IF EXISTS QuizType;
CREATE TABLE IF NOT EXISTS QuizType (
	quiz_type INT AUTO_INCREMENT PRIMARY KEY,
    type_detail VARCHAR(20) NOT NULL UNIQUE KEY #Name must be unique
);

#Add admin manually
INSERT INTO User (user_name, user_pw, is_admin, email, first_name, last_name) VALUES 
('roni2006', '1234', TRUE, 'sbaek2015@my.fit.edu', 'Tony', 'Baek');

#Add quiz types manually
INSERT INTO QuizType (type_detail) VALUES ("Math"), ("Java"), ("Science");

#ALTER TABLE User AUTO_INCREMENT=2;
SELECT * FROM User;
SELECT * FROM QuizType;
SELECT * FROM Feedback;
SELECT * FROM Ustudentser;
#ALTER TABLE Quiz RENAME COLUMN socre TO score;
SELECT * FROM Quiz;
#truncate table Quiz;
SELECT * FROM QuizLog;
#truncate table QuizLog;
SELECT * FROM UserQuestion;
SELECT * FROM Question;
SELECT * FROM QuestionOption;

truncate table Quiz;
truncate table UserQuestion;
truncate table QuizLog;

SELECT * FROM Question;
SELECT * FROM QuestionOption GROUP BY question_id;
UPDATE Question SET is_active = TRUE WHERE question_id = 8;
#truncate table Question;
#truncate table QuestionOption;
#DELETE FROM QUESTION WHERE question_id >= 4 and question_id <= 50000;
#ALTER TABLE Question DROP COLUMN is_short_qestion;
#ALTER TABLE Question ADD COLUMN is_short_question BOOLEAN;
#ALTER TABLE Question DROP COLUMN short_;
#DELETE FROM QuestionOption WHERE question_id = 4;

