package com.pj.hrapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class Employee {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private Long employeeNumber;

	private String lastName;
	private String firstName;
	private String middleName;
	private String nickname;
	private Date birthday;
	private String address;
	private String contactNumber;
	private String sssNumber;
	private String philHealthNumber;
	private String tin;
	private String atmAccountNumber;
	private String magicCustomerCode;
	private Date hireDate;
	private PaySchedule paySchedule;

	public Employee() {
		// default constructor
	}
	
	public Employee(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Long employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}

	public String getSssNumber() {
		return sssNumber;
	}

	public void setSssNumber(String sssNumber) {
		this.sssNumber = sssNumber;
	}

	@Override
	public String toString() {
		return getFullName();
	}

	public String getFirstAndLastName() {
		return new StringBuilder()
				.append(firstName)
				.append(" ")
				.append(lastName)
				.toString();
	}

	public String getMagicCustomerCode() {
		return magicCustomerCode;
	}

	public void setMagicCustomerCode(String magicCustomerCode) {
		this.magicCustomerCode = magicCustomerCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPhilHealthNumber() {
		return philHealthNumber;
	}

	public void setPhilHealthNumber(String philHealthNumber) {
		this.philHealthNumber = philHealthNumber;
	}

	public String getAtmAccountNumber() {
		return atmAccountNumber;
	}

	public void setAtmAccountNumber(String atmAccountNumber) {
		this.atmAccountNumber = atmAccountNumber;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public PaySchedule getPaySchedule() {
		return paySchedule;
	}

	public void setPaySchedule(PaySchedule paySchedule) {
		this.paySchedule = paySchedule;
	}
	
	public String getFullName() {
		return new StringBuilder().append(firstName).append(" ").append(lastName).toString();
	}

	public static Employee withId(long id) {
		return new Employee(id);
	}
}
