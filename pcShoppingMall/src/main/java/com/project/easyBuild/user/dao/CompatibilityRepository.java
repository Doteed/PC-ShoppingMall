package com.project.easyBuild.user.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.user.dto.CartDto;
import com.project.easyBuild.user.dto.CompatibilityCheckRequest;
import com.project.easyBuild.user.dto.CompatibilityResultDto;
import com.project.easyBuild.user.dto.ComponentDto;

@Repository
public class CompatibilityRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<CartDto> fetchComponents(String userId, String type) {
		String sql = "SELECT c.PRODUCT_ID AS productId, c.PRODUCT_TYPE AS productType, p.P_NAME AS productName "
				+ " FROM CART c "
				+ " JOIN PRODUCT p ON c.PRODUCT_ID = p.PRODUCT_ID "
				+ " WHERE c.USER_ID = ? AND c.PRODUCT_TYPE = ? ";
		return jdbcTemplate.query(sql, new Object[]{userId, type}, new BeanPropertyRowMapper<>(CartDto.class));
	}

	public List<CompatibilityResultDto> checkCompatibility(CompatibilityCheckRequest request) {
		List<CompatibilityResultDto> results = new ArrayList<>();

		//CPU - Mainboard
		boolean cpuMainboardCompatible = checkCpuMainboardCompatibility(request.getCpuId(), request.getMainboardId());
		results.add(new CompatibilityResultDto("CPU-메인보드 호환성", cpuMainboardCompatible));

		//CPU - Memory
		boolean cpuMemoryCompatible = checkCpuMemoryCompatibility(request.getCpuId(), request.getMemoryId());
		results.add(new CompatibilityResultDto("CPU-메모리 호환성", cpuMemoryCompatible));

		//Mainboard - Memory
		boolean memoryMainboardCompatible = checkMemoryMainboardCompatibility(request.getMemoryId(),
				request.getMainboardId());
		results.add(new CompatibilityResultDto("메인보드-메모리 호환성", memoryMainboardCompatible));

		//Mainboard - Case
		boolean mainboardCaseCompatible = checkMainboardCaseCompatibility(request.getMainboardId(),
				request.getCaseId());
		results.add(new CompatibilityResultDto("메인보드-케이스 호환성", mainboardCaseCompatible));

		//Power - Case
		boolean powerCaseCompatible = checkPowerCaseCompatibility(request.getPowerId(), request.getCaseId());
		results.add(new CompatibilityResultDto("파워-케이스 호환성", powerCaseCompatible));

		//GPU - Case
		boolean gpuCaseCompatible = checkGpuCaseCompatibility(request.getGpuId(), request.getCaseId());
		results.add(new CompatibilityResultDto("그래픽카드-케이스 호환성", gpuCaseCompatible));

		return results;
	}

	//CPU 소켓 == 메인보드의 CPU소켓
	private boolean checkCpuMainboardCompatibility(int cpuId, int mainboardId) {
		//c.SOCKET = AM5, m.SOCKET = AMD(소켓AM5) 
		String query = "SELECT COUNT(*) FROM CPU c "
				+ " JOIN MAINBOARD m ON m.SOCKET LIKE '%' || c.SOCKET || '%' "
	            + " WHERE c.CPU_ID = ? AND m.MAINBOARD_ID = ?";
		return jdbcTemplate.queryForObject(query, new Object[] { cpuId, mainboardId }, Integer.class) > 0;
	}

	//CPU 메모리 규격 == 메모리의 규격
	private boolean checkCpuMemoryCompatibility(int cpuId, int memoryId) {
		String query = "SELECT COUNT(*) FROM CPU c JOIN Ram r ON c.SUPPORTED_MEMORY_STANDARD = r.PRODUCT_CLASSIFICATION WHERE c.CPU_ID = ? AND r.RAM_ID = ?";
		return jdbcTemplate.queryForObject(query, new Object[] { cpuId, memoryId }, Integer.class) > 0;
	}

	//메인보드의 메모리 규격 == 메모리의 규격
	private boolean checkMemoryMainboardCompatibility(int memoryId, int mainboardId) {
		String query = "SELECT COUNT(*) FROM RAM r JOIN MAINBOARD m ON r.PRODUCT_CLASSIFICATION = m.SUPPORTED_MEMORY_STANDARD WHERE r.RAM_ID = ? AND m.MAINBOARD_ID = ?";
		System.out.println("Mainboard - Memory : "+jdbcTemplate.queryForObject(query, new Object[] { memoryId, mainboardId }, Integer.class));
		return jdbcTemplate.queryForObject(query, new Object[] { memoryId, mainboardId }, Integer.class) > 0;
	}

	//메인보드 폼팩터 == 케이스 지원보드규격
	private boolean checkMainboardCaseCompatibility(int mainboardId, int caseId) {
		//m.FORM_FACTOR = ATX, c.SUPPORTED_BOARD_STANDARD = ATX, M-ATX, ITX
		//구분자 ,로 비교
		String query = "SELECT COUNT(*) "
                + " FROM MAINBOARD m "
                + " JOIN CASE c ON REGEXP_LIKE(c.SUPPORTED_BOARD_STANDARD, '^' || m.FORM_FACTOR || '(,|$)') "
                + " WHERE m.MAINBOARD_ID = ? AND c.CASE = ? ";
		return jdbcTemplate.queryForObject(query, new Object[] { mainboardId, caseId }, Integer.class) > 0;
	}

	//파워 제품분류 == 케이스 지원파워규격
	private boolean checkPowerCaseCompatibility(int powerId, int caseId) {
		//p.PSU_STANDARD = ATX, c.POWER_SUPPLY_STANDARD = 표준-ATX
		String query = "SELECT COUNT(*) "
				+ " FROM POWER p "
				+ " JOIN CASE c ON REPLACE(c.POWER_SUPPLY_STANDARD, '표준-', '') = p.PSU_STANDARD "
				+ " WHERE p.POWER_ID = ? AND c.CASE = ?";
		return jdbcTemplate.queryForObject(query, new Object[] { powerId, caseId }, Integer.class) > 0;
	}

	//그래픽카드 길이 == 케이스 VGA장착길이
	private boolean checkGpuCaseCompatibility(int gpuId, int caseId) {
		String query = "SELECT COUNT(*) FROM GRAPHIC_CARD g JOIN CASE c ON g.VGA_LENGTH <= c.VGA_LENGTH WHERE g.GRAPHIC_CARD_ID = ? AND c.CASE = ?";
		return jdbcTemplate.queryForObject(query, new Object[] { gpuId, caseId }, Integer.class) > 0;
	}
}
