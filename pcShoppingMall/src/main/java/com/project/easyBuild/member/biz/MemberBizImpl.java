package com.project.easyBuild.member.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.easyBuild.member.dao.MemberDao;
import com.project.easyBuild.member.dto.MemberDto;
import com.project.easyBuild.member.util.MailUtils;
import com.project.easyBuild.member.util.TempKey;

@Service
public class MemberBizImpl implements MemberBiz {

	private JavaMailSender mailSender;
	
    private static final Logger logger = LoggerFactory.getLogger(MemberBizImpl.class);

    @Autowired
    private MemberDao dao;

    @Override
    public MemberDto login(MemberDto dto) {
        logger.debug("Attempting login for user: {}", dto.getUserId());
        return dao.login(dto);
    }

    @Override
    public int insert(MemberDto dto) {
        logger.debug("Inserting user: {}", dto.getUserId());
        // Use the boolean result directly
        return dao.insert(dto);  // 아이디가 중복되지 않으면 회원가입 처리
    }

    @Override
    public int delete(String userId) {
        logger.debug("Deleting user: {}", userId);
        return dao.delete(userId);
    }
    
    @Override
    public MemberDto selectOne(String userId) {
    	return dao.selectOne(userId);
    }
    
    @Override
    public int update(MemberDto dto) {
    	return dao.update(dto);
    }

	@Override
	public MemberDto find_id(MemberDto dto) {
		return dao.find_id(dto);
	}
	@Override
	public int findPwCheck(MemberDto dto)throws Exception{
		return dao.findPwCheck(dto);
	}
    
    @Override
	public void findPw(String email,String userId)throws Exception{
		String memberKey = new TempKey().getKey(6,false);
		 dao.findPw(email,userId,memberKey);
		 MailUtils sendMail = new MailUtils(mailSender);
			sendMail.setSubject("[PC-Shoppingmall 임시 비밀번호 입니다.]"); //메일제목
			sendMail.setText(
					"<h1>임시비밀번호 발급</h1>" +
							"<br/>"+userId+"님 "+
							"<br/>비밀번호 찾기를 통한 임시 비밀번호입니다."+
							"<br/>임시비밀번호 :   <h2>"+memberKey+"</h2>"+
							"<br/>로그인 후 비밀번호 변경을 해주세요."+
							"<a href='http://localhost:8080/member/login"+
							">로그인 페이지</a>");
			sendMail.setFrom("[보낼이메일]", "PC-Shoppingmall");
			sendMail.setTo(email);
			sendMail.send();
	}
}
