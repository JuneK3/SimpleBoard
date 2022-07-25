package com.rootlab.simpleboard.model;

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
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Board> boards = new ArrayList<>();
}
