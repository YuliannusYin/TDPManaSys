<template>
  <div class="portrait-page">
    <div class="page-header">
      <div>
        <h2 class="page-title">数字画像</h2>
        <p class="page-desc">多维度科研成果综合评估与可视化分析</p>
      </div>
      <el-select v-if="isAdmin" v-model="selectedUserId" placeholder="选择教师" filterable style="width:240px" @change="onUserChange">
        <el-option v-for="u in teacherList" :key="u.id" :label="u.name + ' (' + u.college + ')'" :value="u.id" />
      </el-select>
      <span v-else class="current-user-badge">{{ userStore.userName || '当前用户' }}</span>
    </div>

    <div class="summary-grid">
      <div v-for="item in summaryItems" :key="item.label" class="summary-card" :style="{ '--card-accent': item.accent }">
        <div class="summary-value" :style="{ color: item.colorFn ? item.colorFn(item.value) : 'var(--color-text-primary)' }">{{ item.display }}</div>
        <div class="summary-label">{{ item.label }}</div>
      </div>
    </div>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <div class="section-header">
              <span class="section-title">综合能力雷达图</span>
            </div>
          </template>
          <RadarChart ref="radarRef" :radar-data="radarData" :compare-mode="compareMode" :compare-data="compareData" :teacher-list="teacherList" @update:compare-data="onCompareData" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <div class="section-header">
              <span class="section-title">成果分布</span>
            </div>
          </template>
          <DistributionChart :user-id="selectedUserId" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="never">
          <template #header>
            <div class="section-header">
              <span class="section-title">历年趋势</span>
            </div>
          </template>
          <TrendChart :trend-data="trendData" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../../store/user'
import { getPortraitRadar, getPortraitDashboard, getPortraitTrend, getPortraitTeachers } from '../../api/portrait'
import RadarChart from './RadarChart.vue'
import TrendChart from './TrendChart.vue'
import DistributionChart from './DistributionChart.vue'

const route = useRoute()
const userStore = useUserStore()
const isAdmin = computed(() => userStore.role === 'ADMIN')
const currentUserId = computed(() => userStore.userInfo?.userId)

const selectedUserId = ref(null)
const teacherList = ref([])
const radarData = ref(null)
const dashboard = ref({ totalFunding: 0, paperACount: 0, paperBCount: 0, patentGrantedCount: 0, softwareCount: 0, competitionAwardCount: 0, rawScores: {}, normalizedScores: {} })
const trendData = ref([])
const compareMode = ref(false)
const compareData = ref(null)
const radarRef = ref(null)

const avgScore = computed(() => {
  const ns = dashboard.value.normalizedScores || {}
  const dims = ['科研项目', '专利成果', '软件著作', '学术论文', '竞赛指导']
  const sum = dims.reduce((s, d) => s + Number(ns[d] || 0), 0)
  return dims.length > 0 ? sum / dims.length : 0
})

function scoreColor(v) { return v >= 60 ? 'var(--color-success)' : v >= 30 ? 'var(--color-warning)' : 'var(--color-danger)' }

function fmtMoney(v) { return v ? Number(v).toFixed(1) : '0.0' }

const summaryItems = computed(() => {
  const d = dashboard.value
  return [
    { label: '项目总经费(万)', display: fmtMoney(d.totalFunding), accent: 'var(--color-teal)' },
    { label: 'A/B类论文', display: `${d.paperACount} / ${d.paperBCount}`, accent: 'var(--color-rose)' },
    { label: '已授权专利', display: d.patentGrantedCount, accent: 'var(--color-amber)' },
    { label: '软件著作', display: d.softwareCount, accent: 'var(--color-info)' },
    { label: '竞赛获奖', display: d.competitionAwardCount, accent: 'var(--color-success)' },
    { label: '综合均分', display: avgScore.value.toFixed(1), accent: 'var(--color-accent)', colorFn: scoreColor, value: avgScore.value },
  ]
})

async function loadTeachers() {
  try {
    const res = await getPortraitTeachers()
    teacherList.value = res.data || []
  } catch { /* ignore */ }
}

async function loadData(uid) {
  try {
    const [radarRes, dashRes, trendRes] = await Promise.all([
      getPortraitRadar(uid), getPortraitDashboard(uid), getPortraitTrend(uid)
    ])
    if (radarRes.code === 200) radarData.value = radarRes.data
    if (dashRes.code === 200) dashboard.value = dashRes.data
    if (trendRes.code === 200) trendData.value = trendRes.data
  } catch { /* ignore */ }
}

function onUserChange(uid) {
  compareMode.value = false
  compareData.value = null
  loadData(uid)
}

function onCompareData(data) {
  compareData.value = data
}

onMounted(async () => {
  const routeUserId = route.params.userId
  if (isAdmin.value) {
    await loadTeachers()
    selectedUserId.value = routeUserId ? Number(routeUserId) : (teacherList.value[0]?.id || currentUserId.value)
  } else {
    selectedUserId.value = currentUserId.value
  }
  if (selectedUserId.value) loadData(selectedUserId.value)
})

watch(() => route.params.userId, (val) => {
  if (val) { selectedUserId.value = Number(val); loadData(Number(val)) }
})
</script>

<style scoped>
.portrait-page {
  max-width: 1200px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.page-title {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 4px;
}

.page-desc {
  font-size: 13px;
  color: var(--color-text-muted);
}

.current-user-badge {
  font-size: 14px;
  color: var(--color-accent);
  font-weight: 600;
  padding: 6px 16px;
  background: var(--color-accent-glow);
  border-radius: var(--radius-sm);
  border: 1px solid rgba(200, 164, 92, 0.2);
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 14px;
}

.summary-card {
  background: var(--color-card);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-md);
  padding: 20px 16px;
  text-align: center;
  position: relative;
  overflow: hidden;
  transition: all var(--transition-base);
}

.summary-card::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 20%;
  right: 20%;
  height: 2px;
  background: var(--card-accent);
  opacity: 0;
  transition: opacity var(--transition-base);
}

.summary-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.summary-card:hover::after {
  opacity: 1;
}

.summary-value {
  font-size: 26px;
  font-weight: 700;
  color: var(--color-text-primary);
  font-variant-numeric: tabular-nums;
  line-height: 1.2;
}

.summary-label {
  font-size: 12px;
  color: var(--color-text-muted);
  margin-top: 6px;
}

.section-header {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text-primary);
}
</style>