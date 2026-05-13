package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portrait.entity.*;
import com.portrait.mapper.*;
import com.portrait.vo.PortraitDashboardVO;
import com.portrait.vo.PortraitDistributionVO;
import com.portrait.vo.PortraitRadarVO;
import com.portrait.vo.PortraitTrendVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScoreCalculationService {

    @Resource
    private ScoreConfigMapper scoreConfigMapper;

    @Resource
    private VerticalProjectMapper verticalProjectMapper;

    @Resource
    private HorizontalProjectMapper horizontalProjectMapper;

    @Resource
    private PatentMapper patentMapper;

    @Resource
    private PatentTransferMapper patentTransferMapper;

    @Resource
    private SoftwareCopyrightMapper softwareCopyrightMapper;

    @Resource
    private PaperMapper paperMapper;

    @Resource
    private PaperIndexMapper paperIndexMapper;

    @Resource
    private CompetitionMapper competitionMapper;

    @Resource
    private UserMapper userMapper;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private volatile Map<String, BigDecimal> cachedGlobalMaxes;

    public PortraitRadarVO calculateRadar(Long userId) {
        User user = userMapper.selectById(userId);
        Map<String, BigDecimal> raw = calculateRawScores(userId);
        Map<String, BigDecimal> normalized = normalizeScores(raw, userId);
        PortraitRadarVO vo = new PortraitRadarVO();
        vo.setUserId(userId);
        vo.setUserName(user != null ? user.getName() : "");
        vo.setCollege(user != null ? user.getCollege() : "");
        vo.setRawScores(raw);
        vo.setNormalizedScores(normalized);
        return vo;
    }

    public PortraitDashboardVO calculateDashboard(Long userId) {
        PortraitDashboardVO vo = new PortraitDashboardVO();
        User user = userMapper.selectById(userId);

        BigDecimal totalFunding = BigDecimal.ZERO;
        List<VerticalProject> verticalList = verticalProjectMapper.selectList(
                new LambdaQueryWrapper<VerticalProject>().eq(VerticalProject::getUserId, userId));
        for (VerticalProject vp : verticalList) {
            if (vp.getFunding() != null) {
                totalFunding = totalFunding.add(vp.getFunding());
            }
        }
        List<HorizontalProject> horizontalList = horizontalProjectMapper.selectList(
                new LambdaQueryWrapper<HorizontalProject>().eq(HorizontalProject::getUserId, userId));
        for (HorizontalProject hp : horizontalList) {
            if (hp.getContractAmount() != null) {
                totalFunding = totalFunding.add(hp.getContractAmount());
            }
        }
        vo.setTotalFunding(totalFunding);

        List<Paper> paperList = paperMapper.selectList(
                new LambdaQueryWrapper<Paper>().eq(Paper::getUserId, userId));
        Map<Long, String> paperClassMap = loadPaperClasses(paperList);
        long aCount = 0, bCount = 0;
        for (Paper p : paperList) {
            String cls = paperClassMap.getOrDefault(p.getId(), "D");
            if ("A".equals(cls)) aCount++;
            else if ("B".equals(cls)) bCount++;
        }
        vo.setPaperACount(aCount);
        vo.setPaperBCount(bCount);

        Long patentGranted = patentMapper.selectCount(
                new LambdaQueryWrapper<Patent>().eq(Patent::getUserId, userId).eq(Patent::getStatus, "已授权"));
        vo.setPatentGrantedCount(patentGranted);

        Long softwareCount = softwareCopyrightMapper.selectCount(
                new LambdaQueryWrapper<SoftwareCopyright>().eq(SoftwareCopyright::getUserId, userId));
        vo.setSoftwareCount(softwareCount);

        Long competitionCount = competitionMapper.selectCount(
                new LambdaQueryWrapper<Competition>().eq(Competition::getUserId, userId));
        vo.setCompetitionAwardCount(competitionCount);

        Map<String, BigDecimal> raw = calculateRawScores(userId);
        vo.setRawScores(raw);
        vo.setNormalizedScores(normalizeScores(raw, userId));

        return vo;
    }

    public List<PortraitRadarVO> compareRadars(List<Long> userIds) {
        List<PortraitRadarVO> result = new ArrayList<>();
        for (Long uid : userIds) {
            result.add(calculateRadar(uid));
        }
        Map<String, BigDecimal> globalMaxes = new HashMap<>();
        for (PortraitRadarVO vo : result) {
            for (Map.Entry<String, BigDecimal> e : vo.getRawScores().entrySet()) {
                BigDecimal cur = globalMaxes.get(e.getKey());
                if (cur == null || e.getValue().compareTo(cur) > 0) {
                    globalMaxes.put(e.getKey(), e.getValue());
                }
            }
        }
        for (PortraitRadarVO vo : result) {
            Map<String, BigDecimal> norm = new LinkedHashMap<>();
            for (String dim : new String[]{"科研项目", "专利成果", "软件著作", "学术论文", "竞赛指导"}) {
                BigDecimal raw = vo.getRawScores().getOrDefault(dim, BigDecimal.ZERO);
                BigDecimal max = globalMaxes.getOrDefault(dim, BigDecimal.ONE);
                if (max.compareTo(BigDecimal.ZERO) == 0) max = BigDecimal.ONE;
                norm.put(dim, raw.multiply(BigDecimal.valueOf(100)).divide(max, 2, RoundingMode.HALF_UP));
            }
            vo.setNormalizedScores(norm);
        }
        return result;
    }

    public List<PortraitTrendVO> calculateTrend(Long userId) {
        List<PortraitTrendVO> list = new ArrayList<>();
        java.time.LocalDate now = java.time.LocalDate.now();
        int endYear = now.getYear();
        int startYear = endYear - 4;

        for (int year = startYear; year <= endYear; year++) {
            PortraitTrendVO vo = new PortraitTrendVO();
            vo.setYear(String.valueOf(year));
            String beginStr = year + "-01-01";
            String endStr = year + "-12-31";

            long vCount = verticalProjectMapper.selectCount(
                    new LambdaQueryWrapper<VerticalProject>().eq(VerticalProject::getUserId, userId)
                            .ge(VerticalProject::getStartDate, beginStr).le(VerticalProject::getStartDate, endStr));
            long hCount = horizontalProjectMapper.selectCount(
                    new LambdaQueryWrapper<HorizontalProject>().eq(HorizontalProject::getUserId, userId)
                            .ge(HorizontalProject::getSignDate, beginStr).le(HorizontalProject::getSignDate, endStr));
            vo.setProjectCount(vCount + hCount);

            BigDecimal vFunding = verticalProjectMapper.selectList(
                    new LambdaQueryWrapper<VerticalProject>().eq(VerticalProject::getUserId, userId)
                            .ge(VerticalProject::getStartDate, beginStr).le(VerticalProject::getStartDate, endStr))
                    .stream().map(v -> v.getFunding() == null ? BigDecimal.ZERO : v.getFunding())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal hFunding = horizontalProjectMapper.selectList(
                    new LambdaQueryWrapper<HorizontalProject>().eq(HorizontalProject::getUserId, userId)
                            .ge(HorizontalProject::getSignDate, beginStr).le(HorizontalProject::getSignDate, endStr))
                    .stream().map(h -> h.getContractAmount() == null ? BigDecimal.ZERO : h.getContractAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setProjectFunding(vFunding.add(hFunding));

            vo.setPatentCount(patentMapper.selectCount(
                    new LambdaQueryWrapper<Patent>().eq(Patent::getUserId, userId)
                            .ge(Patent::getApplicationDate, beginStr).le(Patent::getApplicationDate, endStr)));

            vo.setSoftwareCount(softwareCopyrightMapper.selectCount(
                    new LambdaQueryWrapper<SoftwareCopyright>().eq(SoftwareCopyright::getUserId, userId)
                            .ge(SoftwareCopyright::getRegistrationDate, beginStr).le(SoftwareCopyright::getRegistrationDate, endStr)));

            vo.setPaperCount(paperMapper.selectCount(
                    new LambdaQueryWrapper<Paper>().eq(Paper::getUserId, userId)
                            .ge(Paper::getPublishDate, beginStr).le(Paper::getPublishDate, endStr)));

            vo.setCompetitionCount(competitionMapper.selectCount(
                    new LambdaQueryWrapper<Competition>().eq(Competition::getUserId, userId)
                            .ge(Competition::getCompetitionDate, beginStr).le(Competition::getCompetitionDate, endStr)));

            list.add(vo);
        }
        return list;
    }

    public PortraitDistributionVO calculateDistribution(Long userId) {
        PortraitDistributionVO vo = new PortraitDistributionVO();

        List<VerticalProject> vList = verticalProjectMapper.selectList(
                new LambdaQueryWrapper<VerticalProject>().eq(VerticalProject::getUserId, userId));
        Map<String, Integer> vLevel = new LinkedHashMap<>();
        vLevel.put("国家级", 0); vLevel.put("省部级", 0); vLevel.put("市厅级", 0); vLevel.put("校级", 0);
        for (VerticalProject vp : vList) {
            if (vp.getLevel() != null) vLevel.merge(vp.getLevel(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> e : vLevel.entrySet()) {
            if (e.getValue() > 0) {
                Map<String, Object> item = new HashMap<>(); item.put("name", e.getKey()); item.put("value", e.getValue()); vo.getProjectLevel().add(item);
            }
        }

        List<Patent> pList = patentMapper.selectList(
                new LambdaQueryWrapper<Patent>().eq(Patent::getUserId, userId));
        Map<String, Integer> pType = new LinkedHashMap<>();
        pType.put("发明专利", 0); pType.put("实用新型", 0); pType.put("外观设计", 0);
        for (Patent p : pList) {
            if (p.getType() != null) pType.merge(p.getType(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> e : pType.entrySet()) {
            if (e.getValue() > 0) {
                Map<String, Object> item = new HashMap<>(); item.put("name", e.getKey()); item.put("value", e.getValue()); vo.getPatentType().add(item);
            }
        }

        List<Paper> paperList = paperMapper.selectList(
                new LambdaQueryWrapper<Paper>().eq(Paper::getUserId, userId));
        Map<Long, String> classMap = loadPaperClasses(paperList);
        Map<String, Integer> pClass = new LinkedHashMap<>();
        pClass.put("A类(SCI/SSCI)", 0); pClass.put("B类(EI/CSCD)", 0); pClass.put("C类(CSSCI/北大核心)", 0); pClass.put("D类(其他)", 0);
        for (Paper p : paperList) {
            String cls = classMap.getOrDefault(p.getId(), "D");
            switch (cls) {
                case "A": pClass.merge("A类(SCI/SSCI)", 1, Integer::sum); break;
                case "B": pClass.merge("B类(EI/CSCD)", 1, Integer::sum); break;
                case "C": pClass.merge("C类(CSSCI/北大核心)", 1, Integer::sum); break;
                default: pClass.merge("D类(其他)", 1, Integer::sum);
            }
        }
        for (Map.Entry<String, Integer> e : pClass.entrySet()) {
            if (e.getValue() > 0) {
                Map<String, Object> item = new HashMap<>(); item.put("name", e.getKey()); item.put("value", e.getValue()); vo.getPaperClass().add(item);
            }
        }

        List<Competition> cList = competitionMapper.selectList(
                new LambdaQueryWrapper<Competition>().eq(Competition::getUserId, userId));
        Map<String, Integer> cLevel = new LinkedHashMap<>();
        cLevel.put("国家级", 0); cLevel.put("省级", 0); cLevel.put("校级", 0);
        for (Competition c : cList) {
            if (c.getAwardLevel() != null) cLevel.merge(c.getAwardLevel(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> e : cLevel.entrySet()) {
            if (e.getValue() > 0) {
                Map<String, Object> item = new HashMap<>(); item.put("name", e.getKey()); item.put("value", e.getValue()); vo.getCompetitionLevel().add(item);
            }
        }
        return vo;
    }

    private Map<String, BigDecimal> calculateRawScores(Long userId) {
        Map<String, BigDecimal> scores = new LinkedHashMap<>();
        scores.put("科研项目", calcProjectScore(userId));
        scores.put("专利成果", calcPatentScore(userId));
        scores.put("软件著作", calcSoftwareScore(userId));
        scores.put("学术论文", calcPaperScore(userId));
        scores.put("竞赛指导", calcCompetitionScore(userId));
        return scores;
    }

    private Map<String, BigDecimal> normalizeScores(Map<String, BigDecimal> raw, Long excludeUserId) {
        Map<String, BigDecimal> globalMaxes = getGlobalMaxes();
        Map<String, BigDecimal> normalized = new LinkedHashMap<>();
        for (String dim : new String[]{"科研项目", "专利成果", "软件著作", "学术论文", "竞赛指导"}) {
            BigDecimal r = raw.getOrDefault(dim, BigDecimal.ZERO);
            BigDecimal max = globalMaxes.getOrDefault(dim, BigDecimal.ONE);
            if (max.compareTo(BigDecimal.ZERO) == 0) max = BigDecimal.ONE;
            normalized.put(dim, r.multiply(BigDecimal.valueOf(100)).divide(max, 2, RoundingMode.HALF_UP));
        }
        return normalized;
    }

    private Map<String, BigDecimal> getGlobalMaxes() {
        if (cachedGlobalMaxes != null) {
            return cachedGlobalMaxes;
        }
        synchronized (this) {
            if (cachedGlobalMaxes != null) {
                return cachedGlobalMaxes;
            }
            Map<String, BigDecimal> globalMaxes = new LinkedHashMap<>();
            for (String dim : new String[]{"科研项目", "专利成果", "软件著作", "学术论文", "竞赛指导"}) {
                globalMaxes.put(dim, BigDecimal.ZERO);
            }
            List<User> allUsers = userMapper.selectList(null);
            for (User u : allUsers) {
                Map<String, BigDecimal> ur = calculateRawScores(u.getId());
                for (Map.Entry<String, BigDecimal> e : ur.entrySet()) {
                    if (e.getValue().compareTo(globalMaxes.get(e.getKey())) > 0) {
                        globalMaxes.put(e.getKey(), e.getValue());
                    }
                }
            }
            cachedGlobalMaxes = globalMaxes;
            return globalMaxes;
        }
    }

    public void clearMaxCache() {
        cachedGlobalMaxes = null;
    }

    private BigDecimal calcProjectScore(Long userId) {
        BigDecimal score = BigDecimal.ZERO;
        List<VerticalProject> vList = verticalProjectMapper.selectList(
                new LambdaQueryWrapper<VerticalProject>().eq(VerticalProject::getUserId, userId));
        for (VerticalProject vp : vList) {
            BigDecimal base = BigDecimal.ZERO;
            switch (vp.getLevel() != null ? vp.getLevel() : "") {
                case "国家级": base = BigDecimal.valueOf(20); break;
                case "省部级": base = BigDecimal.valueOf(15); break;
                case "市厅级": base = BigDecimal.valueOf(10); break;
                case "校级": base = BigDecimal.valueOf(5); break;
            }
            if ("参与".equals(vp.getRole())) {
                base = base.multiply(BigDecimal.valueOf(0.5));
            }
            score = score.add(base);
        }
        List<HorizontalProject> hList = horizontalProjectMapper.selectList(
                new LambdaQueryWrapper<HorizontalProject>().eq(HorizontalProject::getUserId, userId));
        for (HorizontalProject hp : hList) {
            BigDecimal base = BigDecimal.ZERO;
            if ("主持".equals(hp.getRole()) && hp.getContractAmount() != null) {
                base = hp.getContractAmount().divide(BigDecimal.valueOf(10), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(2));
            } else if (hp.getContractAmount() != null) {
                base = hp.getContractAmount().divide(BigDecimal.valueOf(10), 2, RoundingMode.HALF_UP);
            }
            score = score.add(base);
        }
        return score.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcPatentScore(Long userId) {
        BigDecimal score = BigDecimal.ZERO;
        List<Patent> pList = patentMapper.selectList(
                new LambdaQueryWrapper<Patent>().eq(Patent::getUserId, userId));
        for (Patent p : pList) {
            BigDecimal base = BigDecimal.ZERO;
            switch (p.getType() != null ? p.getType() : "") {
                case "发明专利": base = BigDecimal.valueOf(20); break;
                case "实用新型": base = BigDecimal.valueOf(10); break;
                case "外观设计": base = BigDecimal.valueOf(5); break;
            }
            if ("申请中".equals(p.getStatus())) {
                base = base.multiply(BigDecimal.valueOf(0.5));
            }
            score = score.add(base);

            Long transferCount = patentTransferMapper.selectCount(
                    new LambdaQueryWrapper<PatentTransfer>().eq(PatentTransfer::getPatentId, p.getId()));
            score = score.add(BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(transferCount)));
        }
        return score.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcSoftwareScore(Long userId) {
        Long count = softwareCopyrightMapper.selectCount(
                new LambdaQueryWrapper<SoftwareCopyright>().eq(SoftwareCopyright::getUserId, userId));
        return BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(count)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcPaperScore(Long userId) {
        List<Paper> paperList = paperMapper.selectList(
                new LambdaQueryWrapper<Paper>().eq(Paper::getUserId, userId));
        Map<Long, String> classMap = loadPaperClasses(paperList);

        BigDecimal score = BigDecimal.ZERO;
        for (Paper p : paperList) {
            String cls = classMap.getOrDefault(p.getId(), "D");
            BigDecimal base = BigDecimal.ZERO;
            switch (cls) {
                case "A": base = BigDecimal.valueOf(25); break;
                case "B": base = BigDecimal.valueOf(15); break;
                case "C": base = BigDecimal.valueOf(10); break;
                case "D": base = BigDecimal.valueOf(5); break;
            }
            if (p.getAuthorOrder() != null && p.getAuthorOrder() != 1) {
                base = base.multiply(BigDecimal.valueOf(0.5));
            }
            score = score.add(base);
        }
        return score.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcCompetitionScore(Long userId) {
        List<Competition> cList = competitionMapper.selectList(
                new LambdaQueryWrapper<Competition>().eq(Competition::getUserId, userId));
        BigDecimal score = BigDecimal.ZERO;
        for (Competition c : cList) {
            BigDecimal base = BigDecimal.ZERO;
            String key = (c.getAwardLevel() != null ? c.getAwardLevel() : "") + "_"
                    + (c.getAwardGrade() != null ? c.getAwardGrade() : "");
            switch (key) {
                case "国家级_特等奖": base = BigDecimal.valueOf(30); break;
                case "国家级_一等奖": base = BigDecimal.valueOf(25); break;
                case "国家级_二等奖": base = BigDecimal.valueOf(20); break;
                case "国家级_三等奖": base = BigDecimal.valueOf(15); break;
                case "省级_特等奖": base = BigDecimal.valueOf(20); break;
                case "省级_一等奖": base = BigDecimal.valueOf(15); break;
                case "省级_二等奖": base = BigDecimal.valueOf(10); break;
                case "省级_三等奖": base = BigDecimal.valueOf(8); break;
                case "校级_特等奖": base = BigDecimal.valueOf(10); break;
                case "校级_一等奖": base = BigDecimal.valueOf(8); break;
                case "校级_二等奖": base = BigDecimal.valueOf(5); break;
                case "校级_三等奖": base = BigDecimal.valueOf(3); break;
                default: base = BigDecimal.ZERO;
            }
            if (c.getGuideRank() != null && c.getGuideRank() != 1) {
                base = base.multiply(BigDecimal.valueOf(0.7));
            }
            score = score.add(base);
        }
        return score.setScale(2, RoundingMode.HALF_UP);
    }

    private Map<Long, String> loadPaperClasses(List<Paper> paperList) {
        if (paperList.isEmpty()) return Collections.emptyMap();
        List<Long> paperIds = paperList.stream().map(Paper::getId).collect(Collectors.toList());
        List<PaperIndex> all = paperIndexMapper.selectList(
                new LambdaQueryWrapper<PaperIndex>().in(PaperIndex::getPaperId, paperIds));
        Map<Long, String> result = new HashMap<>();
        for (PaperIndex pi : all) {
            String cls = indexTypeToClass(pi.getIndexType());
            String existing = result.get(pi.getPaperId());
            if (existing == null || classPriority(cls) > classPriority(existing)) {
                result.put(pi.getPaperId(), cls);
            }
        }
        for (Paper p : paperList) {
            result.putIfAbsent(p.getId(), "D");
        }
        return result;
    }

    private String indexTypeToClass(String indexType) {
        if (indexType == null) return "D";
        switch (indexType) {
            case "SCI": case "SSCI": return "A";
            case "EI": case "CSCD": return "B";
            case "CSSCI": case "北大核心": return "C";
            default: return "D";
        }
    }

    private int classPriority(String cls) {
        switch (cls) {
            case "A": return 4;
            case "B": return 3;
            case "C": return 2;
            case "D": return 1;
            default: return 0;
        }
    }
}