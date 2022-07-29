package com.rootlab.simpleboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private boolean enabled;

	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles = new ArrayList<>();

	// cascade = CascadeType.ALL : Entity의 상태 변화를 전파시키는 옵션
	// 외래키로 연결되어 있어 user정보를 제거할 수 없지만
	// 위의 옵션으로 user정보를 지우면서 외래키로 연결된 boards를 같이 제거하게 됨
	// orphanRemoval = true : 부모가 없는 데이터는 제거
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)

	// fetch = FetchType.LAZY -> 사용하는 데이터만 우선 가져오고 필요할때 따로 쿼리문 실행후 가져옴
	// fetch = FetchType.EAGER -> 사용하지 않는 데이터라도 쿼리문을 실행해 한꺼번에 가져옴
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Board> boards = new ArrayList<>();
}
