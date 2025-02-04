package club.lowscore.app.entity;

import java.time.LocalDate;

import club.lowscore.app.genericentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
//@Table(name="t_user")
@Table(name="t_user", 
uniqueConstraints = {
    @UniqueConstraint(columnNames = "user_name", name = "uk_user_name"),
    @UniqueConstraint(columnNames = "email", name = "uk_email")
}
)
public class User extends BaseEntity{
	
	@Column(name="user_name", length=100)
	private String userName;
	
	@Column(name="email", length=300)
	private String email;
	
	@Column(name="pincode", length=10)
	private String pincode;
//	private LocalDateTime signup_date; we won't need this because we have createTime in BaseEntity
	private LocalDate dob;
	
	@Column(name="password", length=50)
	private String password;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
