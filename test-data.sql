CREATE DATABASE IF NOT EXISTS ch_labs_hibernate_01;

USE ch_labs_hibernate_01;

DROP TABLE IF EXISTS ch_labs_hibernate_01.hbn_employee;

-- Create Table is handled by Hibernate (Might be needed, though, for testing):
CREATE TABLE IF NOT EXISTS `ch_labs_hibernate_01`.`hbn_employee` (
  `employee_id` int NOT NULL,
  `employee_address` varchar(150) DEFAULT NULL,
  `employee_name` varchar(50) NOT NULL,
  `employee_salary` int NOT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO ch_labs_hibernate_01.hbn_employee
	(employee_id,employee_address,employee_name,employee_salary) VALUES
	 (1,'122nd Ave., Compton, CA','Eric Wright',8800),
	 (2,'21st Main, Queens Bridge, NY','Nasir Jones',15000),
	 (3,'Park Hill Projects, Staten Island, NY','Method Man',9100),
	 (4,'Brooklyn, NY','Ol'' Dirty Bastard',10800),
	 (5,'8 Mile Road, Detroit, MI','Slim Shady',9300);
