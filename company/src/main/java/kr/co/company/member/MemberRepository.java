package kr.co.company.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
	
	boolean existsByUserIdAndIsLeave(String userId, boolean isLeave);
	Member findByUserIdAndIsLeave(String userId, boolean isLeave);

}
