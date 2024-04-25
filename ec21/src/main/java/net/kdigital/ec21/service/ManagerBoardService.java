package net.kdigital.ec21.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.repository.ProductRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ManagerBoardService {
    private final ProductRepository productRepository;

    public List<Map<String, Object>> getRegisterProductCount() {
        List<Object[]> results = productRepository.countProductsByCreationDate();
        return results.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("createDate", result[0]); // Assuming result[0] is the createDate
                    map.put("count", result[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Map<String, Integer>> getCategoryData() {
        List<Object[]> results = productRepository.findCategoryCounts();
        Map<String, Map<String, Integer>> categoryData = new HashMap<>();

        for (Object[] result : results) {
            String category = (String) result[0];
            Integer judge0Count = ((Number) result[1]).intValue();
            Integer judge1Count = ((Number) result[2]).intValue();

            Map<String, Integer> counts = new HashMap<>();
            counts.put("이상", judge0Count);
            counts.put("정상", judge1Count);

            categoryData.put(category, counts);
        }

        return categoryData;
    }

    // 특정기간에 다른 케테고리 데이터 반환
    public List<Map<String, Object>> getCategoryDataByDateRange(String start, String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        // LocalDate를 LocalDateTime으로 변환하여 자정 시간을 사용합니다.
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay(); // 다음 날 자정을 끝으로 사용합니다.

        List<Object[]> results = productRepository.countProductsByCategoryAndDateRange(startDateTime, endDateTime);
        return results.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", result[0]); // 날짜
                    map.put("category", result[1]); // 카테고리
                    map.put("count", result[2]); // 갯수
                    return map;
                })
                .collect(Collectors.toList());
    }

}
