# Hibernate Associations: One To One

Documentation:

- [Hibernate 5.6 User Guide &sect; 2.7. Associations](https://docs.hibernate.org/orm/5.6/userguide/html_single/#associations)


### SQL - CREATE TABLE statements & INSERT statements - for testing

**CREATE TABLE statements:**

```sql
CREATE TABLE `employee` (
  `employee_id` int NOT NULL,
  `employee_designation` varchar(255) DEFAULT NULL,
  `employee_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `address` (
  `address_id` int NOT NULL,
  `address_city` varchar(255) DEFAULT NULL,
  `address_house_no` varchar(255) DEFAULT NULL,
  `address_locality` varchar(255) DEFAULT NULL,
  `address_state` varchar(255) DEFAULT NULL,
  `address_street` varchar(255) DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  PRIMARY KEY (`address_id`),
  UNIQUE KEY `UK_2vodg926m7bltkmjoxwfnh4gb` (`employee_id`),
  CONSTRAINT `fk_employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

**INSERT statements:**

```sql
insert into employee (employee_id, employee_name, employee_designation)
	values
	(1, 'Raju Kumar', 'Sr. IT Admin'),
	(2, 'Raghav Chaddha', 'Jr. Boot Licker');

insert into address
	(address_id, address_house_no, address_street , address_locality, address_city , address_state, employee_id)
	values
	(201, 'D-59/7 First Floor', '1st Main 22nd Cross', 'Kuvempu Nagara 4th Phase', 'Bengaluru', 'Karnataka', 1),
	(202, '119/2, Opp. Raghavendra Temple', '3rd Main 60 Feet Road', 'Dwarka Nagar, Hosakerehalli', 'Bengaluru', 'Karnataka', 2);

```
