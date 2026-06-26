package com.entity;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

// ################## INHERITANCE STRATEGIES #######################

/* (1.) ============ MappedSuperclass ==================
 *
 * Inheritance is implemented in the domain model only without reflecting it in
 * the database schema.
 *
 * Use @javax.persistence.MappedSuperclass annotation on the base class.
 *
 * Using @javax.persistence.Entity annotation in addition to above, on the base
 * class, we also get a DB table corresponding to the base class, but no data
 * gets inserted in this table.
 *
 * To be able to use Polymorphic queries, the base class needs to have the
 * @Entity annotation.
 */
// @MappedSuperclass
// ------------------------------------------

/* (2.) ============ Single table ==================
 *
 * The domain model class hierarchy is materialized into a single table which
 * contains entities belonging to different class types.
 *
 * Use @Inheritance(strategy = InheritanceType.SINGLE_TABLE) annotation along
 * with @Entity annotation (from javax.persistence package); on the base class.
 */
// @Entity
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//------------------------------------------

/* (3.) ============ Joined table ==================
 *
 * The base class and all the subclasses have their own database tables and
 * fetching a subclass entity requires a join with the parent table as well.
 *
 * Use @Inheritance(strategy = InheritanceType.JOINED) annotation along
 * with @Entity annotation (from javax.persistence package); on the base class.
 */
// @Entity
// @Inheritance(strategy = InheritanceType.JOINED)
//------------------------------------------

/* (4.) ============ Table per class ==================
 *
 * Each subclass has its own table containing both the subclass and the base
 * class properties.
 *
 * Either use (on the base class):
 * @Entity
 * @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
 * .. Both annotations when you need to use Polymorphic Queries, and we know
 * @Entity annotation will create table for the base class as well.
 *
 * Or use (on the base class):
 * @MappedSuperclass
 * @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
 * .. Replace @Entity with @MappedSuperclass to create only tables for the
 * subclasses (not the base class).
 */
// @Entity
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

// Or:
// @MappedSuperclass
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//------------------------------------------

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Player {

	@Id
	private int id;

	private String name;

	public Player() {
		super();
	}

	public Player(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + "]";
	}

	public String playerString() {
		return toString();
	}

}
