# Hibernate Embeddable values

Documentation:

- [Hibernate 5.6 User Guide &sect; 2.4. Embeddable types](https://docs.hibernate.org/orm/5.6/userguide/html_single/#embeddables)
- [Hibernate 6.6 User Guide &sect; 3.3. Embeddable values](https://docs.hibernate.org/orm/6.6/userguide/html_single/#embeddables)
- [Hibernate 7.4 User Guide &sect; 3.3. Embeddable values](https://docs.hibernate.org/orm/7.4/userguide/html_single/#embeddables)

## Example summary

- We only have an `Employee` entity. This entity embeds references to two
  *Embeddable types*.
- `Employee` contains reference to `Car` and `Company` embeddables.
- `Car` only contains basic fields.
- `Company` type in itself embeds a reference to another embeddable type
  called `Account`.

### Code for above Entity class and Embeddable types 

Here's the Entity `Employee`:

```java
@Entity
public class Employee {

	@Id
	private int employeeId;

	private String employeeName;

	@Embedded
	private Company company;

	@Embedded
	private Car car;

}
```
<br>

Embeddable type `Car`:

```java
@Embeddable
public class Car {

	private String carNo;

	private String carModel;

	private LocalDateTime carPurchasedAt;

	private BigDecimal carPurchasePrice;

}
```
<br>

Embeddable type `Company`:

```java
@Embeddable
public class Company {

	private String companyRegistrationNo;

	private String companyName;

	private String companyPolicy;

	@Embedded
	private Account account;

}
```
<br>

Embeddable type `Account`:

```java
@Embeddable
public class Account {

	private String accountNo;

	private String accountHolderName;

	private String accountHolderAddress;

	private String bankName;

	private String branchIfsc;

}
```
<br>


### Generated `employee` table SQL script

```sql
CREATE TABLE `employee` (
  `employeeId` int NOT NULL,
  `carModel` varchar(255) DEFAULT NULL,
  `carNo` varchar(255) DEFAULT NULL,
  `carPurchasePrice` decimal(19,2) DEFAULT NULL,
  `carPurchasedAt` datetime(6) DEFAULT NULL,
  `accountHolderAddress` varchar(255) DEFAULT NULL,
  `accountHolderName` varchar(255) DEFAULT NULL,
  `accountNo` varchar(255) DEFAULT NULL,
  `bankName` varchar(255) DEFAULT NULL,
  `branchIfsc` varchar(255) DEFAULT NULL,
  `companyName` varchar(255) DEFAULT NULL,
  `companyPolicy` varchar(255) DEFAULT NULL,
  `companyRegistrationNo` varchar(255) DEFAULT NULL,
  `employeeName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`employeeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
