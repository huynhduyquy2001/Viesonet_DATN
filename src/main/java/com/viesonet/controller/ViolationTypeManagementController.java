package com.viesonet.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.ViolationTypes;
import com.viesonet.service.ViolationTypesService;

import jakarta.transaction.Transactional;

@RestController
public class ViolationTypeManagementController {
	@Autowired
	private ViolationTypesService violationTypesService;
	
	@GetMapping("/staff/violation_type_management")
	public ModelAndView getPage() {
		ModelAndView modelAndView = new ModelAndView("/staff/ViolationTypeManagement");
		return modelAndView;
	}
	
	@GetMapping("/staff/violationtype/load")
	public ResponseEntity<List<ViolationTypes>> violationType() {
		List<ViolationTypes> result = violationTypesService.findAll();
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/staff/violationtype/detailViolation/{violationTypesId}")
	public ViolationTypes getViolationsByPostId(@PathVariable int violationTypesId) {
	    return violationTypesService.findByViolationTypesId(violationTypesId);
	}

	@PostMapping("/staff/violationtype/updateViolationDescription/{violationTypesId}")
	@Transactional
	public ResponseEntity<Map<String, String>> updateViolationDescription(@PathVariable("violationTypesId") int violationTypeId, @RequestBody Map<String, Object> data) {
	    String violationDescription = (String) data.get("violationDescription");

	    // Tìm ViolationTypes dựa vào violationTypeId
	    ViolationTypes violationTypes = violationTypesService.findByViolationTypesId(violationTypeId);

	    if (violationTypes == null) {
	        return ResponseEntity.ok().body(Collections.singletonMap("message", "Không tìm thấy vi phạm với mã vi phạm đã cho."));
	    }

	    // Cập nhật violationDescription
	    violationTypes.setViolationDescription(violationDescription);
	    violationTypesService.save(violationTypes);

	    return ResponseEntity.ok().build();
	}

	 @PostMapping("/staff/violationtype/addViolationDescription")
	    @Transactional
	    public ResponseEntity<?> addViolation(@RequestBody ViolationTypes violationTypes) {
	        try {
	            // Gọi phương thức trong service để thêm violation vào cơ sở dữ liệu
	            violationTypesService.save(violationTypes);
	            return ResponseEntity.ok().build();
	        } catch (Exception e) {
	            // Xử lý lỗi nếu có
	            return ResponseEntity.ok().body(Collections.singletonMap("message", "Lỗi không thêm được dữ liệu"));
	        }
	    }
	 
	 @GetMapping("/staff/violationtype/searchViolationDescription")
		public ResponseEntity<List<Object>> searchViolationType(@RequestParam String description) {
		    List<Object> searchResult = violationTypesService.findViolationTypes(description);
		    return ResponseEntity.ok(searchResult);
		}
	
}
