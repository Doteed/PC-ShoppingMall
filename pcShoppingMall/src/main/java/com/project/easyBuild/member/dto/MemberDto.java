package com.project.easyBuild.member.dto;

import java.util.Date;

public class MemberDto {
	private String userId;
	private int authId;
	private String password;
	private String userName;
	private String gender;
	private String email;
	private int phone;
	private Date registerDate;
	private Date lastUpdate;
	private String memberStatus;
	
	public MemberDto() {}

	public MemberDto(String userId, int authId, String password, String userName, String gender, String email,
			int phone, Date registerDate, Date lastUpdate, String memberStatus) {
		super();
		this.userId = userId;
		this.authId = authId;
		this.password = password;
		this.userName = userName;
		this.gender = gender;
		this.email = email;
		this.phone = phone;
		this.registerDate = registerDate;
		this.lastUpdate = lastUpdate;
		this.memberStatus = memberStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getAuthId() {
		return authId;
	}

	public void setAuthId(int authId) {
		this.authId = authId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

	@Override
	public String toString() {
		return "MemberDto [userId=" + userId + ", authId=" + authId + ", password=" + password + ", userName="
				+ userName + ", gender=" + gender + ", email=" + email + ", phone=" + phone + ", registerDate="
				+ registerDate + ", lastUpdate=" + lastUpdate + ", memberStatus=" + memberStatus + "]";
	}

	public void setPhone(String string) {
		// TODO Auto-generated method stub
		
	}
	
	
}
