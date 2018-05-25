package com.myspace.washit;

/**
 * This class was automatically generated by the data modeler tool.
 */

@javax.persistence.Entity
public class Customer implements java.io.Serializable {
	static final long serialVersionUID = 1L;

	@javax.persistence.GeneratedValue(generator = "CUSTOMER_ID_GENERATOR", strategy = javax.persistence.GenerationType.AUTO)
	@javax.persistence.Id
	@javax.persistence.SequenceGenerator(name = "CUSTOMER_ID_GENERATOR", sequenceName = "CUSTOMER_ID_SEQ")
	private java.lang.Long id;

	@org.kie.api.definition.type.Label("First Name")
	private java.lang.String firstName;
	@org.kie.api.definition.type.Label("Last Name")
	private java.lang.String lastName;
	@org.kie.api.definition.type.Label("E-mail")
	private java.lang.String email;
	@org.kie.api.definition.type.Label("Address")
	private String address;

	public Customer() {
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	public java.lang.String getLastName() {
		return this.lastName;
	}

	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	public java.lang.String getEmail() {
		return this.email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getAddress() {
		return this.address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public Customer(java.lang.Long id, java.lang.String firstName,
			java.lang.String lastName, java.lang.String email,
			java.lang.String address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
	}

}